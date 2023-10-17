package net.openio.opendb.db;

import net.openio.opendb.model.SequenceNumber;

import java.util.concurrent.atomic.AtomicBoolean;

public class SnapshotManager {

  private final Snapshot head = new Snapshot();

  private Snapshot tail = head;

  private AtomicBoolean addState = new AtomicBoolean(false);

  public Snapshot addSnapshot(SequenceNumber sequenceNumber) {
    Snapshot snapshot = new Snapshot(sequenceNumber);
    while (addState.compareAndSet(false, true)) {

    }
    Snapshot h = tail;
    while (h != head) {
      if (h.getSequenceNumber().compareTo(snapshot.getSequenceNumber()) <= 0) {
        break;
      }
      h = h.pre;
    }

    if (h.next != null) {
      snapshot.next = h.next;
      h.next.pre = snapshot;
    } else {
      tail = h;
    }
    h.next = snapshot;
    snapshot.pre = h;
    addState.set(false);
    return snapshot;
  }

  public void removeSnapshot(Snapshot snapshot) {
    while (addState.compareAndSet(false, true)) {

    }

    if (snapshot.next != null) {
      snapshot.pre.next = snapshot.next;
      snapshot.next.pre = snapshot.pre;
    }
    addState.set(false);
  }

  public SequenceNumber getMinSnapshot() {
    while (addState.compareAndSet(false, true)) {

    }
    SequenceNumber sequenceNumber = null;
    if (head.next != null) {
      sequenceNumber = head.next.getSequenceNumber();
    }
    addState.set(false);
    return sequenceNumber;
  }
}
