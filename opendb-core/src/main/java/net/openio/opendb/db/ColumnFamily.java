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


import net.openio.opendb.mem.BloomFilter;
import net.openio.opendb.mem.BufferCache;
import net.openio.opendb.mem.MemTable;
import net.openio.opendb.mem.SkipListRep;
import net.openio.opendb.model.SequenceNumber;
import net.openio.opendb.model.key.Key;
import net.openio.opendb.model.key.KeyType;
import net.openio.opendb.model.value.Value;
import net.openio.opendb.model.value.ValueType;
import net.openio.opendb.storage.metadata.Levels;
import net.openio.opendb.storage.sstable.SSTableIterator;
import net.openio.opendb.storage.sstable.SSTableStorage;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;


public class ColumnFamily {

  private long columnFamilyId;

  private String name;

  private KeyType keyType;

  private ValueType valueType;

  private Levels levels;

  private volatile List<MemTable> memTables;

  private volatile List<MemTable> immMemTable;

  private volatile boolean use;

  private int storageBlockSize;

  private long creatTime;

  private SSTableStorage storage;

  private BufferCache bufferCache;

  private AtomicInteger size = new AtomicInteger(0);

  private ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

  private SSTableIterator ssTableIterator;

  public Value getValue(Key key, Comparator<Key> comparator) {
    readWriteLock.readLock().lock();
    for (MemTable memTable : memTables) {
      Value value = memTable.get(key, comparator);
      if (value != null) {
        readWriteLock.readLock().unlock();
        return value;
      }
    }
    readWriteLock.readLock().unlock();
    synchronized (immMemTable) {
      for (MemTable memTable : immMemTable) {
        Value value = memTable.get(key, comparator);
        if (value != null) {
          return value;
        }
      }
    }
    Levels levels = null;
    synchronized (this.levels) {
      levels = this.levels.copy();
    }
    try {
      return ssTableIterator.getValue(levels, key, comparator);
    } catch (Exception e) {
      throw new RuntimeException("can not scan sstable");
    }


  }

  public SequenceNumber getMinSequenceNumber() {
    PriorityQueue<SequenceNumber> sequenceNumbers = new PriorityQueue<>();
    readWriteLock.readLock().lock();
    for (MemTable memTable : memTables) {
      sequenceNumbers.add(memTable.getMin());
    }
    readWriteLock.readLock().unlock();
    synchronized (immMemTable) {
      for (MemTable memTable : immMemTable) {
        sequenceNumbers.add(memTable.getMin());
      }
    }
    if (sequenceNumbers.isEmpty()) {
      return new SequenceNumber(Long.MAX_VALUE);
    }
    return sequenceNumbers.poll();
  }

  public void addMemTable() {
    readWriteLock.writeLock().lock();
    memTables.add(new MemTable(new SkipListRep(), new BloomFilter()));
    readWriteLock.writeLock().unlock();
  }

  public MemTable getMemTableFlush() {
    readWriteLock.writeLock().lock();
    MemTable memTable = memTables.remove(0);
    readWriteLock.writeLock().unlock();
    size.set(0);
    return memTable;
  }

  public Levels getCopyLevels() {
    readWriteLock.readLock().lock();
    Levels levels = this.levels.copy();
    readWriteLock.readLock().unlock();
    return levels;
  }

  public void add(Key key, Value value) {
    KeyValueEntry keyValueEntry = new KeyValueEntry(key, value);
    int s = keyType.getByteSize(key) + valueType.getByteSize(value);
    size.addAndGet(s);
    while (true) {
      readWriteLock.readLock().lock();
      for (MemTable memTable : memTables) {

        if (!memTable.isCanWrite()) {
          continue;
        }
        if (memTable.size.get() > 1 << 20) {
          continue;
        }
        if (!memTable.increaseCount()) {
          continue;
        }
        memTable.put(keyValueEntry, s);
        memTable.declineCount();
        readWriteLock.readLock().unlock();
        return;
      }
      readWriteLock.readLock().unlock();
      addMemTable();
    }

  }


  public ColumnFamily() {
    memTables = new ArrayList<>(8);
    immMemTable = new ArrayList<>(8);
    for (int i = 0; i < 1; i++) {
      memTables.add(new MemTable(new SkipListRep(), new BloomFilter()));
    }
    levels = new Levels();
  }

  public long getColumnFamilyId() {
    return columnFamilyId;
  }

  public void setColumnFamilyId(long columnFamilyId) {
    this.columnFamilyId = columnFamilyId;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public KeyType getKeyType() {
    return keyType;
  }

  public void setKeyType(KeyType keyType) {
    this.keyType = keyType;
  }

  public ValueType getValueType() {
    return valueType;
  }

  public void setValueType(ValueType valueType) {
    this.valueType = valueType;
  }

  public Levels getLevels() {
    return levels;
  }

  public void setLevels(Levels levels) {
    this.levels = levels;
  }

  public List<MemTable> getMemTables() {
    return memTables;
  }

  public void setMemTables(List<MemTable> memTables) {
    this.memTables = memTables;
  }

  public List<MemTable> getImmMemTable() {
    return immMemTable;
  }

  public void setImmMemTable(List<MemTable> immMemTable) {
    this.immMemTable = immMemTable;
  }

  public boolean isUse() {
    return use;
  }

  public void setUse(boolean use) {
    this.use = use;
  }

  public int getStorageBlockSize() {
    return storageBlockSize;
  }

  public void setStorageBlockSize(int storageBlockSize) {
    this.storageBlockSize = storageBlockSize;
  }

  public long getCreatTime() {
    return creatTime;
  }

  public void setCreatTime(long creatTime) {
    this.creatTime = creatTime;
  }

  public AtomicInteger getSize() {
    return size;
  }

  public SSTableStorage getStorage() {
    return storage;
  }

  public void setStorage(SSTableStorage storage) {
    this.storage = storage;
  }

  public BufferCache getBufferCache() {
    return bufferCache;
  }

  public void setBufferCache(BufferCache bufferCache) {
    this.bufferCache = bufferCache;
  }

  public void setSsTableIterator(SSTableIterator ssTableIterator) {
    this.ssTableIterator = ssTableIterator;
  }
}
