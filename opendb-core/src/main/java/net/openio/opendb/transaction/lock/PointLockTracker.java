package net.openio.opendb.transaction.lock;

import net.openio.opendb.model.SequenceNumber;
import net.openio.opendb.model.key.Key;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PointLockTracker implements LockTracker {

  Map<Long, Map<Key, TrackedKeyInfo>> trackerKeys;

  @Override
  public boolean isPointLockSupported() {
    return true;
  }

  @Override
  public SequenceNumber track(PointLockRequest pointLockRequest) {
    ConcurrentHashMap<Key, TrackedKeyInfo> a = null;
    Map<Key, TrackedKeyInfo> cMap = trackerKeys.get(pointLockRequest.columnFamilyId);

    if (cMap == null) {
      trackerKeys.putIfAbsent(pointLockRequest.columnFamilyId, new ConcurrentHashMap<>());
      cMap = trackerKeys.get(pointLockRequest.columnFamilyId);
    }

    TrackedKeyInfo trackedKeyInfo = cMap.get(pointLockRequest.key);

    if (trackedKeyInfo == null) {
      cMap.putIfAbsent(pointLockRequest.key, new TrackedKeyInfo(
        pointLockRequest.seq, pointLockRequest.readOnly ? 0 : 1
        , pointLockRequest.readOnly ? 1 : 0, pointLockRequest.exclusive));
      trackedKeyInfo = cMap.get(pointLockRequest.key);
    }
    SequenceNumber sequenceNumber = trackedKeyInfo.sequenceNumber;

    if (trackedKeyInfo.sequenceNumber.compareTo(trackedKeyInfo.sequenceNumber) == 0) {
      return sequenceNumber;
    }

    synchronized (trackedKeyInfo) {
      if (pointLockRequest.readOnly) {
        trackedKeyInfo.readNum++;
      } else {
        trackedKeyInfo.writeNum++;
      }


      trackedKeyInfo.exclusive = trackedKeyInfo.exclusive || pointLockRequest.exclusive;
    }
    return sequenceNumber;
  }

  @Override
  public PointLockStatus getPointLockStatus(long id, Key key) {
    Map<Key, TrackedKeyInfo> cMap = trackerKeys.get(id);
    if (cMap == null) {
      return new PointLockStatus();
    }
    TrackedKeyInfo trackedKeyInfo = cMap.get(key);
    if (trackedKeyInfo == null) {
      return new PointLockStatus();
    }

    return new PointLockStatus(true, trackedKeyInfo.exclusive, trackedKeyInfo.sequenceNumber);
  }

  @Override
  public UnTrackStatus unTrack(PointLockRequest pointLockRequest) {
    ConcurrentHashMap<Key, TrackedKeyInfo> a = null;
    Map<Key, TrackedKeyInfo> cMap = trackerKeys.get(pointLockRequest.columnFamilyId);

    if (cMap == null) {
      return UnTrackStatus.NOT_TRACKED;

    }

    TrackedKeyInfo trackedKeyInfo = cMap.get(pointLockRequest.key);

    if (trackedKeyInfo == null) {
      return UnTrackStatus.NOT_TRACKED;
    }

    boolean untracked = false;
    synchronized (trackedKeyInfo) {
      if (pointLockRequest.readOnly) {
        if (trackedKeyInfo.readNum > 0) {
          trackedKeyInfo.readNum--;
          untracked = true;
        }
      } else {
        if (trackedKeyInfo.writeNum > 0) {
          trackedKeyInfo.writeNum--;
          untracked = true;
        }
      }

      if (trackedKeyInfo.writeNum == 0 && trackedKeyInfo.readNum == 0) {
        cMap.remove(pointLockRequest.key);
        if (cMap.isEmpty()) {
          trackerKeys.remove(pointLockRequest.columnFamilyId);
        }
        return UnTrackStatus.REMOVED;
      }
    }
    if (untracked) {
      return UnTrackStatus.UNTRACKED;
    }

    return UnTrackStatus.NOT_TRACKED;
  }

  private PointLockTracker() {
    trackerKeys = new ConcurrentHashMap<>();
  }

  public static final class PointLockTrackerFactory {

    public static LockTracker create() {
      return new PointLockTracker();
    }

  }

}
