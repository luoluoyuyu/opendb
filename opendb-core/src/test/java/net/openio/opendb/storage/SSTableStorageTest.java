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
package net.openio.opendb.storage;

import net.openio.opendb.mem.BloomFilter;
import net.openio.opendb.mem.KeyValueGenerator;
import net.openio.opendb.mem.MemTable;
import net.openio.opendb.mem.SkipListRep;
import net.openio.opendb.db.KeyValueEntry;
import net.openio.opendb.storage.sstable.FileHeadBlock;
import net.openio.opendb.storage.sstable.MetaBlock;
import net.openio.opendb.storage.sstable.SSTable;
import net.openio.opendb.storage.sstable.IndexData;
import net.openio.opendb.storage.sstable.DataBlock;
import net.openio.opendb.storage.sstable.MetaData;
import net.openio.opendb.storage.sstable.IndexBlock;
import net.openio.opendb.model.key.KeyType;
import net.openio.opendb.model.value.ValueType;
import net.openio.opendb.storage.sstable.SSTableStorage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Iterator;

public class SSTableStorageTest {
//  private final static MemTable memTable = new MemTable(new SkipListRep<>(), new BloomFilter());
//  private final static SSTableStorage storage = new SSTableStorage(1 << 15, 1 << 17, "src/test/resources/");
//  private final static int count = 1 << 10;
//
//  static {
//    for (int i = 0; i < count; i++) {
//      KeyValueEntry keyValueEntry = KeyValueGenerator.generateRandomIntKeyValueEntry();
//      memTable.put(keyValueEntry);
//    }
//  }
//
//  @Test
//  public void test() throws IOException {
//    SSTable ssTable = storage.flush(memTable, KeyType.intKey, ValueType.intValue);
//    FileHeadBlock fileHeadBlock = null;
//    fileHeadBlock = storage.getFileHead(ssTable);
//    BloomFilter bloomFilter = storage.getBloomFilter(ssTable, fileHeadBlock.getBloomOfferSeek(), fileHeadBlock.getBloomOfferSize());
//    Assertions.assertArrayEquals(bloomFilter.getData(), memTable.getBloomFilter().getData());
//    MetaBlock metaBlock = storage.getMetaBlock(ssTable, fileHeadBlock.getMetaOfferSeek(), fileHeadBlock.getMetaOfferSize());
//    Iterator<KeyValueEntry> iterator = memTable.getMemTableRep().iterator();
//    DataBlock dataBlock = null;
//    for (MetaData metaData : metaBlock.getMetaData()) {
//      IndexBlock indexBlock = storage.getIndexBlock(ssTable, metaData.getOffset(), metaData.getSize());
//      for (IndexData indexData : indexBlock.getDataList()) {
//        if (indexData.getFirstIndex() == 0) {
//          dataBlock = storage.getDataBlock(ssTable, indexData.getOffset(), indexData.getDataBlockSize());
//        }
//
//        for (int i = 0; i < indexData.getNum(); i++) {
//          KeyValueEntry keyValueEntry = iterator.next();
//          Assertions.assertEquals(keyValueEntry.getKey(), indexData.get(i));
//          Assertions.assertEquals(keyValueEntry.getValue().getValue(),
//            dataBlock.getValues().get(indexData.getFirstIndex() + i).getValue());
//        }
//      }
//    }
//
//    Assertions.assertFalse(iterator.hasNext());
//  }
}
