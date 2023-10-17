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
package net.openio.opendb.db;

import net.openio.opendb.model.SequenceNumber;

import java.util.concurrent.atomic.AtomicBoolean;

public class SnapshotManager {

  private final Snapshot head = new Snapshot();

  private Snapshot tail = head;

  private AtomicBoolean addState = new AtomicBoolean(false);

  public Snapshot addSnapshot(SequenceNumber sequenceNumber) {
    Snapshot snapshot = new Snapshot(sequenceNumber);
    while (addState.compareAndSet(false, true)) {

    }
    Snapshot h = tail;
    while (h != head) {
      if (h.getSequenceNumber().compareTo(snapshot.getSequenceNumber()) <= 0) {
        break;
      }
      h = h.pre;
    }

    if (h.next != null) {
      snapshot.next = h.next;
      h.next.pre = snapshot;
    } else {
      tail = h;
    }
    h.next = snapshot;
    snapshot.pre = h;
    addState.set(false);
    return snapshot;
  }

  public void removeSnapshot(Snapshot snapshot) {
    while (addState.compareAndSet(false, true)) {

    }

    if (snapshot.next != null) {
      snapshot.pre.next = snapshot.next;
      snapshot.next.pre = snapshot.pre;
    }
    addState.set(false);
  }

  public SequenceNumber getMinSnapshot() {
    while (addState.compareAndSet(false, true)) {

    }
    SequenceNumber sequenceNumber = null;
    if (head.next != null) {
      sequenceNumber = head.next.getSequenceNumber();
    }
    addState.set(false);
    return sequenceNumber;
  }
}
