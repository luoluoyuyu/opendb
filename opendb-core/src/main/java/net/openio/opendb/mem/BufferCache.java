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

import net.openio.opendb.storage.sstable.DataBlock;
import net.openio.opendb.storage.sstable.IndexBlock;
import net.openio.opendb.storage.sstable.MetaBlock;

public class BufferCache {

  private LRUBufferCache<MetaBlock> metaBlocks;
  private LRUBufferCache<IndexBlock> indexBlocks;
  private LRUBufferCache<DataBlock> dataBlocks;

  public BufferCache(
    LRUBufferCache<MetaBlock> metaBlocks,
    LRUBufferCache<IndexBlock> indexBlocks,
    LRUBufferCache<DataBlock> dataBlocks) {
    this.metaBlocks = metaBlocks;
    this.indexBlocks = indexBlocks;
    this.dataBlocks = dataBlocks;
  }


  public MetaBlock getMetaBlock(String ssTable, long id) {
    return metaBlocks.getData(ssTable, id);
  }


  public IndexBlock getIndexBlock(String ssTable, long id) {
    return indexBlocks.getData(ssTable, id);
  }


  public DataBlock getDataBlock(String ssTable, long id) {
    return dataBlocks.getData(ssTable, id);
  }

  public MetaBlock getMetaBlockNotUpdate(String ssTable, long id) {
    return metaBlocks.getDataNotUpdate(ssTable, id);
  }


  public IndexBlock getIndexBlockNotUpdate(String ssTable, long id) {
    return indexBlocks.getDataNotUpdate(ssTable, id);
  }


  public DataBlock getDataBlockNotUpdate(String ssTable, long id) {
    return dataBlocks.getDataNotUpdate(ssTable, id);
  }


  public void addMetaBlock(MetaBlock metaBlock) {
    metaBlocks.add(metaBlock, metaBlock.getSize(), metaBlock.getSsTable().getFileName());
  }


  public void addIndexBlock(IndexBlock indexBlock) {
    indexBlocks.add(indexBlock, indexBlock.getSize(), indexBlock.getSsTable().getFileName());
  }


  public void addDataBlock(DataBlock dataBlock) {
    dataBlocks.add(dataBlock, dataBlock.getSize(), dataBlock.getSsTable().getFileName());
  }
}
