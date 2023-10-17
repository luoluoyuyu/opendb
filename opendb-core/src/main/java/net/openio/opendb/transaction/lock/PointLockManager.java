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
