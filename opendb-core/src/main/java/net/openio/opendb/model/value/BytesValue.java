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
package net.openio.opendb.model.value;


import net.openio.opendb.model.OperationType;

import java.util.Arrays;


public class BytesValue implements Value {

  private byte[] value;

  private OperationType type;

  @Override
  public Object getValue() {
    return value;
  }

  @Override
  public void setValue(Object value) {
    this.value = (byte[]) value;
  }

  @Override
  public Value copy() {
    return new BytesValue(Arrays.copyOf(value, value.length), type);
  }

  @Override
  public OperationType getType() {
    return type;
  }

  @Override
  public void setType(OperationType type) {
    this.type = type;
  }

  public BytesValue(byte[] value, OperationType type) {
    this.value = value;
    this.type = type;
  }

  public BytesValue(){


  }
}

