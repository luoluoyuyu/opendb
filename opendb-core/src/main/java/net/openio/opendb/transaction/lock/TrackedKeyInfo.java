package net.openio.opendb.transaction.lock;



import net.openio.opendb.model.SequenceNumber;


public class TrackedKeyInfo {

  volatile SequenceNumber sequenceNumber;

  volatile int writeNum;

  volatile int readNum;

  volatile boolean exclusive;

  public TrackedKeyInfo(SequenceNumber sequenceNumber, int writeNum, int readNum, boolean exclusive) {
    this.sequenceNumber = sequenceNumber;
    this.writeNum = writeNum;
    this.readNum = readNum;
    this.exclusive = exclusive;
  }

  public TrackedKeyInfo() {
  }

  public SequenceNumber getSequenceNumber() {
    return sequenceNumber;
  }

  public void setSequenceNumber(SequenceNumber sequenceNumber) {
    this.sequenceNumber = sequenceNumber;
  }

  public int getWriteNum() {
    return writeNum;
  }

  public void setWriteNum(int writeNum) {
    this.writeNum = writeNum;
  }

  public int getReadNum() {
    return readNum;
  }

  public void setReadNum(int readNum) {
    this.readNum = readNum;
  }

  public boolean isExclusive() {
    return exclusive;
  }

  public void setExclusive(boolean exclusive) {
    this.exclusive = exclusive;
  }
}
