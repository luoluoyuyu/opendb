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


public class TrackedKeyInfo {

  volatile SequenceNumber sequenceNumber;

  volatile int writeNum;

  volatile int readNum;

  volatile boolean exclusive;

  public TrackedKeyInfo(SequenceNumber sequenceNumber, int writeNum, int readNum, boolean exclusive) {
    this.sequenceNumber = sequenceNumber;
    this.writeNum = writeNum;
    this.readNum = readNum;
    this.exclusive = exclusive;
  }

  public TrackedKeyInfo() {
  }

  public SequenceNumber getSequenceNumber() {
    return sequenceNumber;
  }

  public void setSequenceNumber(SequenceNumber sequenceNumber) {
    this.sequenceNumber = sequenceNumber;
  }

  public int getWriteNum() {
    return writeNum;
  }

  public void setWriteNum(int writeNum) {
    this.writeNum = writeNum;
  }

  public int getReadNum() {
    return readNum;
  }

  public void setReadNum(int readNum) {
    this.readNum = readNum;
  }

  public boolean isExclusive() {
    return exclusive;
  }

  public void setExclusive(boolean exclusive) {
    this.exclusive = exclusive;
  }
}
