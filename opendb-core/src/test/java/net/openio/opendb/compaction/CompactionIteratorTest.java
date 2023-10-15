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
package net.openio.opendb.compaction;

import net.openio.opendb.compression.CompactionIterator;
import net.openio.opendb.db.KeyValueEntry;
import net.openio.opendb.mem.BloomFilter;
import net.openio.opendb.mem.BufferCache;
import net.openio.opendb.mem.KeyValueGenerator;
import net.openio.opendb.mem.LRUBufferCache;
import net.openio.opendb.mem.MemTable;
import net.openio.opendb.mem.SkipListRep;
import net.openio.opendb.model.SequenceNumber;
import net.openio.opendb.model.key.Key;
import net.openio.opendb.model.key.KeyType;
import net.openio.opendb.model.value.ValueType;
import net.openio.opendb.storage.sstable.SSTable;
import net.openio.opendb.storage.sstable.SSTableStorage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class CompactionIteratorTest {
//  private final static MemTable memTable = new MemTable(new SkipListRep<>(), new BloomFilter());
//  private final static SSTableStorage storage = new SSTableStorage(1 << 15, 1 << 17, "src/test/resources/");
//  private final static MemTable mem = new MemTable(new SkipListRep<>(), new BloomFilter());
//  private final static List<KeyValueEntry> keyValue = new LinkedList<>();
//  private final static int count = 1 << 10;
//
//  static {
//    for (int i = 0; i < count; i++) {
//      KeyValueEntry keyValueEntry = KeyValueGenerator.generateRandomIntKeyValueEntry();
//      keyValue.add(keyValueEntry);
//      memTable.put(keyValueEntry);
//
//      KeyValueEntry keyValueEntry1 = KeyValueGenerator.generateRandomIntKeyValueEntry();
//      keyValue.add(keyValueEntry1);
//      mem.put(keyValueEntry1);
//    }
//
//  }
//
//  @Test
//  public void test() throws IOException {
//    keyValue.sort((a, b) -> {
//        Key k = ((KeyValueEntry) a).getKey();
//        Key key = ((KeyValueEntry) b).getKey();
//        int d = k.compareTo(key);
//        if (d == 0) {
//          return k.getSequenceNumber().compareTo(key.getSequenceNumber());
//        }
//        return d;
//      }
//    );
//    List<SSTable> ssTables = new LinkedList<>();
//    SSTable ssTable = storage.flush(memTable, KeyType.intKey, ValueType.intValue);
//    ssTables.add(ssTable);
//    ssTable = storage.flush(mem, KeyType.intKey, ValueType.intValue);
//    ssTables.add(ssTable);
//
//    CompactionIterator compactionIterator = new CompactionIterator(ssTables, storage, new BufferCache(
//      new LRUBufferCache<>(1000, 60, 60000, 1 << 19),
//      new LRUBufferCache<>(1000, 60, 60000, 1 << 19),
//      new LRUBufferCache<>(1000, 60, 60000, 1 << 19)
//    ),
//      new SequenceNumber(0L));
//    for (KeyValueEntry keyValueEntry : keyValue) {
//      Assertions.assertEquals(keyValueEntry.getKey().getKey(), compactionIterator.next().getKey().getKey());
//    }
//
//    Assertions.assertFalse(compactionIterator.hasNext());
//
//  }
}

