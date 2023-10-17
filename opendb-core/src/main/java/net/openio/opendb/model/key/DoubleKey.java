package net.openio.opendb.model.key;


import net.openio.opendb.model.SequenceNumber;
import net.openio.opendb.tool.Hash;

import java.util.Objects;


public class DoubleKey implements Key {

  private Double key;
  private SequenceNumber sequenceNumber;

  public DoubleKey(Double key) {
    this.key = key;
  }

  public DoubleKey() {

  }

  public DoubleKey(Double key, SequenceNumber sequenceNumber) {
    this.key = key;
    this.sequenceNumber = sequenceNumber;
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
  public void setSequenceNumber(SequenceNumber sequenceNumber) {
    this.sequenceNumber = sequenceNumber;
  }

  @Override
  public void setKey(Object key) {
    this.key = (Double) key;
  }

  @Override
  public void setKey(Object value, SequenceNumber sequenceNumber) {
    key = (Double) value;
    this.sequenceNumber = sequenceNumber;
  }

  @Override
  public Key copy() {
    return new DoubleKey(key, sequenceNumber);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    return Objects.equals(key, ((DoubleKey) o).key);
  }

  @Override
  public int hashCode() {
    return Hash.longHash(Double.doubleToLongBits(key));
  }

  @Override
  public int compareTo(Key o) {
    if (o == null) {
      throw new RuntimeException("o is null");
    }
    if (!(o instanceof DoubleKey)) {
      throw new RuntimeException("o is not DoubleKey Type");
    }
    double otherKey = ((DoubleKey) o).key;
    return Double.compare(this.key, otherKey);
  }
}
