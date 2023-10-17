package net.openio.opendb.compression;

import net.openio.opendb.db.ColumnFamily;
import net.openio.opendb.db.SnapshotManager;
import net.openio.opendb.storage.metadata.Levels;
import net.openio.opendb.storage.sstable.SSTable;

import java.util.LinkedList;
import java.util.List;

public class CompactionTask implements Runnable {

  private List<ColumnFamily> list;

  private int maxLevel;

  private SnapshotManager snapshotManager;

  private int sizeTieredLevel;

  private int ssTableSize;

  public CompactionTask(List<ColumnFamily> list, int maxLevel,
                        SnapshotManager snapshotManager, int sizeTieredLevel, int ssTableSize) {
    this.list = list;
    this.maxLevel = maxLevel;
    this.snapshotManager = snapshotManager;
    this.sizeTieredLevel = sizeTieredLevel;
    this.ssTableSize = ssTableSize;
  }

  @Override
  public void run() {
    List<ColumnFamily> columnFamilies = null;
    synchronized (list) {
      columnFamilies = new LinkedList<>(list);
    }
    for (ColumnFamily columnFamily : columnFamilies) {

      Levels levels = columnFamily.getLevels();
      synchronized (levels) {
        levels.setBeingCompactedLevel(0);
      }
      int level = 0;
      while (level < maxLevel) {
        if (levels.getLevel(level) == null) {
          break;
        }
        levels.setBeingCompactedLevel(level);
        if (levels.getAllSize() > levels.getLevel0CompactionTrigger() << level) {
          if (level < sizeTieredLevel) {
            try {
              SizeTieredCompaction compaction = new SizeTieredCompaction(columnFamily.getBufferCache(),
                columnFamily.getStorage(), ssTableSize);
              List<SSTable> list = compaction.compaction(levels.getLevel(level).getSsTables(),
                snapshotManager.getMinSnapshot());
              levels.getLevels().get(level + 1).addSSTables(list);
            } catch (Exception e) {
              e.printStackTrace();
              throw new RuntimeException("compaction is wrong");
            }

          } else {
            try {
              LeveledCompaction leveledCompaction = new LeveledCompaction(columnFamily.getBufferCache(),
                columnFamily.getStorage(), ssTableSize);
              List<SSTable> list = leveledCompaction.compaction(levels.getLevel(level).getSsTables(),
                snapshotManager.getMinSnapshot());
              levels.getLevels().get(level + 1).addSSTables(list);
            } catch (Exception e) {
              e.printStackTrace();
              throw new RuntimeException("compaction is wrong");
            }
          }
        }
        level++;
      }

      levels.setBeingCompactedLevel(-1);
    }
  }
}
