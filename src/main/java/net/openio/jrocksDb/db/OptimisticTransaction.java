package net.openio.jrocksDb.db;

import net.openio.jrocksDb.config.Config;
import net.openio.jrocksDb.config.TransactionConfig;
import net.openio.jrocksDb.mem.KeyValueEntry;
import net.openio.jrocksDb.mem.MemTableRep;
import net.openio.jrocksDb.transaction.SequenceNumber;
import net.openio.jrocksDb.transaction.Snapshot;
import net.openio.jrocksDb.transaction.Transaction;

import java.util.ArrayList;
import java.util.List;


public class OptimisticTransaction implements Transaction {

    long transactionId;

    OptimisticTransactionDB dB;

    Snapshot snapshot;

    List<TransactionKeyValue> writeBatch = new ArrayList<>();


    @Override
    public Status SetSnapshot() {
        snapshot = new Snapshot();
        return Status.success();
    }


    @Override
    public Status ClearSnapshot() {
        snapshot = new Snapshot();
        return Status.success();
    }

    @Override
    public Status Commit() {
        int i = 0;

        for (TransactionKeyValue keyValue : writeBatch) {

            SequenceNumber s = dB.lockManager.TryLock(transactionId, keyValue.columnFamilyId, keyValue.keyValueEntry.getKey(),
                    true, true, snapshot.getNumber(), false);
            if (s == null) {
                break;
            }
            if (s != snapshot.getNumber()) {
                break;
            }
            i++;
        }

        if (i != writeBatch.size()) {
            for (TransactionKeyValue keyValue : writeBatch) {
                dB.lockManager.UnLock(transactionId, keyValue.columnFamilyId, keyValue.keyValueEntry.getKey(),
                        true, true, false);
                i--;
                if (i == 0) {
                    break;
                }
            }
            return Status.f();
        }



        for (TransactionKeyValue keyValue : writeBatch) {
            dB.columnFamilyHandleMap.get(keyValue.name).put(keyValue.getKeyValueEntry());
            dB.lockManager.UnLock(transactionId, keyValue.columnFamilyId, keyValue.keyValueEntry.getKey(),
                    true, true, false);
        }


        return Status.success();
    }

    @Override
    public Status Rollback() {
        writeBatch = new ArrayList<>();
        snapshot = new Snapshot();
        return Status.success();
    }

    @Override
    public Status<Value> Get(Key key, String ColumnFamilyName) {
        ColumnFamilyHandle columnFamilyHandle =
                dB.columnFamilyHandleMap.get(ColumnFamilyName);
        if (columnFamilyHandle == null) {
            return Status.NOTColumnFamilyHandle(ColumnFamilyName);
        }
        KeyValueEntry keyValueEntry = new KeyValueEntry(key, null, snapshot.getCommitId(), snapshot.getNumber(), snapshot.getPrepareId(), null);
        KeyValueEntry keyValue = columnFamilyHandle.get(keyValueEntry);
        if (keyValue == null || keyValue.getType() == KeyValueEntry.Type.delete) {
            return Status.GetDeleteValue();
        }
        return Status.Get(keyValue.getValue());

    }

    @Override
    public Status<Value> GetForUpdate(Key key, Value value, String ColumnFamilyName) {
        ColumnFamilyHandle columnFamilyHandle = null;
        if ((columnFamilyHandle = dB.columnFamilyHandleMap.get(ColumnFamilyName)) == null) {
            return Status.NOTColumnFamilyHandle(ColumnFamilyName);
        }
        if (key == null || !columnFamilyHandle.verify(key)) {
            return Status.CKeyTypeVerify();
        }
        if (value != null && !columnFamilyHandle.verify(value)) {
            return Status.CValueTypeVerify();
        }
        KeyValueEntry keyValueEntry = null;
        if (TransactionConfig.TransactionType.readCommit == TransactionConfig.type) {
            for (TransactionKeyValue keyValue : writeBatch) {
                if (keyValue.columnFamilyId.equals(columnFamilyHandle.columnFamilyId)) {
                    if (keyValue.keyValueEntry.getKey().compareTo(key) == 0) {
                        keyValueEntry = keyValue.getKeyValueEntry();
                        break;
                    }
                }
            }
        }
        KeyValueEntry keyValue = new KeyValueEntry(key, value,
                snapshot.getCommitId(), snapshot.getNumber(), snapshot.getPrepareId(), KeyValueEntry.Type.update);
        if (keyValueEntry == null) {
            tryLock(transactionId, key, columnFamilyHandle.columnFamilyId);
            keyValueEntry = columnFamilyHandle.get(keyValue);
            if (keyValueEntry == null || keyValueEntry.getType() == KeyValueEntry.Type.delete) {
                return Status.NotHasValue();
            }
        }

        synchronized (this) {
            writeBatch.add(new TransactionKeyValue(columnFamilyHandle.getColumnFamilyId()
                    , keyValue, columnFamilyHandle.name));

        }
        return Status.update(keyValueEntry.getValue());
    }

