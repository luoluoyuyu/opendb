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


public class StringKey implements Key {

  private String value;
  private SequenceNumber sequenceNumber;

  public StringKey(String value) {
    this.value = value;
  }

  public StringKey(String value, SequenceNumber sequenceNumber) {
    this.value = value;
    this.sequenceNumber = sequenceNumber;
  }

  @Override
  public Object getKey() {
    return value;
  }

  @Override
  public SequenceNumber getSequenceNumber() {
    return sequenceNumber;
  }

  @Override
  public void setKey(Object key) {
    this.value = (String) key;
  }

  @Override
  public void setKey(Object value, SequenceNumber sequenceNumber) {
    this.value = (String) value;
    this.sequenceNumber = sequenceNumber;
  }

  @Override
  public Key copy() {
    return new StringKey(value, sequenceNumber);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    StringKey stringKey = (StringKey) o;
    return Objects.equals(value, stringKey.value);
  }

  @Override
  public int hashCode() {
    return Hash.hash32(value.getBytes(), 0, value.length());
  }

  @Override
  public void setSequenceNumber(SequenceNumber sequenceNumber) {
    this.sequenceNumber = sequenceNumber;
  }

  @Override
  public int compareTo(Key o) {
    if (o == null) {
      throw new RuntimeException("o is null");
    }
    if (!(o instanceof StringKey)) {
      throw new RuntimeException("o is not StringKey Type");
    }
    StringKey otherKey = (StringKey) o;
    return otherKey.value.compareTo(value);
  }

  public StringKey() {

  }
}
