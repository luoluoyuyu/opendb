package net.openio.opendb.model;

import net.openio.opendb.db.ColumnFamily;
import net.openio.opendb.model.key.KeyType;
import net.openio.opendb.model.value.ValueType;

public class ColumnFamilyDescriptor {

  public String name;

  public KeyType keyType;

  public ValueType valueType;

  public int blockSize;

  public ColumnFamilyDescriptor(ColumnFamily columnFamily) {
    name = columnFamily.getName();
    keyType = columnFamily.getKeyType();
    valueType = columnFamily.getValueType();
    blockSize = columnFamily.getStorageBlockSize();
  }


  public ColumnFamilyDescriptor(String name, KeyType keyType, ValueType valueType, int blockSize) {
    this.name = name;
    this.keyType = keyType;
    this.valueType = valueType;
    this.blockSize = blockSize;
  }

  public ColumnFamilyDescriptor() {
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public KeyType getKeyType() {
    return keyType;
  }

  public void setKeyType(KeyType keyType) {
    this.keyType = keyType;
  }

  public ValueType getValueType() {
    return valueType;
  }

  public void setValueType(ValueType valueType) {
    this.valueType = valueType;
  }

  public int getBlockSize() {
    return blockSize;
  }

  public void setBlockSize(int blockSize) {
    this.blockSize = blockSize;
  }
}
