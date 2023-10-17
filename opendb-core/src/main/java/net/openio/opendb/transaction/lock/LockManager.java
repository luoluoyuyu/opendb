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

interface LockManager {

  // Whether supports locking a specific key.
  boolean isPointLockSupported();


  void tryLock(Long cId, Key key, boolean exclusive);

  void tracker(Long cId, Key key, SequenceNumber sequenceNumber);

  void unLock(Long cId, Key key);


  SequenceNumber tryLock(long tId, Long cId, Key key, boolean exclusive, boolean readOnly);

  void unLock(long tid, Long cId, Key key);


  void tracker(long tid, Long cId, Key key, SequenceNumber sequenceNumber);


  boolean validate(Long cId, Key key, SequenceNumber sequenceNumber);


}
