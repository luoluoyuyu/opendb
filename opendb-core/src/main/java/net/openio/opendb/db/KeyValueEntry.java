package net.openio.opendb.db;


import net.openio.opendb.model.key.Key;
import net.openio.opendb.model.value.Value;


public class KeyValueEntry {

  Key key;

  Value value;

  public KeyValueEntry(Key key, Value value) {
    this.key = key;
    this.value = value;
  }

  public KeyValueEntry() {
  }

  public KeyValueEntry(Key key) {
    this.key = key;
  }

  public KeyValueEntry(Value value) {
    this.value = value;
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
}
