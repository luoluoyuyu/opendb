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
