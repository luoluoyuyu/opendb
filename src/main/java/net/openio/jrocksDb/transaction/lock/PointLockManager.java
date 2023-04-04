package net.openio.jrocksDb.transaction.lock;

import net.openio.jrocksDb.db.ColumnFamilyHandle;
import net.openio.jrocksDb.db.ColumnFamilyId;
import net.openio.jrocksDb.db.Key;
import net.openio.jrocksDb.transaction.SequenceNumber;

import javax.xml.crypto.Data;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;

public class PointLockManager implements LockManager {

    private long expirationTime;

    private long tryLockTime;

    private long waitTime;

    ConcurrentHashMap<Long, CFKey> waitInfo;

    ConcurrentHashMap<CFKey, Long> useKey;

    LockTracker lockTracker;

    HashMap<ColumnFamilyId, LockMap> lockMaps;


    @Override
    public boolean IsPointLockSupported() {
        return true;
    }


    @Override
    public SequenceNumber TryLock(long tId, ColumnFamilyId cId, Key key, boolean exclusive
            , boolean readOnly, SequenceNumber sequenceNumber,boolean isTracker) {
        LockMap lm = null;
        synchronized (this) {
            lm = lockMaps.get(cId);
            if (lm == null) {
                lockMaps.put(cId, lm=new LockMap());

            }
        }
        LockMapStripes lms = null;
        synchronized (lm) {
            lms = lm.LockMap.get(key);
            if (lms == null) {
                 lm.LockMap.put(key, lms =new LockMapStripes());
            }
        }

        Lock lock = exclusive ? lms.reentrantReadWriteLock.writeLock()
                : lms.reentrantReadWriteLock.readLock();

        Date date = new Date();
        waitInfo.put(tId, new CFKey(cId, key));
        while (!lock.tryLock()) {
            try {
                lms.wait(waitTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (dealLockCheck(tId)) {
                waitInfo.remove(tId);
                return null;
            }

            Date date1 = new Date();
            if (date1.getTime() <= date.getTime() + tryLockTime) {
                waitInfo.remove(tId);
                return null;
            }
        }
        waitInfo.remove(tId);

        useKey.put(new CFKey(cId, key), tId);


        LockInfo lockInfo = new LockInfo(tId, exclusive, new Date().getTime() + expirationTime);

        lms.add(lockInfo);

        if(isTracker){
            return lockTracker.Track(new LockTracker.PointLockRequest(cId, key, sequenceNumber, readOnly, exclusive));
        }

        return lockTracker.GetPointLockStatus(cId,key).seq;

    }

    @Override
    public void UnLock(long tId, ColumnFamilyId cId, Key key, boolean exclusive, boolean readOnly,boolean isTracker) {
        LockMap lm = lockMaps.get(cId);
        if (lm == null) {
            throw new RuntimeException("LockManager UNLock:LockMap is null");
        }

        LockMapStripes lms = lm.LockMap.get(key);

        if (lms == null) {
            throw new RuntimeException("LockManager UNLock:LockMapStripes is null");
        }

        Lock lock = exclusive ? lms.reentrantReadWriteLock.writeLock()
                : lms.reentrantReadWriteLock.readLock();


        useKey.remove(new CFKey(cId, key));

        lms.delete(tId);
        synchronized (lm) {
            if (lms.num == 0) {
                lm.LockMap.remove(key);
            }
        }

        synchronized (this) {
            if (lm.LockMap.size() == 0) {
                lockMaps.remove(cId);
            }
        }

        if(isTracker) {
            lockTracker.UnTrack(new LockTracker.
                    PointLockRequest(cId, key, null, readOnly, exclusive));
        }
        lock.unlock();

        try {
            lms.notifyAll();
        }catch (IllegalMonitorStateException e){
//            e.printStackTrace();
        }


    }

    @Override
    public LockTracker.PointLockStatus GetPointLockStatus(ColumnFamilyId cId, Key key) {
        return lockTracker.GetPointLockStatus(cId, key);
    }

    private boolean dealLockCheck(long id) {
        long tId = id;
        CFKey waitKey = waitInfo.get(id);

        tId = useKey.get(waitKey);

        while (true) {
            if ((waitKey = waitInfo.get(tId)) == null) return false;
            if ((tId = useKey.get(waitKey)) == id) return true;
        }
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
