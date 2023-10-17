package net.openio.opendb.compression;

import net.openio.opendb.db.KeyValueEntry;
import net.openio.opendb.mem.BufferCache;
import net.openio.opendb.model.SequenceNumber;
import net.openio.opendb.model.key.Key;
import net.openio.opendb.storage.sstable.IteratorData;
import net.openio.opendb.storage.sstable.SSTable;
import net.openio.opendb.storage.sstable.SSTableStorage;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.PriorityQueue;

public class CompactionIterator implements Iterator<KeyValueEntry> {

  private final PriorityQueue<IteratorData> queue;
  private final SSTableStorage storage;
  private final BufferCache bufferCache;
  private final SequenceNumber minNumber;
  private Key key = null;


  @Override
  public boolean hasNext() {

    while (key != null) {
      if (queue.isEmpty()) {
        return false;
      }
      IteratorData iteratorData = queue.peek();
      if (iteratorData.getKey().compareTo(key) != 0) {
        return true;
      }
      if (iteratorData.getKey().getSequenceNumber().compareTo(minNumber) >= 0) {
        return true;
      }
      if (key.getSequenceNumber().compareTo(minNumber) > 0) {
        return true;
      }
      iteratorData = queue.poll();
      try {
        if (iteratorData.update(storage, bufferCache)) {
          queue.add(iteratorData);
        }
      } catch (Exception e) {
        e.printStackTrace();
        throw new RuntimeException("can not scan the SSTable ");
      }

    }
    return true;
  }

  @Override
  public KeyValueEntry next() {
    IteratorData iteratorData = queue.poll();
    KeyValueEntry keyValueEntry = new KeyValueEntry(iteratorData.getKey(), iteratorData.getValue());
    key = iteratorData.getKey();
    try {
      if (iteratorData.update(storage, bufferCache)) {
        queue.add(iteratorData);
      }
    } catch (Exception e) {
      e.printStackTrace();
      throw new RuntimeException("can not scan the SSTable ");
    }
    return keyValueEntry;
  }

  public CompactionIterator(List<SSTable> ssTables, SSTableStorage storage,
                            BufferCache bufferCache, SequenceNumber minNumber) throws IOException {
    PriorityQueue<IteratorData> priorityQueue = new PriorityQueue<>();
    for (SSTable ssTable : ssTables) {
      priorityQueue.add(IteratorData.init(ssTable, storage, bufferCache));
    }
    this.storage = storage;
    this.bufferCache = bufferCache;
    this.queue = priorityQueue;
    this.minNumber = minNumber;
  }
}