    @Override
    public Status<Value> Put(Key key, Value value, String ColumnFamilyName) {
        ColumnFamilyHandle columnFamilyHandle = null;
        if ((columnFamilyHandle = dB.columnFamilyHandleMap.get(ColumnFamilyName)) == null) {
            return Status.NOTColumnFamilyHandle(ColumnFamilyName);
        }
        if (key == null || !columnFamilyHandle.verify(key)) {
            return Status.CKeyTypeVerify();
        }
        if (value != null && !columnFamilyHandle.verify(value)) {
            return Status.CValueTypeVerify();
        }
        KeyValueEntry keyValueEntry = null;
        if (TransactionConfig.TransactionType.readCommit == TransactionConfig.type) {
            for (TransactionKeyValue keyValue : writeBatch) {
                if (keyValue.columnFamilyId.equals(columnFamilyHandle.columnFamilyId)) {
                    if (keyValue.keyValueEntry.getKey().compareTo(key) == 0) {
                        keyValueEntry = keyValue.getKeyValueEntry();
                        break;
                    }
                }
            }
        }
        KeyValueEntry keyValue = new KeyValueEntry(key, value,
                snapshot.getCommitId(), snapshot.getNumber(), snapshot.getPrepareId(), KeyValueEntry.Type.insert);
        if (keyValueEntry == null) {
            tryLock(transactionId, key, columnFamilyHandle.columnFamilyId);
            keyValueEntry = columnFamilyHandle.get(keyValue);
            if (keyValueEntry != null && keyValueEntry.getType() != KeyValueEntry.Type.delete) {
                return Status.hasValue();
            }
        } else {
            return Status.hasValue();
        }

        synchronized (this) {
            writeBatch.add(new TransactionKeyValue(columnFamilyHandle.getColumnFamilyId()
                    , keyValue, columnFamilyHandle.name));

        }
        return Status.put();
    }

    @Override
    public Status<Value> delete(Key key, String ColumnFamilyName) {
        ColumnFamilyHandle columnFamilyHandle = null;
        if ((columnFamilyHandle = dB.columnFamilyHandleMap.get(ColumnFamilyName)) == null) {
            return Status.NOTColumnFamilyHandle(ColumnFamilyName);
        }
        if (key == null || !columnFamilyHandle.verify(key)) {
            return Status.CKeyTypeVerify();
        }

        KeyValueEntry keyValueEntry = null;
        if (TransactionConfig.TransactionType.readCommit == TransactionConfig.type) {
            for (TransactionKeyValue keyValue : writeBatch) {
                if (keyValue.columnFamilyId.equals(columnFamilyHandle.columnFamilyId)) {
                    if (keyValue.keyValueEntry.getKey().compareTo(key) == 0) {
                        keyValueEntry = keyValue.getKeyValueEntry();
                        break;
                    }
                }
            }
        }
        KeyValueEntry keyValue = new KeyValueEntry(key, null,
                snapshot.getCommitId(), snapshot.getNumber(), snapshot.getPrepareId(), KeyValueEntry.Type.delete);
        if (keyValueEntry == null) {
            tryLock(transactionId, key, columnFamilyHandle.columnFamilyId);
            keyValueEntry = columnFamilyHandle.get(keyValue);
            if (keyValueEntry == null || keyValueEntry.getType() == KeyValueEntry.Type.delete) {
                return Status.NotHasValue();
            }
        }

        synchronized (this) {
            writeBatch.add(new TransactionKeyValue(columnFamilyHandle.getColumnFamilyId()
                    , keyValue, columnFamilyHandle.name));

        }
        return Status.Delete(keyValueEntry.getValue());
    }


    public OptimisticTransaction(OptimisticTransactionDB db, long tId) {
        this.transactionId = tId;
        this.dB = db;
    }

    private SequenceNumber tryLock(long tId, Key key, ColumnFamilyId columnFamilyId) {
        return dB.lockManager.TryLock
                (tId, columnFamilyId, key, false, false, snapshot.getNumber(), true);
    }

    private void unLock(long tId, Key key, ColumnFamilyId columnFamilyId) {
        dB.lockManager.UnLock
                (tId, columnFamilyId, key, false, false, true);
    }
}
