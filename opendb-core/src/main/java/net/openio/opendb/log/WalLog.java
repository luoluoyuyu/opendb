package net.openio.opendb.log;


import net.openio.opendb.model.SequenceNumber;
import net.openio.opendb.model.key.Key;
import net.openio.opendb.model.key.KeyType;
import net.openio.opendb.model.value.Value;
import net.openio.opendb.model.value.ValueType;


public class WalLog implements Log{

  private long columnId;

  private Key key;

  private Value value;

  private KeyType keyType;

  private ValueType valueType;

  private long transactionId;

  private SequenceNumber sequenceNumber;

  public WalLog() {
  }

  public WalLog(long columnId, Key key, Value value, KeyType keyType, ValueType valueType) {
    this.columnId = columnId;
    this.key = key;
    this.value = value;
    this.keyType = keyType;
    this.valueType = valueType;
  }

  @Override
  public int compare(SequenceNumber sequenceNumber) {
    return this.sequenceNumber.compareTo(sequenceNumber);
  }

  public long getColumnId() {
    return columnId;
  }

  public void setColumnId(long columnId) {
    this.columnId = columnId;
  }

  public Key getKey() {
    return key;
  }

  public void setKey(Key key) {
    this.key = key;
  }

  public Value getValue() {
    return value;
  }

  public void setValue(Value value) {
    this.value = value;
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

  @Override
  public long getTransactionId() {
    return transactionId;
  }

  public void setTransactionId(long transactionId) {
    this.transactionId = transactionId;
  }

  public SequenceNumber getSequenceNumber() {
    return sequenceNumber;
  }

  public void setSequenceNumber(SequenceNumber sequenceNumber) {
    this.sequenceNumber = sequenceNumber;
  }
}
