package net.openio.opendb.db;


import net.openio.opendb.model.key.KeyType;
import net.openio.opendb.model.value.ValueType;

public class ColumnFamilyHandle {
  public final String name;

  public final long cId;

  public final KeyType keyType;

  public final ValueType valueType;

  ColumnFamilyHandle(String name, long cId, KeyType keyType, ValueType valueType) {
    this.name = name;
    this.cId = cId;
    this.keyType = keyType;
    this.valueType = valueType;
  }

  ColumnFamilyHandle(ColumnFamily columnFamily) {
    this.name = columnFamily.getName();
    this.cId = columnFamily.getColumnFamilyId();
    this.keyType = columnFamily.getKeyType();
    this.valueType = columnFamily.getValueType();
  }

  public String getName() {
    return name;
  }

  public long getColumnFamilyId() {
    return cId;
  }

  public KeyType getKeyType() {
    return keyType;
  }

  public ValueType getValueType() {
    return valueType;
  }
}
