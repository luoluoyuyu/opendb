package net.openio.opendb.transaction.lock;

import net.openio.opendb.model.SequenceNumber;
import net.openio.opendb.model.key.Key;


import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class PointLockManager implements LockManager {

  private long expirationTime;

  private long tryLockTime;

  private long waitTime;

  ConcurrentHashMap<Long, CFKey> waitInfo;

  ConcurrentHashMap<CFKey, Long> useKey;

  LockTracker lockTracker;

  HashMap<Long, LockMap> lockMaps;


  @Override
  public boolean isPointLockSupported() {
    return true;
  }


  public void tryLock(Long cId, Key key, boolean exclusive) {


  }

  public void tracker(Long cId, Key key, SequenceNumber sequenceNumber) {


  }

  public void unLock(Long cId, Key key) {

  }


  public SequenceNumber tryLock(long tId, Long cId, Key key, boolean exclusive, boolean readOnly) {

    return new SequenceNumber();
  }


  public void unLock(long tid, Long cId, Key key) {

  }


  public void tracker(long tid, Long cId, Key key, SequenceNumber sequenceNumber) {
    lockTracker.track(new LockTracker.PointLockRequest(cId, key, sequenceNumber, false, false));
  }


  public boolean validate(Long cId, Key key, SequenceNumber sequenceNumber) {
    LockTracker.PointLockStatus status = lockTracker.getPointLockStatus(cId, key);
    return status.seq.compareTo(sequenceNumber) <= 0;
  }


  public PointLockManager(long expirationTime, long tryLockTime,
                          long waitTime) {
    this.expirationTime = expirationTime;
    this.tryLockTime = tryLockTime;
    this.waitTime = waitTime;
    waitInfo = new ConcurrentHashMap<>();

    useKey = new ConcurrentHashMap<>();

    lockTracker = PointLockTracker.PointLockTrackerFactory.create();

    lockMaps = new HashMap<>();
  }

  public PointLockManager(long expirationTime, long tryLockTime
  ) {
    this.expirationTime = expirationTime;
    this.tryLockTime = tryLockTime;
    this.waitTime = 1;
    waitInfo = new ConcurrentHashMap<>();

    useKey = new ConcurrentHashMap<>();

    lockTracker = PointLockTracker.PointLockTrackerFactory.create();

    lockMaps = new HashMap<>();
  }


  public PointLockManager() {
    this.expirationTime = 15000;
    this.tryLockTime = 1000;
    this.waitTime = 1;
    waitInfo = new ConcurrentHashMap<>();

    useKey = new ConcurrentHashMap<>();

    lockTracker = PointLockTracker.PointLockTrackerFactory.create();

    lockMaps = new HashMap<>();
  }


}
