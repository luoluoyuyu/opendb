package net.openio.opendb.model.key;


import net.openio.opendb.model.SequenceNumber;
import net.openio.opendb.tool.Hash;

import java.util.Objects;


public class StringKey implements Key {

  private String value;
  private SequenceNumber sequenceNumber;

  public StringKey(String value) {
    this.value = value;
  }

  public StringKey(String value, SequenceNumber sequenceNumber) {
    this.value = value;
    this.sequenceNumber = sequenceNumber;
  }

  @Override
  public Object getKey() {
    return value;
  }

  @Override
  public SequenceNumber getSequenceNumber() {
    return sequenceNumber;
  }

  @Override
  public void setKey(Object key) {
    this.value = (String) key;
  }

  @Override
  public void setKey(Object value, SequenceNumber sequenceNumber) {
    this.value = (String) value;
    this.sequenceNumber = sequenceNumber;
  }

  @Override
  public Key copy() {
    return new StringKey(value, sequenceNumber);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    StringKey stringKey = (StringKey) o;
    return Objects.equals(value, stringKey.value);
  }

  @Override
  public int hashCode() {
    return Hash.hash32(value.getBytes(), 0, value.length());
  }

  @Override
  public void setSequenceNumber(SequenceNumber sequenceNumber) {
    this.sequenceNumber = sequenceNumber;
  }

  @Override
  public int compareTo(Key o) {
    if (o == null) {
      throw new RuntimeException("o is null");
    }
    if (!(o instanceof StringKey)) {
      throw new RuntimeException("o is not StringKey Type");
    }
    StringKey otherKey = (StringKey) o;
    return otherKey.value.compareTo(value);
  }

  public StringKey() {

  }
}
