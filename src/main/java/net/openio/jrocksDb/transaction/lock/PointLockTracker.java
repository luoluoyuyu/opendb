package net.openio.jrocksDb.transaction.lock;

import net.openio.jrocksDb.db.ColumnFamilyId;
import net.openio.jrocksDb.db.Key;
import net.openio.jrocksDb.transaction.SequenceNumber;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PointLockTracker implements LockTracker {

    Map<ColumnFamilyId, Map<Key, TrackedKeyInfo>> trackerKeys;

    @Override
    public boolean IsPointLockSupported() {
        return true;
    }

    @Override
    public SequenceNumber Track(PointLockRequest pointLockRequest) {
        ConcurrentHashMap<Key, TrackedKeyInfo> a = null;
        Map<Key, TrackedKeyInfo> cMap
                = trackerKeys.get(pointLockRequest.columnFamilyId);

        if (cMap == null) {
            trackerKeys.putIfAbsent
                    (pointLockRequest.columnFamilyId, new ConcurrentHashMap<>());
            cMap = trackerKeys.get(pointLockRequest.columnFamilyId);
        }

        TrackedKeyInfo trackedKeyInfo = cMap.get(pointLockRequest.key);

        if (trackedKeyInfo == null) {
            cMap.putIfAbsent(pointLockRequest.key, new TrackedKeyInfo(
                    pointLockRequest.seq, pointLockRequest.read_only ? 0 : 1
                    , pointLockRequest.read_only ? 1 : 0, pointLockRequest.exclusive));
            trackedKeyInfo = cMap.get(pointLockRequest.key);
        }
        SequenceNumber sequenceNumber=trackedKeyInfo.sequenceNumber;

        if (trackedKeyInfo.sequenceNumber.compareTo(trackedKeyInfo.sequenceNumber) == 0) {
            return sequenceNumber;
        }

        synchronized (trackedKeyInfo) {
            if (pointLockRequest.read_only) {
                trackedKeyInfo.readNum++;
            } else {
                trackedKeyInfo.writeNum++;
            }


            trackedKeyInfo.exclusive = trackedKeyInfo.exclusive || pointLockRequest.exclusive;
        }
        return sequenceNumber;
    }

    @Override
    public PointLockStatus GetPointLockStatus(ColumnFamilyId id, Key key) {
        Map<Key, TrackedKeyInfo> cMap
                = trackerKeys.get(id);
        if (cMap == null) return new PointLockStatus();
        TrackedKeyInfo trackedKeyInfo = cMap.get(key);
        if (trackedKeyInfo == null) return new PointLockStatus();

        return new PointLockStatus
                (true, trackedKeyInfo.exclusive, trackedKeyInfo.sequenceNumber);
    }

    @Override
    public UntrackStatus UnTrack(PointLockRequest pointLockRequest) {
        ConcurrentHashMap<Key, TrackedKeyInfo> a = null;
        Map<Key, TrackedKeyInfo> cMap
                = trackerKeys.get(pointLockRequest.columnFamilyId);

        if (cMap == null) {
            return UntrackStatus.NOT_TRACKED;

        }

        TrackedKeyInfo trackedKeyInfo = cMap.get(pointLockRequest.key);

        if (trackedKeyInfo == null) {
            return UntrackStatus.NOT_TRACKED;
        }

        boolean untracked = false;
        synchronized (trackedKeyInfo) {
            if (pointLockRequest.read_only) {
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
                return UntrackStatus.REMOVED;
            }
        }
        if (untracked) return UntrackStatus.UNTRACKED;

        return UntrackStatus.NOT_TRACKED;
    }

    private PointLockTracker() {
        trackerKeys = new ConcurrentHashMap<>();
    }

    public final static class PointLockTrackerFactory {


        public static LockTracker create() {

            return new PointLockTracker();
        }

    }

}
