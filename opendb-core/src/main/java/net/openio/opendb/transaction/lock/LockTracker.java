package net.openio.opendb.transaction.lock;


import net.openio.opendb.model.SequenceNumber;
import net.openio.opendb.model.key.Key;

public interface LockTracker {

  boolean isPointLockSupported();

  SequenceNumber track(PointLockRequest pointLockRequest);


  PointLockStatus getPointLockStatus(long cid, Key key);

  UnTrackStatus unTrack(PointLockRequest pointLockRequest);


  class PointLockRequest {

    long columnFamilyId;

    Key key;

    SequenceNumber seq;

    boolean readOnly = false;

    boolean exclusive = true;

    public PointLockRequest(long columnFamilyId, Key key, SequenceNumber seq, boolean readOnly, boolean exclusive) {
      this.columnFamilyId = columnFamilyId;
      this.key = key;
      this.seq = seq;
      this.readOnly = readOnly;
      this.exclusive = exclusive;
    }
  }



  class PointLockStatus {

    boolean locked = false;

    boolean exclusive = true;

    SequenceNumber seq = new SequenceNumber(0L);

    public PointLockStatus(boolean locked, boolean exclusive, SequenceNumber seq) {
      this.locked = locked;
      this.exclusive = exclusive;
      this.seq = seq;
    }

    public PointLockStatus() {
    }
  }


  enum UnTrackStatus {

    NOT_TRACKED,

    UNTRACKED,

    REMOVED,
  }


}
