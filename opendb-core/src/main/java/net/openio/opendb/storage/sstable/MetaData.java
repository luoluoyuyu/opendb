package net.openio.opendb.storage.sstable;


import net.openio.opendb.model.key.Key;


public class MetaData {

  private long indexId;

  private Key key;

  private int offset;

  private int size;

  private SSTable ssTable;

  public MetaData(long indexId, Key key, int offset, int size, SSTable ssTable) {
    this.indexId = indexId;
    this.key = key;
    this.offset = offset;
    this.size = size;
    this.ssTable = ssTable;
  }

  public MetaData() {
  }

  public long getIndexId() {
    return indexId;
  }

  public void setIndexId(long indexId) {
    this.indexId = indexId;
  }

  public Key getKey() {
    return key;
  }

  public void setKey(Key key) {
    this.key = key;
  }

  public int getOffset() {
    return offset;
  }

  public void setOffset(int offset) {
    this.offset = offset;
  }

  public int getSize() {
    return size;
  }

  public void setSize(int size) {
    this.size = size;
  }

  public SSTable getSsTable() {
    return ssTable;
  }

  public void setSsTable(SSTable ssTable) {
    this.ssTable = ssTable;
  }
}
