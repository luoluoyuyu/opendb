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
package net.openio.opendb.mem;


import net.openio.opendb.db.KeyValueEntry;
import net.openio.opendb.model.SequenceNumber;
import net.openio.opendb.model.key.Key;
import net.openio.opendb.model.value.Value;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;


public class MemTable {

  private final MemTableRep memTableRep;

  private final BloomFilter bloomFilter;

  private final AtomicInteger count = new AtomicInteger(0);

  public final AtomicBoolean canWrite = new AtomicBoolean(true);

  public final AtomicInteger size = new AtomicInteger(0);

  public boolean isCanWrite() {
    return canWrite.get();
  }

  public SequenceNumber min = new SequenceNumber(Long.MAX_VALUE);

  public boolean flush() {
    if (!canWrite.compareAndSet(true, false)) {
      return false;
    }
    while (!count.compareAndSet(0, -1)) {

    }
    return true;
  }

  public Value get(Key key, Comparator<Key> comparator) {
    if (!bloomFilter.get(key)) {
      return null;
    }
    return memTableRep.getValue(key, comparator);
  }

  public boolean put(KeyValueEntry key, int size) {
    this.size.addAndGet(size);
    if (key.getKey().getSequenceNumber().compareTo(min) < 0) {
      min = key.getKey().getSequenceNumber();
    }
    memTableRep.addKeyValue(key);

    bloomFilter.add(key.getKey());

    return true;
  }

  public boolean put(List<KeyValueEntry> key) {

    if (increaseCount()) {
      return false;
    }

    memTableRep.addKeyValue(key);

    bloomFilter.add(key);

    declineCount();

    return true;
  }


  public boolean increaseCount() {
    int count = 0;
    do {
      count = this.count.get();
      if (count < 0) {
        return false;
      }
    } while (!this.count.compareAndSet(count, count + 1));
    return true;
  }


  public void declineCount() {
    int count = 0;
    do {
      count = this.count.get();
    } while (!this.count.compareAndSet(count, count - 1));
  }


  public MemTable(MemTableRep memTableRep, BloomFilter bloomFilter) {
    this.bloomFilter = bloomFilter;
    this.memTableRep = memTableRep;
  }

  public MemTableRep getMemTableRep() {
    return memTableRep;
  }

  public BloomFilter getBloomFilter() {
    return bloomFilter;
  }

  public SequenceNumber getMin() {
    return min;
  }
}
