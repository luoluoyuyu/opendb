package net.openio.opendb.mem;

import net.openio.opendb.db.KeyValueEntry;
import net.openio.opendb.model.key.Key;

import java.util.List;

public class BloomFilter {

  private byte[] data;


  public BloomFilter() {
    data = new byte[1024];

  }

  public BloomFilter(int length) {
    length = roundUpToPowerOf2(length);
    if (length <= 0) {
      throw new RuntimeException("bloomFiler array size mem out ");
    }
    data = new byte[length];
  }


  private int roundUpToPowerOf2(int x) {
    x--;
    x |= x >> 1;
    x |= x >> 2;
    x |= x >> 4;
    x |= x >> 8;
    x |= x >> 16;
    x++;
    return x;
  }


  public void add(Key k) {

    int index = getIndex(k.hashCode());

    data[index] = 1;
  }

  public void add(List<KeyValueEntry> k) {
    for (KeyValueEntry key : k) {
      int index = getIndex(key.getKey().hashCode());
      data[index] = 1;
    }
  }

  public boolean get(Key key) {
    int index = getIndex(key.hashCode());
    return data[index] != 0;
  }

  public byte[] getData() {
    return data;
  }

  public void setData(byte[] data) {
    this.data = data;
  }


  private int getIndex(int hash) {
    return hash & (data.length - 1);
  }


}

