package net.openio.opendb.compression;

import net.openio.opendb.mem.BufferCache;
import net.openio.opendb.model.SequenceNumber;
import net.openio.opendb.storage.sstable.IteratorData;
import net.openio.opendb.storage.sstable.SSTable;
import net.openio.opendb.storage.sstable.SSTableStorage;

import java.io.IOException;
import java.util.List;
import java.util.PriorityQueue;

public class Compaction {

  final BufferCache bufferCache;

  final SSTableStorage storage;

  final int size;

  protected List<SSTable> compaction(List<SSTable> ssTables, SequenceNumber sequenceNumber) throws IOException {
    CompactionIterator compactionIterator = new CompactionIterator(ssTables, storage, bufferCache, sequenceNumber);
    return storage.compaction(compactionIterator, ssTables.get(0).getKeyType(), ssTables.get(0).getValueType(), size);

  }

  public Compaction(BufferCache bufferCache, SSTableStorage storage, int ssTableSize) {
    this.bufferCache = bufferCache;
    this.storage = storage;
    size = ssTableSize;
  }

  private PriorityQueue<IteratorData> init(List<SSTable> ssTables) throws IOException {
    PriorityQueue<IteratorData> priorityQueue = new PriorityQueue<>();
    for (SSTable ssTable : ssTables) {
      priorityQueue.add(IteratorData.init(ssTable, storage, bufferCache));
    }
    return priorityQueue;
  }


}
