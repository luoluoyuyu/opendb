package net.openio.opendb.model.key;


import net.openio.opendb.model.SequenceNumber;
import net.openio.opendb.tool.Hash;

import java.util.Arrays;


public class BytesKey implements Key {

  private byte[] key;

  private SequenceNumber sequenceNumber;

  @Override
  public boolean equals(Object o) {
    if (this == o){
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BytesKey bytesKey = (BytesKey) o;
    return Arrays.equals(key, bytesKey.key);
  }

  @Override
  public int hashCode() {
    return Hash.hash32(key, key.length);
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
    this.key = (byte[]) key;
  }

  @Override
  public void setKey(Object value, SequenceNumber sequenceNumber) {
    key = (byte[]) value;
    this.sequenceNumber = sequenceNumber;
  }

  @Override
  public Key copy() {
    return new BytesKey(Arrays.copyOf(key, key.length), sequenceNumber);
  }

  @Override
  public int compareTo(Key o) {
    if (o == null) {
      throw new RuntimeException("o is null");
    }
    if (!(o instanceof BytesKey)) {
      throw new RuntimeException("o is not BytesKey Type");
    }
    byte[] otherKey = ((BytesKey) o).key;
    return compareBytes(this.key, otherKey);
  }

  private int compareBytes(byte[] a, byte[] b) {
    if (a == b) {
      return 0;
    }
    if (a == null || b == null) {
      return (a == null) ? -1 : 1;
    }

    int minLength = Math.min(a.length, b.length);
    for (int i = 0; i < minLength; i++) {
      int cmp = Byte.compare(a[i], b[i]);
      if (cmp != 0) {
        return cmp;
      }
    }

    return Integer.compare(a.length, b.length);
  }

  public BytesKey(byte[] key) {
    this.key = key;
  }

  public BytesKey(byte[] key, SequenceNumber sequenceNumber) {
    this.key = key;
    this.sequenceNumber = sequenceNumber;
  }

  public BytesKey(){

  }
}

