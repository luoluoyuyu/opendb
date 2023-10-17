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
import net.openio.opendb.model.key.KeyType;
import net.openio.opendb.model.value.ValueType;



public class SSTable {

  private String fileName;

  private long createTime;

  private int size;

  private KeyType keyType;

  private ValueType valueType;

  private Key minKey;

  private Key maxValue;

  private int fileHeadSeek;

  private int fileHeadSize;


  public String getFileName() {
    return fileName;
  }

  public void setFileName(String fileName) {
    this.fileName = fileName;
  }

  public long getCreateTime() {
    return createTime;
  }

  public void setCreateTime(long createTime) {
    this.createTime = createTime;
  }

  public int getSize() {
    return size;
  }

  public void setSize(int size) {
    this.size = size;
  }

  public KeyType getKeyType() {
    return keyType;
  }

  public void setKeyType(KeyType keyType) {
    this.keyType = keyType;
  }

  public ValueType getValueType() {
    return valueType;
  }

  public void setValueType(ValueType valueType) {
    this.valueType = valueType;
  }

  public Key getMinKey() {
    return minKey;
  }

  public void setMinKey(Key minKey) {
    this.minKey = minKey;
  }

  public Key getMaxValue() {
    return maxValue;
  }

  public void setMaxValue(Key maxValue) {
    this.maxValue = maxValue;
  }

  public int getFileHeadSeek() {
    return fileHeadSeek;
  }

  public void setFileHeadSeek(int fileHeadSeek) {
    this.fileHeadSeek = fileHeadSeek;
  }

  public int getFileHeadSize() {
    return fileHeadSize;
  }

  public void setFileHeadSize(int fileHeadSize) {
    this.fileHeadSize = fileHeadSize;
  }
}
