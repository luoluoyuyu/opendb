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
package net.openio.opendb.mem;

import net.openio.opendb.db.ColumnFamily;
import net.openio.opendb.storage.metadata.Levels;
import net.openio.opendb.storage.sstable.SSTable;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class FlushTask implements Runnable {

  private final List<ColumnFamily> list;


  private final int maxSize;

  private final int minSize;

  public FlushTask(List<ColumnFamily> list, int maxSize) {
    this.list = list;
    this.maxSize = maxSize;
    minSize = maxSize / 4;
  }

  @Override
  public void run() {
    List<ColumnFamily> columnFamilies = null;
    synchronized (list) {
      columnFamilies = new LinkedList<>(list);
    }
    columnFamilies.sort((a, b) -> {
      return Integer.compare(b.getSize().get(), a.getSize().get());
    });
    int size = 0;
    for (ColumnFamily columnFamily : columnFamilies) {
      size += columnFamily.getSize().get();
    }
    if (size < this.maxSize) {
      return;
    }

    for (ColumnFamily columnFamily : columnFamilies) {
      if (size < minSize) {
        break;
      }
      size -= columnFamily.getSize().get();
      columnFamily.addMemTable();
      MemTable memTable = columnFamily.getMemTableFlush();
      memTable.flush();
      synchronized (columnFamily.getImmMemTable()) {
        columnFamily.getImmMemTable().add(memTable);
      }
      try {
        SSTable ssTable = columnFamily.getStorage().flush(memTable, columnFamily.getKeyType(), columnFamily.getValueType());
        Levels levels = columnFamily.getLevels();
        synchronized (columnFamily.getLevels()) {
          levels.getWaitToMerge().add(ssTable);
        }
      } catch (IOException e) {
        e.printStackTrace();
        throw new RuntimeException("can not flush SSTable");
      }
    }

    for (ColumnFamily columnFamily : columnFamilies) {
      if (columnFamily.getMemTables().get(0).size.get() > 1 << 20) {
        MemTable memTable = columnFamily.getMemTableFlush();
        memTable.flush();
        synchronized (columnFamily.getImmMemTable()) {
          columnFamily.getImmMemTable().add(memTable);
        }
        try {
          SSTable ssTable = columnFamily.getStorage().flush(memTable, columnFamily.getKeyType(), columnFamily.getValueType());
          Levels levels = columnFamily.getLevels();
          synchronized (columnFamily.getLevels()) {
            levels.getWaitToMerge().add(ssTable);
          }
        } catch (IOException e) {
          e.printStackTrace();
          throw new RuntimeException("can not flush SSTable");
        }
      } else {
        break;
      }

    }
  }
}
