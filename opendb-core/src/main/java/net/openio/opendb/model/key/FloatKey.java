package net.openio.opendb.model.key;


import net.openio.opendb.model.SequenceNumber;
import net.openio.opendb.tool.Hash;

import java.util.Objects;


public class FloatKey implements Key {

  private Float value;
  private SequenceNumber sequenceNumber;

  public FloatKey(Float value) {
    this.value = value;
  }

  public FloatKey(Float value, SequenceNumber sequenceNumber) {
    this.value = value;
    this.sequenceNumber = sequenceNumber;
  }

  @Override
  public int hashCode() {
    return Hash.intHash(Float.floatToIntBits(value));
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
    this.value = (Float) key;
  }

  @Override
  public void setKey(Object value, SequenceNumber sequenceNumber) {
    this.value = (Float) value;
    this.sequenceNumber = sequenceNumber;
  }

  @Override
  public Key copy() {
    return new FloatKey(value, sequenceNumber);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    FloatKey floatKey = (FloatKey) o;
    return Objects.equals(value, floatKey.value);
  }


  @Override
  public int compareTo(Key o) {
    if (o == null) {
      throw new RuntimeException("o is null");
    }
    if (!(o instanceof FloatKey)) {
      throw new RuntimeException("o is not FloatKey Type");
    }
    FloatKey otherKey = (FloatKey) o;
    return Float.compare(this.value, otherKey.value);
  }

  public FloatKey(){

  }

  @Override
  public void setSequenceNumber(SequenceNumber sequenceNumber) {
    this.sequenceNumber = sequenceNumber;
  }
}
