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
package net.openio.opendb.tool.codec.meta;

import io.netty.buffer.ByteBuf;
import net.openio.opendb.storage.metadata.Level;
import net.openio.opendb.storage.sstable.SSTable;
import net.openio.opendb.tool.codec.Codec;


public class LevelProtoCodec {

  public static final int LEVEL_NUM = 1;
  public static final int LEVEL_TAG = 8;
  public static final int LEVEL_TAG_ENCODE_SIZE = 1;


  public static final int TOTALSIZE_NUM = 2;
  public static final int TOTALSIZE_TAG = 16;
  public static final int TOTALSIZE_TAG_ENCODE_SIZE = 1;


  public static final int SSTABLES_NUM = 3;
  public static final int SSTABLES_TAG = 26;
  public static final int SSTABLES_TAG_ENCODE_SIZE = 1;


  private static void encodeLevel(ByteBuf buf, Level level) {
    Codec.encodeVarInt32(buf, LEVEL_TAG);
    Codec.encodeVarInt32(buf, level.getLevel());
  }

  private static void decodeLevel(ByteBuf buf, Level level) {
    level.setLevel(Codec.decodeVarInt32(buf));
  }

  private static void encodeTotalSize(ByteBuf buf, Level level) {
    Codec.encodeVarInt32(buf, TOTALSIZE_TAG);
    Codec.encodeVarInt64(buf, level.getTotalSize());
  }

  private static void decodeTotalSize(ByteBuf buf, Level level) {
    level.setTotalSize(Codec.decodeVarInt64(buf));
  }

  private static void decodeSSTables(ByteBuf buf, Level level) {
    level.addSsTables(SSTableProtoCodec.decode(buf, Codec.decodeVarInt32(buf)));
  }

  private static void encodeSSTables(ByteBuf buf, Level level) {
    for (SSTable value : level.getSsTables()) {
      Codec.encodeVarInt32(buf, SSTABLES_TAG);
      Codec.encodeVarInt32(buf, SSTableProtoCodec.getByteSize(value));
      SSTableProtoCodec.encode(buf, value);
    }
  }

  public static void encode(ByteBuf buf, Level level) {

    encodeLevel(buf, level);


    encodeSSTables(buf, level);

    encodeTotalSize(buf, level);


  }

  public static Level decode(ByteBuf buf, int length) {
    Level value = new Level();
    int end = buf.readerIndex() + length;
    while (buf.readerIndex() < end) {
      int num = Codec.decodeVarInt32(buf);
      switch (num) {
        case LEVEL_TAG:
          decodeLevel(buf, value);
          break;
        case TOTALSIZE_TAG:
          decodeTotalSize(buf, value);
          break;
        case SSTABLES_TAG:
          decodeSSTables(buf, value);
          break;
        default:
          Codec.skipUnknownField(num, buf);
      }
    }
    return value;
  }

  public static int getByteSize(Level level) {

    int length = LEVEL_TAG_ENCODE_SIZE;
    length += Codec.computeVarInt32Size(level.getLevel());

    length += TOTALSIZE_TAG_ENCODE_SIZE;
    length += Codec.computeVarInt64Size(level.getTotalSize());

    length += SSTABLES_TAG_ENCODE_SIZE * level.getSsTables().size();
    int i = 0;
    for (SSTable value : level.getSsTables()) {
      i = SSTableProtoCodec.getByteSize(value);
      length += Codec.computeVarInt32Size(i) + i;
    }
    return length;
  }
}
