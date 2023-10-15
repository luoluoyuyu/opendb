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
import net.openio.opendb.model.key.Key;

import java.util.List;

public class BloomFilter {

  private byte[] data;


  public BloomFilter() {
    data = new byte[1024];

  }

  public BloomFilter(int length) {
    length = roundUpToPowerOf2(length);
    if (length <= 0) {
      throw new RuntimeException("bloomFiler array size mem out ");
    }
    data = new byte[length];
  }


  private int roundUpToPowerOf2(int x) {
    x--;
    x |= x >> 1;
    x |= x >> 2;
    x |= x >> 4;
    x |= x >> 8;
    x |= x >> 16;
    x++;
    return x;
  }


  public void add(Key k) {

    int index = getIndex(k.hashCode());

    data[index] = 1;
  }

  public void add(List<KeyValueEntry> k) {
    for (KeyValueEntry key : k) {
      int index = getIndex(key.getKey().hashCode());
      data[index] = 1;
    }
  }

  public boolean get(Key key) {
    int index = getIndex(key.hashCode());
    return data[index] != 0;
  }

  public byte[] getData() {
    return data;
  }

  public void setData(byte[] data) {
    this.data = data;
  }


  private int getIndex(int hash) {
    return hash & (data.length - 1);
  }


}

