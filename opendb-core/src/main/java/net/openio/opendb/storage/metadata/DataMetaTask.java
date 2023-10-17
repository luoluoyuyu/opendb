package net.openio.opendb.storage.metadata;

import net.openio.opendb.db.ColumnFamily;
import net.openio.opendb.db.ColumnFamilyManager;
import net.openio.opendb.db.SnapshotManager;
import net.openio.opendb.db.WriteBatch;

public class DataMetaTask implements Runnable {

  MetaMetaStorage metaMetaStorage;

  SnapshotManager snapshotManager;

  ColumnFamilyManager columnFamilyManager;

  WriteBatch writeBatch;

  String dir;

  public DataMetaTask(MetaMetaStorage metaMetaStorage,
                      SnapshotManager snapshotManager,
                      ColumnFamilyManager columnFamilyManager,
                      WriteBatch writeBatch, String dir) {
    this.metaMetaStorage = metaMetaStorage;
    this.snapshotManager = snapshotManager;
    this.columnFamilyManager = columnFamilyManager;
    this.writeBatch = writeBatch;
    this.dir = dir;
  }


  @Override
  public void run() {
    for (ColumnFamily columnFamily : columnFamilyManager.getColumnFamilies()) {
      synchronized (columnFamily.getLevels()) {
        Levels levels = columnFamily.getLevels();
        int size = levels.getWaitToMerge().size();
        if (levels.getBeingCompactedLevel() == -1 && size > 0) {
          if (levels.getLevel(0) == null) {
            Level level = new Level();
            level.addSSTables(levels.getWaitToMerge());
            level.setLevel(0);
            levels.addLevels(new Level());
          } else {
            levels.getLevel(0).getSsTables().addAll(levels.getWaitToMerge());
          }
          levels.getWaitToMerge().clear();
          synchronized (columnFamily.getImmMemTable()) {
            columnFamily.getImmMemTable().subList(0, size).clear();
          }
        }
      }
    }
    DataMeta dataMeta = new DataMeta();
    dataMeta.setMaxNumber(writeBatch.getMaxSequenceNumber());
    dataMeta.setUnPersistedSeqNumberLow(columnFamilyManager.getMinSequenceNumber());
    dataMeta.setWalLog(writeBatch.getFile());
    dataMeta.setColumnFamilies(columnFamilyManager.getColumnFamilies());
    metaMetaStorage.flush(dir, dataMeta);


  }
}
