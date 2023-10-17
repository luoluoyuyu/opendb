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
package net.openio.opendb.tool.codec.value;

import io.netty.buffer.ByteBuf;
import net.openio.opendb.model.OperationType;
import net.openio.opendb.model.value.FloatValue;
import net.openio.opendb.model.value.Value;
import net.openio.opendb.tool.codec.Codec;


public class FloatValueProtoCodec implements ValueProtoCodec {

  public static final int VALUE_NUM = 1;
  public static final int VALUE_TAG = 13;
  public static final int VALUE_TAG_ENCODE_SIZE = 1;


  public static final int TYPE_NUM = 2;
  public static final int TYPE_TAG = 16;
  public static final int TYPE_TAG_ENCODE_SIZE = 1;

  private void encodeValue(ByteBuf buf, Value value) {
    Codec.encodeVarInt32(buf, VALUE_TAG);
    Codec.encodeFloat(buf, (float) value.getValue());
  }

  private void decodeValue(ByteBuf buf, Value value) {
    value.setValue(Codec.decodeFloat(buf));
  }

  private void encodeType(ByteBuf buf, Value value) {
    Codec.encodeVarInt32(buf, TYPE_TAG);
    Codec.encodeVarInt32(buf, value.getType().getNum());
  }

  private void decodeType(ByteBuf buf, Value value) {
    value.setType(OperationType.get(Codec.decodeVarInt32(buf)));
  }

  @Override
  public Value decode(ByteBuf buf, int length) {
    FloatValue value = new FloatValue();
    int end = buf.readerIndex() + length;
    while (buf.readerIndex() < end) {
      int num = Codec.decodeVarInt32(buf);
      switch (num) {
        case VALUE_TAG:
          decodeValue(buf, value);
          break;
        case TYPE_TAG:
          decodeType(buf, value);
          break;
        default:
          Codec.skipUnknownField(num, buf);
      }
    }

    return value;
  }

  @Override
  public void encode(ByteBuf buf, Value value) {

    this.encodeValue(buf, value);

    this.encodeType(buf, value);
  }

  @Override
  public int getByteSize(Value value) {
    int length = VALUE_TAG_ENCODE_SIZE;
    length += 4;
    length += TYPE_TAG_ENCODE_SIZE;
    length += Codec.computeVarInt32Size(value.getType().getNum());
    return length;
  }
}
