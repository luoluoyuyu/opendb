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
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

public class Level {

  private int level;

  private long totalSize;

  private List<SSTable> ssTables;

  public void clear() {
    ssTables.clear();
    totalSize = 0;
  }

  public void clear(int fIndex, int end) {
    List<SSTable> s = ssTables.subList(fIndex, end);
    for (SSTable ssTable : s) {
      totalSize -= ssTable.getFileHeadSize() + ssTable.getFileHeadSeek();
    }
    ssTables.removeAll(s);
  }

  public Level(int level, long totalSize, List<SSTable> ssTables) {
    this.level = level;
    this.totalSize = totalSize;
    this.ssTables = ssTables;
  }

  public Level() {
    ssTables = new ArrayList<>();
  }

  public int getLevel() {
    return level;
  }

  public void setLevel(int level) {
    this.level = level;
  }

  public long getTotalSize() {
    return totalSize;
  }

  public void setTotalSize(long totalSize) {
    this.totalSize = totalSize;
  }

  public List<SSTable> getSsTables() {
    return ssTables;
  }

  public void setSsTables(List<SSTable> ssTables) {
    this.ssTables = ssTables;
  }

  public void addSsTables(SSTable ssTables) {
    this.ssTables.add(ssTables);
    totalSize += ssTables.getFileHeadSize() + ssTables.getFileHeadSeek();
    this.ssTables.sort(Comparator.comparing(SSTable::getMinKey));
  }

  public void addSSTables(List<SSTable> ssTables) {
    for (SSTable ssTable : ssTables) {
      addSsTables(ssTable);
    }
    ssTables.sort(Comparator.comparing(SSTable::getMinKey));
  }

  public Level copy() {
    Level level = new Level();
    level.level = this.level;
    level.totalSize = totalSize;
    level.ssTables = new LinkedList<>(ssTables);
    return level;
  }
}
