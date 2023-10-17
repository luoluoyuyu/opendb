package net.openio.opendb.model.key;



import net.openio.opendb.model.SequenceNumber;
import net.openio.opendb.tool.Hash;

import java.util.Objects;


public class LongKey implements Key {

  Long key;

  SequenceNumber sequenceNumber;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    return Objects.equals(key, ((LongKey) o).key);
  }

  @Override
  public int hashCode() {
    return Hash.longHash(key);
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
    this.key = (Long) key;
  }

  @Override
  public void setKey(Object value, SequenceNumber sequenceNumber) {
    key = (Long) value;
  }

  @Override
  public Key copy() {
    return new LongKey(key, sequenceNumber);
  }


  @Override
  public int compareTo(Key o) {
    if (o == null) {
      throw new RuntimeException("o is null");
    }
    if (!(o instanceof LongKey)) {
      throw new RuntimeException(" o is not LongKey Type");
    }
    long key = ((LongKey) o).key;
    return (this.key < key) ? -1 : ((this.key == key) ? 0 : 1);
  }

  @Override
  public void setSequenceNumber(SequenceNumber sequenceNumber) {
    this.sequenceNumber = sequenceNumber;
  }

  public LongKey(Long key) {
    this.key = key;
  }

  public LongKey() {

  }

  public LongKey(Long key, SequenceNumber sequenceNumber) {
    this.key = key;
    this.sequenceNumber = sequenceNumber;
  }
}
