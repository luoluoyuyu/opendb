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
package net.openio.opendb.db;


import net.openio.opendb.mem.MemTable;
import net.openio.opendb.model.key.KeyType;
import net.openio.opendb.model.value.ValueType;
import net.openio.opendb.storage.metadata.Levels;

import java.util.ArrayList;
import java.util.List;


public class ColumnFamily {

  private long columnFamilyId;

  private String name;

  private KeyType keyType;

  private ValueType valueType;

  private Levels levels;

  private volatile List<MemTable> memTables;

  private volatile List<MemTable> immMemTable;

  private volatile boolean use;

  private int storageBlockSize;

  private long creatTime;

  public ColumnFamily() {
    memTables = new ArrayList<>();
    immMemTable = new ArrayList<>();
  }

  public long getColumnFamilyId() {
    return columnFamilyId;
  }

  public void setColumnFamilyId(long columnFamilyId) {
    this.columnFamilyId = columnFamilyId;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
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

  public Levels getLevels() {
    return levels;
  }

  public void setLevels(Levels levels) {
    this.levels = levels;
  }

  public List<MemTable> getMemTables() {
    return memTables;
  }

  public void setMemTables(List<MemTable> memTables) {
    this.memTables = memTables;
  }

  public List<MemTable> getImmMemTable() {
    return immMemTable;
  }

  public void setImmMemTable(List<MemTable> immMemTable) {
    this.immMemTable = immMemTable;
  }

  public boolean isUse() {
    return use;
  }

  public void setUse(boolean use) {
    this.use = use;
  }

  public int getStorageBlockSize() {
    return storageBlockSize;
  }

  public void setStorageBlockSize(int storageBlockSize) {
    this.storageBlockSize = storageBlockSize;
  }

  public long getCreatTime() {
    return creatTime;
  }

  public void setCreatTime(long creatTime) {
    this.creatTime = creatTime;
  }
}
