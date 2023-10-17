package net.openio.opendb.storage.sstable;


import net.openio.opendb.tool.codec.sstable.MetaBlockProtoCodec;

import java.util.LinkedList;
import java.util.List;


public class MetaBlock implements Block{

  private List<MetaData> metaData;

  private SSTable ssTable;

  private int size;

  public void add(MetaData metaData) {
    this.metaData.add(metaData);
    size += MetaBlockProtoCodec.getAddMetaSize(metaData);
  }

  public MetaData get(int index) {
    return metaData.get(index);
  }

  public int getNum() {
    return metaData.size();

  }

  public MetaBlock() {
    metaData = new LinkedList<>();
  }

  public MetaBlock(List<MetaData> list) {
    metaData = new LinkedList<>();
  }

  public MetaBlock(List<MetaData> list, SSTable ssTable) {
    metaData = new LinkedList<>();
    this.ssTable = ssTable;
  }

  public MetaBlock(SSTable ssTable) {
    metaData = new LinkedList<>();
    this.ssTable = ssTable;
  }


  public List<MetaData> getMetaData() {
    return metaData;
  }

  public void setMetaData(List<MetaData> metaData) {
    this.metaData = metaData;
  }

  public SSTable getSsTable() {
    return ssTable;
  }

  public void setSsTable(SSTable ssTable) {
    this.ssTable = ssTable;
  }

  public int getSize() {
    return size;
  }

  public void setSize(int size) {
    this.size = size;
  }

  @Override
  public long getId() {
    return 0;
  }
}
