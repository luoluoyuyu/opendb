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

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class LockMapStripes {

  ReentrantReadWriteLock reentrantReadWriteLock;

  volatile List<LockInfo> infos;

  volatile int num = 0;

  public LockMapStripes() {
    reentrantReadWriteLock = new ReentrantReadWriteLock();
    infos = new LinkedList<>();
  }

  public void add(LockInfo lockInfo) {
    synchronized (this) {
      infos.add(lockInfo);
      num++;
    }
  }

  public boolean delete(long tId) {
    if (num <= 0) {
      throw new RuntimeException("num is zero");
    }
    synchronized (this) {
      for (int i = 0; i < infos.size(); i++) {
        if (infos.get(i).transactionId == tId) {
          infos.remove(i);
          num--;
          return true;
        }
      }
    }

    return false;
  }

  public LockInfo get(long tId) {
    if (num <= 0) {
      throw new RuntimeException("num is zero");
    }
    LockInfo lockInfo = null;
    synchronized (this) {
      for (LockInfo info : infos) {
        if (info.transactionId == tId) {
          return info;
        }
      }
    }

    return null;
  }

}
