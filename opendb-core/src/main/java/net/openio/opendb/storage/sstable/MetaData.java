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
package net.openio.opendb.storage.sstable;


import net.openio.opendb.model.key.Key;


public class MetaData {

  private long indexId;

  private Key key;

  private int offset;

  private int size;

  private SSTable ssTable;

  public MetaData(long indexId, Key key, int offset, int size, SSTable ssTable) {
    this.indexId = indexId;
    this.key = key;
    this.offset = offset;
    this.size = size;
    this.ssTable = ssTable;
  }

  public MetaData() {
  }

  public long getIndexId() {
    return indexId;
  }

  public void setIndexId(long indexId) {
    this.indexId = indexId;
  }

  public Key getKey() {
    return key;
  }

  public void setKey(Key key) {
    this.key = key;
  }

  public int getOffset() {
    return offset;
  }

  public void setOffset(int offset) {
    this.offset = offset;
  }

  public int getSize() {
    return size;
  }

  public void setSize(int size) {
    this.size = size;
  }

  public SSTable getSsTable() {
    return ssTable;
  }

  public void setSsTable(SSTable ssTable) {
    this.ssTable = ssTable;
  }
}
