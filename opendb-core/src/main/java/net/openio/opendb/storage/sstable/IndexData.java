package net.openio.opendb.storage.sstable;


import net.openio.opendb.model.key.Key;
import net.openio.opendb.model.key.KeyType;
import net.openio.opendb.tool.codec.sstable.IndexBlockProtoCodec;
import net.openio.opendb.tool.codec.sstable.IndexDataProtoCodec;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;


public class IndexData {

  private long dataId;

  private int offset;

  private int dataBlockSize;

  private int num;

  private int firstIndex;

  int size;

  private List<Key> keys;

  private SSTable ssTable;

  public KeyType getKeyType() {
    return ssTable.getKeyType();
  }

  public void add(Key key) {
    keys.add(key);
    num = keys.size();
    size += IndexDataProtoCodec.getKeyByteSize(getKeyType(), key);
  }

  public void addNotComSize(Key key) {
    keys.add(key);
    num = keys.size();
  }

  public Key get(int index) {
    return keys.get(index);
  }


  public int getSize() {
    return size + IndexDataProtoCodec.getHeadByteSize(this);
  }

  public IndexData() {
    keys = new LinkedList<>();
  }

  public IndexData(long id, SSTable ssTable, int seek, int dataBlockSize) {
    this.dataId = id;
    this.ssTable = ssTable;
    offset = seek;
    keys = new LinkedList<>();
    this.dataBlockSize = dataBlockSize;
  }

  public IndexData(long id, SSTable ssTable, int seek) {
    this.dataId = id;
    this.ssTable = ssTable;
    offset = seek;
    keys = new LinkedList<>();
  }

  public boolean addKey(Key key, int expSize) {
    this.add(key);
    if (IndexBlockProtoCodec.getIndexDataSize(this) > expSize) {
      keys.remove(keys.size() - 1);
      num = keys.size();
      size = IndexDataProtoCodec.getKeyByteSize(getKeyType(), keys);
      return false;
    }
    return true;
  }

  public IndexData getMaxIndexData(int size) {
    IndexData indexData = new IndexData(this.dataId, ssTable, this.offset, this.dataBlockSize);
    indexData.setFirstIndex(firstIndex);
    Iterator<Key> i = keys.iterator();
    while (i.hasNext()) {
      if (!indexData.addKey(i.next(), size)) {
        break;
      }
      i.remove();
      firstIndex++;
    }
    this.size = IndexDataProtoCodec.getKeyByteSize(getKeyType(), keys);
    num = keys.size();
    return indexData;
  }

  public long getDataId() {
    return dataId;
  }

  public void setDataId(long dataId) {
    this.dataId = dataId;
  }

  public int getOffset() {
    return offset;
  }

  public void setOffset(int offset) {
    this.offset = offset;
  }

  public int getDataBlockSize() {
    return dataBlockSize;
  }

  public void setDataBlockSize(int dataBlockSize) {
    this.dataBlockSize = dataBlockSize;
  }

  public int getNum() {
    return num;
  }

  public void setNum(int num) {
    this.num = num;
  }

  public int getFirstIndex() {
    return firstIndex;
  }

  public void setFirstIndex(int firstIndex) {
    this.firstIndex = firstIndex;
  }

  public void setSize(int size) {
    this.size = size;
  }

  public List<Key> getKeys() {
    return keys;
  }

  public void setKeys(List<Key> keys) {
    this.keys = keys;
  }

  public SSTable getSsTable() {
    return ssTable;
  }

  public void setSsTable(SSTable ssTable) {
    this.ssTable = ssTable;
  }
}
