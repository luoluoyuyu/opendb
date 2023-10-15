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

import net.openio.opendb.mem.BufferCache;
import net.openio.opendb.model.key.Key;
import net.openio.opendb.model.value.Value;

import java.io.IOException;

public class IteratorData implements Comparable {

  private SSTable ssTable;

  private FileHeadBlock fileHeadBlock;

  private MetaBlock metaBlock;

  private IndexBlock indexBlock;

  private IndexData indexData;

  private DataBlock dataBlock;

  private int dataBlockIndex;

  private int indexDataIndex;

  private int indexBlockIndex;

  private int metaBlockIndex;


  @Override
  public int compareTo(Object o) {
    Key key = getKey();
    Key k = ((IteratorData) o).getKey();
    int d = key.compareTo(k);
    if (d == 0) {
      return key.getSequenceNumber().compareTo(k.getSequenceNumber());
    }
    return d;
  }

  public static IteratorData init(SSTable ssTable, SSTableStorage storage,
                                  BufferCache bufferCache) throws IOException {
    IteratorData compactionData = new IteratorData();
    FileHeadBlock fileHeadBlock = storage.getFileHead(ssTable);
    compactionData.fileHeadBlock = fileHeadBlock;
    MetaBlock metaBlock = getMetaBlock(ssTable, storage, bufferCache, fileHeadBlock);
    compactionData.metaBlock = metaBlock;

    IndexBlock indexBlock = getIndexBlock(ssTable, storage, bufferCache, metaBlock.get(0));
    compactionData.indexBlock = indexBlock;
    IndexData indexData = indexBlock.get(0);
    compactionData.indexData = indexData;

    compactionData.dataBlock = getDataBlock(ssTable, storage, bufferCache, indexData);
    compactionData.ssTable = ssTable;
    return compactionData;

  }

  public Key getKey() {
    return indexData.get(indexDataIndex);
  }

  public Value getValue() {
    return dataBlock.get(dataBlockIndex);
  }

  public boolean update(SSTableStorage storage, BufferCache bufferCache) throws IOException {
    indexDataIndex++;
    if (indexDataIndex >= indexData.getNum()) {
      if (!updateIndexBlock(storage, bufferCache)) {
        return false;
      }
      indexData = indexBlock.get(indexBlockIndex);
      indexDataIndex = 0;
      if (indexData.getFirstIndex() == 0) {
        dataBlock = getDataBlock(ssTable, storage, bufferCache, indexData);
        dataBlockIndex = -1;
      }
    }
    dataBlockIndex++;
    return true;
  }

  public boolean isEnd() {
    return metaBlockIndex < metaBlock.getNum();
  }


  private boolean updateIndexBlock(SSTableStorage storage, BufferCache bufferCache) throws IOException {
    indexBlockIndex++;
    if (indexBlockIndex >= indexBlock.getNum()) {
      if (!updateMetaBlock()) {
        return false;
      }
      indexBlock = getIndexBlock(ssTable, storage, bufferCache, metaBlock.get(metaBlockIndex));
      indexBlockIndex = 0;
    }
    return true;
  }

  private boolean updateMetaBlock() {
    metaBlockIndex++;
    return metaBlockIndex < metaBlock.getNum();
  }

  private static MetaBlock getMetaBlock(SSTable ssTable, SSTableStorage storage,
                                        BufferCache bufferCache, FileHeadBlock fileHeadBlock) throws IOException {
    MetaBlock metaBlock = bufferCache.getMetaBlock(ssTable.getFileName(), 0);
    if (metaBlock != null) {
      return metaBlock;
    }
    metaBlock = storage.getMetaBlock(ssTable,
      fileHeadBlock.getMetaOfferSeek(), fileHeadBlock.getMetaOfferSize());
    bufferCache.addMetaBlock(metaBlock);
    return metaBlock;
  }

  private static IndexBlock getIndexBlock(SSTable ssTable, SSTableStorage storage,
                                          BufferCache bufferCache, MetaData metaData) throws IOException {
    IndexBlock indexBlock = bufferCache.getIndexBlock(ssTable.getFileName(), metaData.getIndexId());
    if (indexBlock != null) {
      return indexBlock;
    }
    indexBlock = storage.getIndexBlock(ssTable,
      metaData.getOffset(), metaData.getSize());
    bufferCache.addIndexBlock(indexBlock);
    return indexBlock;
  }

  private static DataBlock getDataBlock(SSTable ssTable, SSTableStorage storage,
                                        BufferCache bufferCache, IndexData indexData) throws IOException {

    DataBlock dataBlock = bufferCache.getDataBlock(ssTable.getFileName(), indexData.getDataId());
    if (dataBlock != null) {
      return dataBlock;
    }
    dataBlock = storage.getDataBlock(ssTable,
      indexData.getOffset(), indexData.getDataBlockSize());
    bufferCache.addDataBlock(dataBlock);
    return dataBlock;
  }
}
