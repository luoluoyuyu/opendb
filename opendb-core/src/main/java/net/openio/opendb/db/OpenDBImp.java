/**
 * Licensed to the OpenIO.Net under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.openio.opendb.db;

import net.openio.opendb.log.Log;
import net.openio.opendb.log.WalLog;
import net.openio.opendb.memarena.MemArena;
import net.openio.opendb.model.ColumnFamilyDescriptor;
import net.openio.opendb.model.OperationType;
import net.openio.opendb.model.Options;
import net.openio.opendb.model.SequenceNumber;
import net.openio.opendb.model.Status;
import net.openio.opendb.model.key.Key;
import net.openio.opendb.model.value.Value;
import net.openio.opendb.storage.metadata.DataMeta;
import net.openio.opendb.storage.metadata.DataMetaTask;
import net.openio.opendb.storage.metadata.MetaMetaStorage;
import net.openio.opendb.storage.wal.LogStorage;
import net.openio.opendb.tool.codec.log.WalLogsProtoCodec;
import net.openio.opendb.transaction.Transaction;


import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class OpenDBImp implements OpenDB {

  private final String logDir = "log";

  private final String dir;

  private SnapshotManager snapshotManager;

  private WriteBatch writeBatch;

  private ColumnFamilyManager columnFamilyManager;

  private ScheduledExecutorService metaDataExecutor;

  private MetaMetaStorage metaMetaStorage;

  Comparator<Key> comparator;


  private OpenDBImp(String dir) {
    this.dir = dir;
  }

  public static OpenDB open(Options option, String dir) {
    DataMeta metaData = getMetaData(dir);
    if (metaData == null) {
      metaData = new DataMeta();
    }
    List<ColumnFamily> cfs = metaData.getColumnFamilies();
    List<String> logs = metaData.getWalLog();
    OpenDBImp openDBImp = new OpenDBImp(dir);
    openDBImp.snapshotManager = new SnapshotManager();
    openDBImp.columnFamilyManager = new ColumnFamilyManager(cfs, option, dir,
      openDBImp.snapshotManager);
    LogStorage logStorage = new LogStorage(new MemArena(option.logMemArenaSize, option.logBlockSize),
      option.logBlockSize, option.walLogHeadSize, new WalLogsProtoCodec(), dir + "/" + openDBImp.logDir + "/");
    metaData.getWalLog().add(logStorage.getFileName());
    if (option.asyLog) {
      openDBImp.writeBatch = new AsynchronousWriteBatch(logs, logStorage, option.logfileSize, metaData.getMaxNumber().getTimes());
    } else {
      openDBImp.writeBatch = new SynchronousWriteBatch(logs, logStorage, option.logfileSize, metaData.getMaxNumber().getTimes());
    }
    openDBImp.metaMetaStorage = new MetaMetaStorage();
    openDBImp.metaDataExecutor = Executors.newSingleThreadScheduledExecutor();
    openDBImp.metaDataExecutor.scheduleAtFixedRate(new DataMetaTask(openDBImp.metaMetaStorage,
        openDBImp.snapshotManager, openDBImp.columnFamilyManager, openDBImp.writeBatch, openDBImp.dir),
      2, 2, TimeUnit.SECONDS);

    List<Log> o = openDBImp.writeBatch.getWal(metaData.getUnPersistedSeqNumberLow());
    for (Log walLog : o) {
      WalLog wal = (WalLog) walLog;
      openDBImp.columnFamilyManager.add(wal.getKey(), wal.getValue(), wal.getColumnId());
    }
    if (option.transactionType == Options.TransactionType.readCommit) {
      openDBImp.comparator = (a, b) -> {
        return a.compareTo(b);
      };
    } else {
      openDBImp.comparator = (a, b) -> {
        int d = a.compareTo(b);
        if (d == 0) {
          d = a.getSequenceNumber().compareTo(b.getSequenceNumber());
        }
        return d;
      };
    }
    return openDBImp;
  }

  private static DataMeta getMetaData(String dir) {
    MetaMetaStorage metaMetaStorage = new MetaMetaStorage();
    return metaMetaStorage.getMetaData(dir);
  }

  @Override
  public Status<Value> get(Key key, ColumnFamilyHandle columnFamilyHandle) {
    SequenceNumber sequenceNumber = writeBatch.getMaxSequenceNumber();
    Snapshot snapshot = snapshotManager.addSnapshot(sequenceNumber);
    Status<Value> status = new Status<>(columnFamilyManager.get(columnFamilyHandle.getColumnFamilyId(), key, comparator));
    snapshotManager.removeSnapshot(snapshot);
    return status;
  }

  @Override
  public Status<Value> put(Key key, Value value, ColumnFamilyHandle columnFamilyHandle) {
    value.setType(OperationType.insert);
    key = key.copy();
    value = value.copy();
    writeBatch.addLog(new WalLog(columnFamilyHandle.getColumnFamilyId(), key, value, columnFamilyHandle.keyType,
      columnFamilyHandle.valueType));
    columnFamilyManager.add(key, value, columnFamilyHandle.getColumnFamilyId());
    return new Status<>(null);
  }

  @Override
  public Status<Value> update(Key key, Value value, ColumnFamilyHandle columnFamilyHandle) {
    SequenceNumber sequenceNumber = writeBatch.getMaxSequenceNumber();
    Snapshot snapshot = snapshotManager.addSnapshot(sequenceNumber);
    Value value1 = columnFamilyManager.get(columnFamilyHandle.getColumnFamilyId(), key, comparator);
    snapshotManager.removeSnapshot(snapshot);
    if (value1 == null) {
      return Status.fail();
    }
    key = key.copy();
    value.setType(OperationType.update);
    columnFamilyManager.add(key, value, columnFamilyHandle.getColumnFamilyId());
    return new Status<>(value1);
  }

  @Override
  public Status<Value> delete(Key key, ColumnFamilyHandle columnFamilyHandle) {
    SequenceNumber sequenceNumber = writeBatch.getMaxSequenceNumber();
    Snapshot snapshot = snapshotManager.addSnapshot(sequenceNumber);
    Value value = columnFamilyManager.get(columnFamilyHandle.getColumnFamilyId(), key, comparator);
    snapshotManager.removeSnapshot(snapshot);
    if (value == null) {
      Status.fail();
    }
    key = key.copy();
    Value v = value.copy();
    v.setType(OperationType.delete);
    columnFamilyManager.add(key, value, columnFamilyHandle.getColumnFamilyId());
    return new Status<>(value);
  }


  @Override
  public Status<ColumnFamilyHandle> getColumnFamilyHandle(String name) {
    ColumnFamilyHandle columnFamilyHandle = columnFamilyManager.getColumnFamilyHandle(name);
    if (columnFamilyHandle == null) {
      return Status.fail();
    }
    return new Status<>(columnFamilyHandle);
  }

  @Override
  public Status deleteColumnFamilyHandle(ColumnFamilyHandle columnFamilyHandle) {
    return null;
  }

  @Override
  public Status<ColumnFamilyDescriptor> getColumnFamily(String name) {
    return null;
  }

  @Override
  public Status<ColumnFamilyDescriptor> createColumnFamily(ColumnFamilyDescriptor columnFamilyDescriptor) {
    boolean a = columnFamilyManager.create(columnFamilyDescriptor);
    if (!a) {
      return Status.fail();
    }
    return new Status<>(columnFamilyDescriptor);
  }

  @Override
  public Status<List<ColumnFamilyDescriptor>> getAllColumnFamily() {
    return new Status<>(columnFamilyManager.getAll());
  }

  @Override
  public Transaction createTransaction(Snapshot snapshot) {
    return null;
  }

  @Override
  public Snapshot getSnapshot() {
    return null;
  }

  @Override
  public void close() {
    metaDataExecutor.shutdown();
    columnFamilyManager.close();
    writeBatch.close();
  }
}
