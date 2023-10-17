package net.openio.opendb.db;

import net.openio.opendb.compression.CompactionTask;
import net.openio.opendb.mem.BufferCache;
import net.openio.opendb.mem.FlushTask;
import net.openio.opendb.model.ColumnFamilyDescriptor;
import net.openio.opendb.model.Options;
import net.openio.opendb.model.SequenceNumber;
import net.openio.opendb.model.key.Key;
import net.openio.opendb.model.value.Value;
import net.openio.opendb.storage.sstable.SSTableIterator;
import net.openio.opendb.storage.sstable.SSTableStorage;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class ColumnFamilyManager {

  final String dir;

  final int storageMemArenaSize;

  List<ColumnFamily> columnFamilies;

  ConcurrentHashMap<Long, ColumnFamily> familyConcurrentHashMap;

  private final ScheduledExecutorService compactionExecutor;

  private final ScheduledExecutorService flushExecutor;

  BufferCache bufferCache;

  public void add(Key key, Value value, long cId) {
    familyConcurrentHashMap.get(cId).add(key, value);

  }

  public Value get(long cId, Key key, Comparator<Key> comparator) {
    return familyConcurrentHashMap.get(cId).getValue(key, comparator);
  }

  public List<ColumnFamily> getColumnFamilies() {
    return columnFamilies;
  }

  public List<ColumnFamilyDescriptor> getAll() {
    List<ColumnFamilyDescriptor> columnFamilyDescriptors = new LinkedList<>();
    for (ColumnFamily columnFamily : columnFamilies) {
      ColumnFamilyDescriptor columnFamilyDescriptor = new ColumnFamilyDescriptor(columnFamily);
      columnFamilyDescriptors.add(columnFamilyDescriptor);
    }
    return columnFamilyDescriptors;
  }

  public ColumnFamilyHandle getColumnFamilyHandle(String name) {
    for (ColumnFamily columnFamily : columnFamilies) {
      if (columnFamily.getName().equals(name)) {
        return new ColumnFamilyHandle(columnFamily);
      }
    }
    return null;
  }

  public boolean create(ColumnFamilyDescriptor columnFamilyDescriptor) {
    for (ColumnFamily columnFamily : columnFamilies) {
      if (columnFamily.getName().equals(columnFamilyDescriptor.name)) {
        return false;
      }
    }
    ColumnFamily columnFamily = new ColumnFamily();
    columnFamily.setColumnFamilyId(System.currentTimeMillis());
    columnFamily.setStorageBlockSize(columnFamilyDescriptor.blockSize);
    columnFamily.setName(columnFamilyDescriptor.name);
    columnFamily.setCreatTime(System.currentTimeMillis());
    columnFamily.setKeyType(columnFamilyDescriptor.keyType);
    columnFamily.setValueType(columnFamilyDescriptor.valueType);

    familyConcurrentHashMap.put(columnFamily.getColumnFamilyId(), columnFamily);
    columnFamily.setBufferCache(bufferCache);
    columnFamily.setStorage(new SSTableStorage(columnFamily.getStorageBlockSize(), storageMemArenaSize,
      dir + "/" + columnFamily.getColumnFamilyId() + "/"));
    columnFamily.setSsTableIterator(new SSTableIterator(columnFamily.getStorage(), bufferCache));

    columnFamilies.add(columnFamily);
    return true;
  }


  public ColumnFamilyManager(List<ColumnFamily> columnFamilies,
                             Options options, String fileDir, SnapshotManager snapshotManager) {
    storageMemArenaSize = options.storageMemArenaSize;
    dir = fileDir;
    familyConcurrentHashMap = new ConcurrentHashMap<>();
    bufferCache = new BufferCache(options.getLruExpireTime(),
      options.getLruScanTime(), options.getLruOldBlocksTime(), options.getLruMaxCapacity());
    this.columnFamilies = columnFamilies;
    for (ColumnFamily columnFamily : columnFamilies) {
      familyConcurrentHashMap.put(columnFamily.getColumnFamilyId(), columnFamily);
      columnFamily.setBufferCache(bufferCache);
      columnFamily.setStorage(new SSTableStorage(columnFamily.getStorageBlockSize(), options.storageMemArenaSize,
        fileDir + "/" + columnFamily.getColumnFamilyId() + "/"));
      columnFamily.setSsTableIterator(new SSTableIterator(columnFamily.getStorage(), bufferCache));
    }

    new CompactionTask(columnFamilies, options.maxLevel, snapshotManager, options.sizeTieredLevel, options.ssTableSize);
    compactionExecutor = Executors.newSingleThreadScheduledExecutor();
    compactionExecutor.scheduleAtFixedRate(
      new CompactionTask(columnFamilies, options.maxLevel, snapshotManager, options.sizeTieredLevel, options.ssTableSize)
      , options.compactionTime, options.compactionTime, TimeUnit.SECONDS);

    FlushTask flushTask = new FlushTask(columnFamilies, options.allMemTableMaxSize);
    flushExecutor = Executors.newSingleThreadScheduledExecutor();
    flushExecutor.scheduleAtFixedRate(flushTask, options.flushTime, options.flushTime, TimeUnit.SECONDS);
  }

  public SequenceNumber getMinSequenceNumber() {
    PriorityQueue<SequenceNumber> sequenceNumbers = new PriorityQueue<>();

    for (ColumnFamily memTable : columnFamilies) {
      sequenceNumbers.add(memTable.getMinSequenceNumber());
    }
    if (sequenceNumbers.isEmpty()) {
      return new SequenceNumber(Long.MAX_VALUE);
    }
    return sequenceNumbers.poll();
  }

  public void close() {
    flushExecutor.shutdown();
    compactionExecutor.shutdown();
    bufferCache.close();
  }

}

