package net.openio.opendb.mem;

import net.openio.opendb.storage.sstable.DataBlock;
import net.openio.opendb.storage.sstable.IndexBlock;
import net.openio.opendb.storage.sstable.MetaBlock;

public class BufferCache {

  private final LRUBufferCache<MetaBlock> metaBlocks;
  private final LRUBufferCache<IndexBlock> indexBlocks;
  private final LRUBufferCache<DataBlock> dataBlocks;

  public BufferCache(long expireTime, long scanTime, long oldBlocksTime, int maxCapacity) {
    this.metaBlocks = new LRUBufferCache<>(expireTime, scanTime, oldBlocksTime, maxCapacity);
    this.indexBlocks = new LRUBufferCache<>(expireTime, scanTime, oldBlocksTime, maxCapacity);
    this.dataBlocks = new LRUBufferCache<>(expireTime, scanTime, oldBlocksTime, maxCapacity);
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

  public void close(){
    metaBlocks.close();
    indexBlocks.close();
    dataBlocks.close();
  }
}
