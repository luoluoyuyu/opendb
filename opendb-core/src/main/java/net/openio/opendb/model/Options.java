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

  public int allMemTableMaxSize = 1 << 20;

  public int storageMemArenaSize = 1 << 25;

  public int ssTableSize = 1 << 25;

  public int bufferSize = 1 << 20;

  public boolean asyLog = true;

  public int logfileSize = 1 << 30;

  public int logBlockSize = 1 << 15;

  public int logMemArenaSize = 1 << 20;

  public int walLogHeadSize = 1 << 14;

  public long lruExpireTime = 300000; //ms

  public int lruMaxCapacity = 1 << 20;

  public long lruOldBlocksTime = 1000; //ms

  public long lruScanTime = 1000; //ms

  public long compactionTime = 10; //s

  public long flushTime = 1; //s

  public long level0CompactionSize = 1 << 28;

  public int sizeTieredLevel = 1;

  public int maxLevel = 8;

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

  public int getSizeTieredLevel() {
    return sizeTieredLevel;
  }

  public void setSizeTieredLevel(int sizeTieredLevel) {
    this.sizeTieredLevel = sizeTieredLevel;
  }

  public int getMaxLevel() {
    return maxLevel;
  }

  public void setMaxLevel(int maxLevel) {
    this.maxLevel = maxLevel;
  }

  public int getAllMemTableMaxSize() {
    return allMemTableMaxSize;
  }

  public void setAllMemTableMaxSize(int allMemTableMaxSize) {
    this.allMemTableMaxSize = allMemTableMaxSize;
  }

  public int getSsTableSize() {
    return ssTableSize;
  }

  public void setSsTableSize(int ssTableSize) {
    this.ssTableSize = ssTableSize;
  }

  public int getLogBlockSize() {
    return logBlockSize;
  }

  public void setLogBlockSize(int logBlockSize) {
    this.logBlockSize = logBlockSize;
  }

  public int getLogMemArenaSize() {
    return logMemArenaSize;
  }

  public void setLogMemArenaSize(int logMemArenaSize) {
    this.logMemArenaSize = logMemArenaSize;
  }

  public long getFlushTime() {
    return flushTime;
  }

  public void setFlushTime(long flushTime) {
    this.flushTime = flushTime;
  }

  public boolean isAsyLog() {
    return asyLog;
  }

  public void setAsyLog(boolean asyLog) {
    this.asyLog = asyLog;
  }

  public int getWalLogHeadSize() {
    return walLogHeadSize;
  }

  public void setWalLogHeadSize(int walLogHeadSize) {
    this.walLogHeadSize = walLogHeadSize;
  }


  public enum TransactionType {
    readCommit,
    repeatableRead,
  }

}
