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

import net.openio.opendb.db.KeyValueEntry;
import net.openio.opendb.model.key.KeyType;
import net.openio.opendb.model.value.ValueType;
import net.openio.opendb.storage.sstable.*;
import net.openio.opendb.tool.codec.sstable.DataBlockProtoCodec;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;

public class LRUBufferCacheTest {

  private final static MemTable memTable = new MemTable(new SkipListRep<>(), new BloomFilter());
  private final static SSTableStorage storage = new SSTableStorage(1 << 15, 1 << 17, "src/test/resources/");
  private final static int count = 1 << 10;
  private final static List<DataBlock> blocks = new ArrayList();

  static {
    for (int i = 0; i < count; i++) {
      KeyValueEntry keyValueEntry = KeyValueGenerator.generateRandomIntKeyValueEntry();
      memTable.put(keyValueEntry,0);
    }
  }

  @Test
  public void test() throws IOException {
    int s = 0;
    SSTable ssTable = storage.flush(memTable, KeyType.intKey, ValueType.intValue);
    FileHeadBlock fileHeadBlock = null;
    fileHeadBlock = storage.getFileHead(ssTable);
    MetaBlock metaBlock = storage.getMetaBlock(ssTable, fileHeadBlock.getMetaOfferSeek(), fileHeadBlock.getMetaOfferSize());
    DataBlock dataBlock = null;
    for (MetaData metaData : metaBlock.getMetaData()) {
      IndexBlock indexBlock = storage.getIndexBlock(ssTable, metaData.getOffset(), metaData.getSize());
      for (IndexData indexData : indexBlock.getDataList()) {
        if (indexData.getFirstIndex() == 0) {
          dataBlock = storage.getDataBlock(ssTable, indexData.getOffset(), indexData.getDataBlockSize());
          blocks.add(dataBlock);
          s += DataBlockProtoCodec.getByteSize(dataBlock);
        }

      }
    }

    LRUBufferCache<Block> cache =
      new LRUBufferCache<>(1000, 60, 60000, blocks.size() / 2);


    ExecutorService executorService = Executors.newFixedThreadPool(10);
    CountDownLatch startSignal = new CountDownLatch(1);
    CountDownLatch doneSignal = new CountDownLatch(10);

    // Spawn 10 threads to access the cache concurrently
    for (int i = 0; i < 10; i++) {
      executorService.execute(() -> {
        try {
          startSignal.await();
          for (int j = 0; j < 10000; j++) {
            int randomBlockIndex = ThreadLocalRandom.current().nextInt(blocks.size());
            DataBlock block = blocks.get(randomBlockIndex);
            cache.add(block, DataBlockProtoCodec.getByteSize(block), "ssTable");
            cache.getData("ssTable", block.getId());
          }
          doneSignal.countDown();
        } catch (InterruptedException e) {
          Thread.currentThread().interrupt();
        }
      });
    }

    startSignal.countDown();
    try {
      doneSignal.await();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    executorService.shutdown();
    Assertions.assertTrue(cache.getCurrentSize() <= s);

  }
}
