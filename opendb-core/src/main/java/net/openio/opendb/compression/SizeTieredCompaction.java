package net.openio.opendb.compression;

import net.openio.opendb.mem.BufferCache;
import net.openio.opendb.model.SequenceNumber;
import net.openio.opendb.storage.metadata.Level;
import net.openio.opendb.storage.metadata.Levels;
import net.openio.opendb.storage.sstable.SSTable;
import net.openio.opendb.storage.sstable.SSTableStorage;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class SizeTieredCompaction extends Compaction {


  public SizeTieredCompaction(BufferCache bufferCache, SSTableStorage storage, int ssTableSize) {
    super(bufferCache, storage, ssTableSize);
  }

  public List<SSTable> compaction(Levels levels, int level, SequenceNumber sequenceNumber, int ssTableSize) throws IOException {
    for (Level list : levels.getLevels()) {
      if (list.getLevel() == level) {
        list.clear();
        return compaction(list.getSsTables(), sequenceNumber);
      }
    }
    return new LinkedList<>();
  }
}
