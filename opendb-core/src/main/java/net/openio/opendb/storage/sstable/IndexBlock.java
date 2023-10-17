package net.openio.opendb.storage.sstable;


import net.openio.opendb.tool.codec.sstable.IndexBlockProtoCodec;

import java.util.LinkedList;
import java.util.List;


public class IndexBlock implements Block{

  private long id;

  private int offset;

  private int indexDataSize;

  private List<IndexData> dataList;

  private SSTable ssTable;

  private int size;

  public void add(IndexData indexData) {
    dataList.add(indexData);
    size += IndexBlockProtoCodec.getIndexDataSize(indexData);
  }

  public IndexData get(int index) {
    return dataList.get(index);

  }


  public void addNotComSize(IndexData indexData) {
    dataList.add(indexData);
  }

  public int getSize() {
    return size + IndexBlockProtoCodec.getHeadSize(this);

  }

  public int getNum() {
    return dataList.size();
  }

  public IndexBlock() {
    dataList = new LinkedList<>();
  }


  public IndexBlock(long id, SSTable ssTable, int seek) {
    this.id = id;
    this.ssTable = ssTable;
    offset = seek;
    dataList = new LinkedList<>();
  }


  public boolean addKey(IndexData indexData, int expSize) {
    int size = IndexBlockProtoCodec.getIndexDataSize(indexData) + IndexBlockProtoCodec.getHeadSize(this);
    if (this.size + size > expSize) {
      return false;
    }
    this.addNotComSize(indexData);
    this.size = size + this.size;
    return true;
  }


  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public int getOffset() {
    return offset;
  }

  public void setOffset(int offset) {
    this.offset = offset;
  }

  public int getIndexDataSize() {
    return indexDataSize;
  }

  public void setIndexDataSize(int indexDataSize) {
    this.indexDataSize = indexDataSize;
  }

  public List<IndexData> getDataList() {
    return dataList;
  }

  public void setDataList(List<IndexData> dataList) {
    this.dataList = dataList;
  }

  public SSTable getSsTable() {
    return ssTable;
  }

  public void setSsTable(SSTable ssTable) {
    this.ssTable = ssTable;
  }

  public void setSize(int size) {
    this.size = size;
  }
}
