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
package net.openio.opendb.storage.wal;


import java.util.LinkedList;
import java.util.List;


public class LogFileHead {

  private int length;

  private long createTime;

  private List<Integer> blockEndSeek;

  public void add(Integer i) {
    blockEndSeek.add(i);
  }

  public void add(int i, int pageSize) {
    if (blockEndSeek.size() <= 1) {
      blockEndSeek.add(i);
      return;
    }
    int l = blockEndSeek.size();
    int f = blockEndSeek.get(l - 1);
    int e = blockEndSeek.get(l - 2);
    if (f - e < pageSize) {
      blockEndSeek.set(l - 1, f + i);
    } else {
      blockEndSeek.add(i);
    }

  }

  public LogFileHead() {
    blockEndSeek = new LinkedList<>();
  }

  public int getLength() {
    return length;
  }

  public void setLength(int length) {
    this.length = length;
  }

  public long getCreateTime() {
    return createTime;
  }

  public void setCreateTime(long createTime) {
    this.createTime = createTime;
  }

  public List<Integer> getBlockEndSeek() {
    return blockEndSeek;
  }

  public void setBlockEndSeek(List<Integer> blockEndSeek) {
    this.blockEndSeek = blockEndSeek;
  }
}
