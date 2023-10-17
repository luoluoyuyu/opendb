package net.openio.opendb.storage.sstable;


import net.openio.opendb.model.value.Value;
import net.openio.opendb.model.value.ValueType;
import net.openio.opendb.tool.codec.sstable.DataBlockProtoCodec;

import java.util.LinkedList;
import java.util.List;


public class DataBlock implements Block{

  private long id; //tag=1

  private int size;

  private int valueNum; //tag=3;

  private List<Value> values; //tag=4;

  private int offerSet;

  private SSTable ssTable;

  public Value get(int i){
    return values.get(i);
  }

  public void add(List<Value> values) {
    this.values = values;
    size += DataBlockProtoCodec.getAddValueSize(this, values);
  }

  public void add(Value value) {
    values.add(value);
    valueNum = values.size();
    size += DataBlockProtoCodec.getAddValueSize(this, value);
  }

  public boolean addValue(Value value, int expSize) {
    int size = DataBlockProtoCodec.getAddValueSize(this, value);
    if (this.size + size > expSize) {
      return false;
    }
    this.addNotComSize(value);
    this.size = size + this.size;
    return true;
  }

  public void addNotComSize(Value value) {
    values.add(value);
    valueNum = values.size();
  }


  public DataBlock() {
    values = new LinkedList<>();
    size = DataBlockProtoCodec.getHeadLength();
  }

  public DataBlock(int id, SSTable ssTable, int seek) {
    this.id = id;
    this.ssTable = ssTable;
    offerSet = seek;
    values = new LinkedList<>();
    size = DataBlockProtoCodec.getHeadLength();
  }

  public ValueType getValueType() {
    return ssTable.getValueType();
  }


  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public int getSize() {
    return size;
  }

  public void setSize(int size) {
    this.size = size;
  }

  public int getValueNum() {
    return valueNum;
  }

  public void setValueNum(int valueNum) {
    this.valueNum = valueNum;
  }

  public List<Value> getValues() {
    return values;
  }

  public void setValues(List<Value> values) {
    this.values = values;
    size = DataBlockProtoCodec.getAddValueSize(this, values) + DataBlockProtoCodec.getHeadLength();
  }

  public int getOfferSet() {
    return offerSet;
  }

  public void setOfferSet(int offerSet) {
    this.offerSet = offerSet;
  }

  public SSTable getSsTable() {
    return ssTable;
  }

  public void setSsTable(SSTable ssTable) {
    this.ssTable = ssTable;
  }
}
