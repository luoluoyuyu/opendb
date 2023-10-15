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
package net.openio.opendb.model.key;


import net.openio.opendb.model.SequenceNumber;
import net.openio.opendb.tool.Hash;

import java.util.Objects;


public class IntKey implements Key {

  private Integer key;

  private SequenceNumber sequenceNumber;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    return Objects.equals(key, ((IntKey) o).key);
  }

  @Override
  public int hashCode() {
    return Hash.intHash(key);
  }

  @Override
  public Object getKey() {
    return key;
  }

  @Override
  public SequenceNumber getSequenceNumber() {
    return sequenceNumber;
  }

  @Override
  public void setKey(Object key) {
    this.key = (Integer) key;
  }

  @Override
  public void setKey(Object value, SequenceNumber sequenceNumber) {
    key = (Integer) value;
    this.sequenceNumber = sequenceNumber;
  }

  @Override
  public Key copy() {
    return new IntKey(key, sequenceNumber);
  }

  @Override
  public int compareTo(Key o) {
    if (o == null) {
      throw new RuntimeException("o is null");
    }
    if (!(o instanceof IntKey)) {
      throw new RuntimeException("o is not IntKey Type");
    }
    int otherKey = ((IntKey) o).key;
    return Integer.compare(this.key, otherKey);
  }

  @Override
  public void setSequenceNumber(SequenceNumber sequenceNumber) {
    this.sequenceNumber = sequenceNumber;
  }

  public IntKey(Integer key) {
    this.key = key;
  }

  public IntKey(Integer key, SequenceNumber sequenceNumber) {
    this.key = key;
    this.sequenceNumber = sequenceNumber;
  }

  public IntKey(){

  }
}
