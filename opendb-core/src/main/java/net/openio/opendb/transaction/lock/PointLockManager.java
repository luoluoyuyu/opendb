/**
 * Licensed to the OpenIO.Net under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.openio.opendb.transaction.lock;

import net.openio.opendb.model.SequenceNumber;
import net.openio.opendb.model.key.Key;

import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;

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


  @Override
  public SequenceNumber tryLock(long tId, Long cId, Key key, boolean exclusive
    , boolean readOnly, SequenceNumber sequenceNumber, boolean isTracker) {
    LockMap lm = null;
    synchronized (this) {
      lm = lockMaps.get(cId);
      if (lm == null) {
        lockMaps.put(cId, lm = new LockMap());

      }
    }
    LockMapStripes lms = null;
    synchronized (lm) {
      lms = lm.lockMap.get(key);
      if (lms == null) {
        lm.lockMap.put(key, lms = new LockMapStripes());
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

    if (isTracker) {
      return lockTracker.track(new LockTracker.PointLockRequest(cId, key, sequenceNumber, readOnly, exclusive));
    }

    return lockTracker.getPointLockStatus(cId, key).seq;

  }

  @Override
  public void unLock(long lid, Long cId, Key key, boolean exclusive, boolean readOnly, boolean isTracker) {
    LockMap lm = lockMaps.get(cId);
    if (lm == null) {
      throw new RuntimeException("LockManager UNLock:LockMap is null");
    }

    LockMapStripes lms = lm.lockMap.get(key);

    if (lms == null) {
      throw new RuntimeException("LockManager UNLock:LockMapStripes is null");
    }

    Lock lock = exclusive ? lms.reentrantReadWriteLock.writeLock()
      : lms.reentrantReadWriteLock.readLock();


    useKey.remove(new CFKey(cId, key));

    lms.delete(lid);
    synchronized (lm) {
      if (lms.num == 0) {
        lm.lockMap.remove(key);
      }
    }

    synchronized (this) {
      if (lm.lockMap.size() == 0) {
        lockMaps.remove(cId);
      }
    }

    if (isTracker) {
      lockTracker.unTrack(new LockTracker.
        PointLockRequest(cId, key, null, readOnly, exclusive));
    }
    lock.unlock();

    try {
      lms.notifyAll();
    } catch (IllegalMonitorStateException e) {
//            e.printStackTrace();
    }


  }

  @Override
  public LockTracker.PointLockStatus getPointLockStatus(Long cId, Key key) {
    return lockTracker.getPointLockStatus(cId, key);
  }

  private boolean dealLockCheck(long id) {
    long tId = id;
    CFKey waitKey = waitInfo.get(id);

    tId = useKey.get(waitKey);

    while (true) {
      if ((waitKey = waitInfo.get(tId)) == null) {
        return false;
      }
      if ((tId = useKey.get(waitKey)) == id) {
        return true;
      }
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
