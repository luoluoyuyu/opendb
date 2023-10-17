package net.openio.opendb.db;


import net.openio.opendb.model.SequenceNumber;


public class Snapshot {

  private SequenceNumber sequenceNumber;

  Snapshot next;

  Snapshot pre;

  public SequenceNumber getSequenceNumber() {
    return sequenceNumber;
  }

  public void setSequenceNumber(SequenceNumber sequenceNumber) {
    this.sequenceNumber = sequenceNumber;
  }

  Snapshot(SequenceNumber sequenceNumber) {
    this.sequenceNumber = sequenceNumber;

  }

  public Snapshot() {

  }
}
