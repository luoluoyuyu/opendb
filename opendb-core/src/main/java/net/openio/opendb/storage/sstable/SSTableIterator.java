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
import net.openio.opendb.storage.metadata.Level;
import net.openio.opendb.storage.metadata.Levels;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;

public class SSTableIterator {

  SSTableStorage storage;

  BufferCache bufferCache;

  public SSTableIterator(SSTableStorage storage, BufferCache bufferCache) {
    this.storage = storage;
    this.bufferCache = bufferCache;
  }


  public Value getValue(Levels levels, Key key, Comparator<Key> comparator) throws IOException {
    Value value = null;
    for (Level level : levels.getLevels()) {
      for (SSTable ssTable : level.getSsTables()) {
        value = getValue(ssTable, key, comparator);
        if (value != null) {
          return value;
        }
      }
    }
    return value;

  }

  private Value getValue(SSTable ssTable, Key key, Comparator<Key> comparator) throws IOException {
    if (ssTable.getMinKey().compareTo(key) > 0) {
      return null;
    }

    if (ssTable.getMaxValue().compareTo(key) < 0) {
      return null;
    }

    FileHeadBlock fileHeadBlock = storage.getFileHead(ssTable);

    MetaBlock metaBlock = getMetaBlock(ssTable, fileHeadBlock);

    int index = binarySearchMetadata(metaBlock.getMetaData(), key, comparator);

    MetaData metaData = metaBlock.get(index);

    IndexBlock indexBlock = getIndexBlock(ssTable, metaData);
    int a = 0;
    for (IndexData indexData : indexBlock.getDataList()) {
      a = binarySearch(indexData.getKeys(), key, comparator);
      if (a != -1) {
        return getDataBlock(ssTable, indexData).get(a);
      }
    }
    return null;
  }


  public static int binarySearch(List<Key> keyList, Key targetKey, Comparator<Key> comparator) {
    int left = 0;
    int right = keyList.size() - 1;
    while (left <= right) {
      int mid = left + (right - left) / 2;
      Key key = keyList.get(mid);
      int comparison = comparator.compare(targetKey, key);
      if (comparison == 0) {

        return mid;
      } else if (comparison < 0) {

        right = mid - 1;
      } else {

        left = mid + 1;
      }
    }

    return -1;
  }

  public static int binarySearchMetadata(List<MetaData> metaDataList, Key targetKey, Comparator<Key> comparator) {
    int left = 0;
    int right = metaDataList.size() - 1;
    int resultIndex = -1;

    while (left <= right) {
      int mid = left + (right - left) / 2;
      MetaData metadata = metaDataList.get(mid);
      int comparison = comparator.compare(targetKey, metadata.getKey());

      if (comparison == 0) {
        return mid;
      } else if (comparison < 0) {
        right = mid - 1;
      } else {
        resultIndex = mid;
        left = mid + 1;
      }
    }
    return resultIndex;
  }


  private MetaBlock getMetaBlock(SSTable ssTable, FileHeadBlock fileHeadBlock) throws IOException {
    MetaBlock metaBlock = bufferCache.getMetaBlock(ssTable.getFileName(), 0);
    if (metaBlock != null) {
      return metaBlock;
    }
    metaBlock = storage.getMetaBlock(ssTable,
      fileHeadBlock.getMetaOfferSeek(), fileHeadBlock.getMetaOfferSize());
    bufferCache.addMetaBlock(metaBlock);
    return metaBlock;
  }

  private IndexBlock getIndexBlock(SSTable ssTable, MetaData metaData) throws IOException {
    IndexBlock indexBlock = bufferCache.getIndexBlock(ssTable.getFileName(), metaData.getIndexId());
    if (indexBlock != null) {
      return indexBlock;
    }
    indexBlock = storage.getIndexBlock(ssTable,
      metaData.getOffset(), metaData.getSize());
    bufferCache.addIndexBlock(indexBlock);
    return indexBlock;
  }

  private DataBlock getDataBlock(SSTable ssTable, IndexData indexData) throws IOException {

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
