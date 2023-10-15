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
package net.openio.opendb.tool.codec.log;

import io.netty.buffer.ByteBuf;
import net.openio.opendb.storage.wal.LogFileHead;
import net.openio.opendb.tool.codec.Codec;

public class FileHeadProtoCodec {

  public static final int LENGTH_NUM = 1;
  public static final int LENGTH_TAG = 8;
  public static final int LENGTH_TAG_ENCODE_SIZE = 1;

  public static final int CREATE_TIME_NUM = 2;
  public static final int CREATE_TIME_TAG = 16;
  public static final int CREATE_TIME_TAG_ENCODE_SIZE = 1;

  public static final int BLOCK_END_SEEK_NUM = 3;
  public static final int BLOCK_END_SEEK_TAG = 24;
  public static final int BLOCK_END_SEEK_TAG_ENCODE_SIZE = 1;


  private static void encodeLength(ByteBuf buf, LogFileHead logFileHead) {
    Codec.encodeVarInt32(buf, LENGTH_TAG);
    Codec.encodeVarInt32(buf, logFileHead.getLength());
  }

  private static void decodeLength(ByteBuf buf, LogFileHead logFileHead) {
    logFileHead.setLength(Codec.decodeVarInt32(buf));
  }

  private static void encodeCreateTime(ByteBuf buf, LogFileHead logFileHead) {
    Codec.encodeVarInt32(buf, CREATE_TIME_TAG);
    Codec.encodeVarInt64(buf, logFileHead.getCreateTime());
  }

  private static void decodeCreateTime(ByteBuf buf, LogFileHead logFileHead) {
    logFileHead.setCreateTime(Codec.decodeVarInt64(buf));
  }

  private static void decodeBlockEndSeek(ByteBuf buf, LogFileHead logFileHead) {
    logFileHead.add(Codec.decodeVarInt32(buf));
  }

  private static void encodeBlockEndSeek(ByteBuf buf, LogFileHead logFileHead) {
    for (Integer value : logFileHead.getBlockEndSeek()) {
      Codec.encodeVarInt32(buf, BLOCK_END_SEEK_TAG);
      Codec.encodeVarInt32(buf, value);
    }
  }

  public static LogFileHead decode(ByteBuf buf) {
    LogFileHead value = new LogFileHead();
    while (buf.readerIndex() < buf.writerIndex()) {
      int num = Codec.decodeVarInt32(buf);
      switch (num) {
        case LENGTH_TAG:
          decodeLength(buf, value);
          break;
        case CREATE_TIME_TAG:
          decodeCreateTime(buf, value);
          break;
        case BLOCK_END_SEEK_TAG:
          decodeBlockEndSeek(buf, value);
          break;
        default:
          return value;
      }
    }
    return value;
  }

  public static void encode(ByteBuf buf, LogFileHead logFileHead) {
    encodeLength(buf, logFileHead);
    encodeCreateTime(buf, logFileHead);
    encodeBlockEndSeek(buf, logFileHead);
    if (buf.writerIndex() < buf.capacity()) {
      buf.writeByte(0);
    }
  }


  public static LogFileHead decode(ByteBuf buf, int length) {
    LogFileHead value = new LogFileHead();
    int end = buf.readerIndex() + length;
    while (buf.readerIndex() < end) {
      int num = Codec.decodeVarInt32(buf);
      switch (num) {
        case LENGTH_TAG:
          decodeLength(buf, value);
          break;
        case CREATE_TIME_TAG:
          decodeCreateTime(buf, value);
          break;
        case BLOCK_END_SEEK_TAG:
          decodeBlockEndSeek(buf, value);
          break;
        default:
          Codec.skipUnknownField(num, buf);
      }
    }
    return value;
  }

  public static int getByteSize(LogFileHead logFileHead) {
    int length = LENGTH_TAG_ENCODE_SIZE;
    length += Codec.computeVarInt32Size(logFileHead.getLength());

    length += CREATE_TIME_TAG_ENCODE_SIZE;
    length += Codec.computeVarInt64Size(logFileHead.getCreateTime());

    length += BLOCK_END_SEEK_TAG_ENCODE_SIZE * logFileHead.getBlockEndSeek().size();
    for (Integer value : logFileHead.getBlockEndSeek()) {
      length += Codec.computeVarInt32Size(value);
    }
    return length;
  }

}
