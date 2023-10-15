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

import net.openio.opendb.log.Log;
import net.openio.opendb.log.WalLog;
import net.openio.opendb.model.SequenceNumber;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class WriteBatch {

  private volatile long maxTime;

  private final AtomicBoolean storage = new AtomicBoolean();

  private final AtomicBoolean addState = new AtomicBoolean();

  private final Node head = new Node();

  private volatile Node tail = head;

  private final List<Log> logs = new LinkedList<>();

  volatile long writeSequenceNumber;


  public void addLog(WalLog walLog) {
    Node node = new Node();

    while (addState.compareAndSet(false, true)) {

    }
    SequenceNumber sequenceNumber = new SequenceNumber(++maxTime);
    walLog.getKey().setSequenceNumber(sequenceNumber);
    tail.next = node;
    node.pre = tail;
    tail = node;
    logs.add(walLog);
    int i = logs.size();
    addState.set(false);
    while (tail.next == null) {
      if (!storage.compareAndSet(false, true)) {
        continue;
      }
      List<Log> logs = this.logs.subList(0, i);
      writeSequenceNumber = sequenceNumber.getTimes();
      step(new LinkedList<>(logs));
      node.pre.next = null;
      head.next = node.next;
      logs.clear();
      storage.set(false);
      return;
    }
    waitForNextNode(node);
  }

  abstract void step(List<Log> list);


  abstract void waitForNextNode(Node snapshot);

  static class Node {
    volatile Node next;

    volatile Node pre;
  }

}
