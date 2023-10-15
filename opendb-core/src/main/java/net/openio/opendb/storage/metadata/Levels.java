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
package net.openio.opendb.storage.metadata;


import net.openio.opendb.storage.sstable.SSTable;

import java.util.ArrayList;
import java.util.List;

public class Levels {

  private List<Level> levels;

  private int level0CompactionNumTrigger;

  private int level0CompactionTrigger;

  private int allSize;

  private int beingCompactedLevel;

  private List<SSTable> waitToMerge;

  public Levels() {
    levels = new ArrayList<>();
    waitToMerge = new ArrayList<>();
  }

  public void addList(int level, List<SSTable> ssTables) {
    for (Level level1 : levels) {
      if (level1.getLevel() == level) {
        level1.addSSTables(ssTables);
        return;
      }
    }
    updateToil();
  }

  public Level getLevel(int level) {
    for (Level level1 : levels) {
      if (level1.getLevel() == level) {
        return level1;
      }
    }
    return null;
  }

  public void updateToil() {
    int i = 0;
    for (Level level1 : levels) {
      i += level1.getTotalSize();
    }
    allSize = i;
  }


  public List<Level> getLevels() {
    return levels;
  }

  public void setLevels(List<Level> levels) {
    this.levels = levels;
  }

  public void addLevels(Level level) {
    this.levels.add(level);
  }

  public int getLevel0CompactionNumTrigger() {
    return level0CompactionNumTrigger;
  }

  public void setLevel0CompactionNumTrigger(int level0CompactionNumTrigger) {
    this.level0CompactionNumTrigger = level0CompactionNumTrigger;
  }

  public int getLevel0CompactionTrigger() {
    return level0CompactionTrigger;
  }

  public void setLevel0CompactionTrigger(int level0CompactionTrigger) {
    this.level0CompactionTrigger = level0CompactionTrigger;
  }

  public int getAllSize() {
    return allSize;
  }

  public void setAllSize(int allSize) {
    this.allSize = allSize;
  }

  public int getBeingCompactedLevel() {
    return beingCompactedLevel;
  }

  public void setBeingCompactedLevel(int beingCompactedLevel) {
    this.beingCompactedLevel = beingCompactedLevel;
  }

  public List<SSTable> getWaitToMerge() {
    return waitToMerge;
  }

  public void setWaitToMerge(List<SSTable> waitToMerge) {
    this.waitToMerge = waitToMerge;
  }

  public void addWaitToMerge(SSTable waitToMerge) {
    this.waitToMerge.add(waitToMerge);
  }

}
