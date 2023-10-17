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
import java.util.concurrent.ThreadLocalRandom;

public class LeveledCompaction extends Compaction {
  public LeveledCompaction(BufferCache bufferCache, SSTableStorage storage, int ssTableSize) {
    super(bufferCache, storage, ssTableSize);
  }

  public List<SSTable> compaction(Levels levels, int level, SequenceNumber sequenceNumber, int ssTableSize) throws IOException {
    for (Level list : levels.getLevels()) {
      if (list.getLevel() == level) {
        int a = ThreadLocalRandom.current().nextInt(0,
          list.getSsTables().size() - 1);
        int b = ThreadLocalRandom.current().nextInt(a + 1,
          list.getSsTables().size());
        List<SSTable> ssTables = compaction(list.getSsTables().subList(a, b), sequenceNumber);
        list.clear(a, b);
        return ssTables;
      }
    }
    return new LinkedList<>();
  }
}
