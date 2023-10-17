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
package net.openio.opendb.tool.codec.sstable;

import io.netty.buffer.ByteBuf;
import net.openio.opendb.model.SequenceNumber;
import net.openio.opendb.tool.codec.Codec;


public class SequenceNumberProtoCodec {


  public static final int TIMES_NUM = 1;
  public static final int TIMES_TAG = 8;
  public static final int TIMES_TAG_ENCODE_SIZE = 1;

  public static SequenceNumber decode(ByteBuf buf, int length) {
    SequenceNumber sequenceNumber = new SequenceNumber();
    int fIndex = buf.readerIndex();
    while (buf.readerIndex() < fIndex + length) {
      int num = Codec.decodeVarInt32(buf);
      switch (num) {
        case TIMES_TAG:
          decodeTimes(buf, sequenceNumber);
          break;
        default:
          Codec.skipUnknownField(num, buf);
      }
    }
    return sequenceNumber;
  }

  public static int getByteSize(SequenceNumber sequenceNumber) {
    return TIMES_TAG_ENCODE_SIZE + Codec
      .computeVarInt64Size(Codec.encodeZigzag64(sequenceNumber.getTimes()));
  }

  private static void decodeTimes(ByteBuf buf, SequenceNumber sequenceNumber) {
    Long value = Codec.decodeZigzag64(Codec.decodeVarInt64(buf));
    sequenceNumber.setTimes(value);
  }


  public static void encode(ByteBuf buf, SequenceNumber sequenceNumber) {
    Codec.encodeVarInt32(buf, TIMES_TAG);
    Codec.encodeVarInt64(buf, Codec.encodeZigzag64(sequenceNumber.getTimes()));
  }


}
