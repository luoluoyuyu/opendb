package net.openio.opendb.model.key;


import net.openio.opendb.model.SequenceNumber;
import net.openio.opendb.tool.Hash;

import java.util.Objects;


public class IntKey implements Key {

  private Integer key;

  private SequenceNumber sequenceNumber;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    return Objects.equals(key, ((IntKey) o).key);
  }

  @Override
  public int hashCode() {
    return Hash.intHash(key);
  }

  @Override
  public Object getKey() {
    return key;
  }

  @Override
  public SequenceNumber getSequenceNumber() {
    return sequenceNumber;
  }

  @Override
  public void setKey(Object key) {
    this.key = (Integer) key;
  }

  @Override
  public void setKey(Object value, SequenceNumber sequenceNumber) {
    key = (Integer) value;
    this.sequenceNumber = sequenceNumber;
  }

  @Override
  public Key copy() {
    return new IntKey(key, sequenceNumber);
  }

  @Override
  public int compareTo(Key o) {
    if (o == null) {
      throw new RuntimeException("o is null");
    }
    if (!(o instanceof IntKey)) {
      throw new RuntimeException("o is not IntKey Type");
    }
    int otherKey = ((IntKey) o).key;
    return Integer.compare(this.key, otherKey);
  }

  @Override
  public void setSequenceNumber(SequenceNumber sequenceNumber) {
    this.sequenceNumber = sequenceNumber;
  }

  public IntKey(Integer key) {
    this.key = key;
  }

  public IntKey(Integer key, SequenceNumber sequenceNumber) {
    this.key = key;
    this.sequenceNumber = sequenceNumber;
  }

  public IntKey(){

  }
}
