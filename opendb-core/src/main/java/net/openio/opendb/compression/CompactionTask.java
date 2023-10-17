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
