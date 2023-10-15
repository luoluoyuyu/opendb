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
package net.openio.opendb.model;

public class Options {

  public int bufferSize = 1 << 20;

  public int logfileSize = 1 << 30;

  public int storageMemArenaSize = 1 << 25;

  public long lruExpireTime = 300000;

  public int lruMaxCapacity = 1 << 20;

  public long lruOldBlocksTime = 1000;

  public long lruScanTime = 1000;

  public long compactionTime = 150000;

  public long level0CompactionSize = 0;

  public TransactionType transactionType = TransactionType.repeatableRead;

  public int getBufferSize() {
    return bufferSize;
  }

  public void setBufferSize(int bufferSize) {
    this.bufferSize = bufferSize;
  }

  public int getLogfileSize() {
    return logfileSize;
  }

  public void setLogfileSize(int logfileSize) {
    this.logfileSize = logfileSize;
  }

  public int getStorageMemArenaSize() {
    return storageMemArenaSize;
  }

  public void setStorageMemArenaSize(int storageMemArenaSize) {
    this.storageMemArenaSize = storageMemArenaSize;
  }

  public long getLruExpireTime() {
    return lruExpireTime;
  }

  public void setLruExpireTime(long lruExpireTime) {
    this.lruExpireTime = lruExpireTime;
  }

  public int getLruMaxCapacity() {
    return lruMaxCapacity;
  }

  public void setLruMaxCapacity(int lruMaxCapacity) {
    this.lruMaxCapacity = lruMaxCapacity;
  }

  public long getLruOldBlocksTime() {
    return lruOldBlocksTime;
  }

  public void setLruOldBlocksTime(long lruOldBlocksTime) {
    this.lruOldBlocksTime = lruOldBlocksTime;
  }

  public long getLruScanTime() {
    return lruScanTime;
  }

  public void setLruScanTime(long lruScanTime) {
    this.lruScanTime = lruScanTime;
  }

  public long getCompactionTime() {
    return compactionTime;
  }

  public void setCompactionTime(long compactionTime) {
    this.compactionTime = compactionTime;
  }

  public long getLevel0CompactionSize() {
    return level0CompactionSize;
  }

  public void setLevel0CompactionSize(long level0CompactionSize) {
    this.level0CompactionSize = level0CompactionSize;
  }

  public TransactionType getTransactionType() {
    return transactionType;
  }

  public void setTransactionType(TransactionType transactionType) {
    this.transactionType = transactionType;
  }


  public enum TransactionType {
    readCommit,
    repeatableRead,
  }

}
