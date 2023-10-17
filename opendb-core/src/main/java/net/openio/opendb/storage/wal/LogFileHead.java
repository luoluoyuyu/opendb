package net.openio.opendb.storage.wal;


import java.util.LinkedList;
import java.util.List;


public class LogFileHead {

  private int length;

  private long createTime;

  private List<Integer> blockEndSeek;

  public void add(Integer i) {
    blockEndSeek.add(i);
  }

  public void add(int i, int pageSize) {
    if (blockEndSeek.size() <= 1) {
      blockEndSeek.add(i);
      return;
    }
    int l = blockEndSeek.size();
    int f = blockEndSeek.get(l - 1);
    int e = blockEndSeek.get(l - 2);
    if (f - e < pageSize) {
      blockEndSeek.set(l - 1, f + i);
    } else {
      blockEndSeek.add(i);
    }

  }

  public LogFileHead() {
    blockEndSeek = new LinkedList<>();
  }

  public int getLength() {
    return length;
  }

  public void setLength(int length) {
    this.length = length;
  }

  public long getCreateTime() {
    return createTime;
  }

  public void setCreateTime(long createTime) {
    this.createTime = createTime;
  }

  public List<Integer> getBlockEndSeek() {
    return blockEndSeek;
  }

  public void setBlockEndSeek(List<Integer> blockEndSeek) {
    this.blockEndSeek = blockEndSeek;
  }
}
