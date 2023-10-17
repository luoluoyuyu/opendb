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
package net.openio.opendb.log;


import net.openio.opendb.model.SequenceNumber;
import net.openio.opendb.model.key.Key;
import net.openio.opendb.model.key.KeyType;
import net.openio.opendb.model.value.Value;
import net.openio.opendb.model.value.ValueType;


public class WalLog implements Log{

  private long columnId;

  private Key key;

  private Value value;

  private KeyType keyType;

  private ValueType valueType;

  private long transactionId;

  private SequenceNumber sequenceNumber;

  public WalLog() {
  }

  public WalLog(long columnId, Key key, Value value, KeyType keyType, ValueType valueType) {
    this.columnId = columnId;
    this.key = key;
    this.value = value;
    this.keyType = keyType;
    this.valueType = valueType;
  }

  @Override
  public int compare(SequenceNumber sequenceNumber) {
    return this.sequenceNumber.compareTo(sequenceNumber);
  }

  public long getColumnId() {
    return columnId;
  }

  public void setColumnId(long columnId) {
    this.columnId = columnId;
  }

  public Key getKey() {
    return key;
  }

  public void setKey(Key key) {
    this.key = key;
  }

  public Value getValue() {
    return value;
  }

  public void setValue(Value value) {
    this.value = value;
  }

  public KeyType getKeyType() {
    return keyType;
  }

  public void setKeyType(KeyType keyType) {
    this.keyType = keyType;
  }

  public ValueType getValueType() {
    return valueType;
  }

  public void setValueType(ValueType valueType) {
    this.valueType = valueType;
  }

  @Override
  public long getTransactionId() {
    return transactionId;
  }

  public void setTransactionId(long transactionId) {
    this.transactionId = transactionId;
  }

  public SequenceNumber getSequenceNumber() {
    return sequenceNumber;
  }

  public void setSequenceNumber(SequenceNumber sequenceNumber) {
    this.sequenceNumber = sequenceNumber;
  }
}
