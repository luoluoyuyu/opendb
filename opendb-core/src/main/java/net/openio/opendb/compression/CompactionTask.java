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

import net.openio.opendb.model.SequenceNumber;
import net.openio.opendb.storage.metadata.Levels;
import net.openio.opendb.storage.sstable.SSTable;

import java.util.List;

public class CompactionTask implements Runnable {

  Levels levels;

  int maxLevel;

  SizeTieredCompaction compaction;

  LeveledCompaction leveledCompaction;

  SequenceNumber sequenceNumber;

  int sizeTieredLevel;

  public CompactionTask(Levels levels, int maxLevel, SizeTieredCompaction compaction,
                        LeveledCompaction leveledCompaction,
                        SequenceNumber sequenceNumber, int sizeTieredLevel) {
    this.levels = levels;
    this.maxLevel = maxLevel;
    this.compaction = compaction;
    this.leveledCompaction = leveledCompaction;
    this.sequenceNumber = sequenceNumber;
    this.sizeTieredLevel = sizeTieredLevel;
  }

  @Override
  public void run() {
    levels.setBeingCompactedLevel(0);
    if (!levels.getWaitToMerge().isEmpty()) {
      levels.addList(0, levels.getWaitToMerge());
      levels.getWaitToMerge().clear();
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
            List<SSTable> list = compaction.compaction(levels.getLevel(level).getSsTables(), sequenceNumber);
            levels.getLevels().get(level + 1).addSSTables(list);
          } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("compaction is wrong");
          }

        } else {
          try {
            List<SSTable> list = leveledCompaction.compaction(levels.getLevel(level).getSsTables(), sequenceNumber);
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
