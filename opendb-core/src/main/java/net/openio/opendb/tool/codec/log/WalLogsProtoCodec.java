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
import net.openio.opendb.log.Log;
import net.openio.opendb.log.WalLog;
import net.openio.opendb.model.key.KeyType;
import net.openio.opendb.tool.codec.Codec;
import net.openio.opendb.tool.codec.sstable.SequenceNumberProtoCodec;

import java.util.LinkedList;
import java.util.List;

public class WalLogsProtoCodec implements LogProtoCodec {

  public static final int WAL_LOG_NUM = 1;
  public static final int WAL_LOG_TAG = 10;
  public static final int WAL_LOG_TAG_ENCODE_SIZE = 1;


  private static void decodeWalLog(ByteBuf buf, List<Log> list) {

    list.add(WalLogProtoCodec.decode(buf, Codec.decodeVarInt32(buf)));
  }

  private static void encodeWalLog(ByteBuf buf, WalLog walLog) {
    Codec.encodeVarInt32(buf, WAL_LOG_TAG);
    Codec.encodeVarInt32(buf, WalLogProtoCodec.getByteSize(walLog));
    WalLogProtoCodec.encode(buf, walLog);
  }


  @Override
  public List<Log> decode(ByteBuf buf, int length) {
    List<Log> value = new LinkedList<>();
    int end = buf.readerIndex() + length;
    while (buf.readerIndex() < end) {
      int num = Codec.decodeVarInt32(buf);
      switch (num) {
        case WAL_LOG_TAG:
          decodeWalLog(buf, value);
          break;
        default:
          Codec.skipUnknownField(num, buf);
      }
    }
    return value;
  }

  @Override
  public void encode(ByteBuf buf, Log log) {
    WalLogProtoCodec.encode(buf, (WalLog) log);
  }

  @Override
  public void encode(ByteBuf buf, List<Log> logs) {
    for (Log log : logs) {
      WalLogProtoCodec.encode(buf, (WalLog) log);
    }
  }

  @Override
  public int getByteSize(Log log) {
    int l = WalLogProtoCodec.getByteSize((WalLog) log);
    return WAL_LOG_TAG_ENCODE_SIZE + l + Codec.computeVarInt32Size(l);
  }

  @Override
  public int getByteSize(List<Log> log) {
    int length = WAL_LOG_TAG_ENCODE_SIZE * log.size();
    for (Log log1 : log) {
      int l = WalLogProtoCodec.getByteSize((WalLog) log1);
      length += (l + Codec.computeVarInt32Size(l));
    }
    return length;
  }

  static class WalLogProtoCodec {
    public static final int COLUMN_ID_NUM = 1;
    public static final int COLUMN_ID_TAG = 8;
    public static final int COLUMN_ID_TAG_ENCODE_SIZE = 1;

    public static final int KEY_NUM = 2;
    public static final int KEY_TAG = 18;
    public static final int KEY_TAG_ENCODE_SIZE = 1;

    public static final int VALUE_NUM = 3;
    public static final int VALUE_TAG = 26;
    public static final int VALUE_TAG_ENCODE_SIZE = 1;

    public static final int KEY_TYPE_NUM = 4;
    public static final int KEY_TYPE_TAG = 32;
    public static final int KEY_TYPE_TAG_ENCODE_SIZE = 1;


    public static final int VALUE_TYPE_NUM = 5;
    public static final int VALUE_TYPE_TAG = 40;
    public static final int VALUE_TYPE_TAG_ENCODE_SIZE = 1;

    public static final int TRANSACTION_ID_NUM = 6;
    public static final int TRANSACTION_ID_TAG = 48;
    public static final int TRANSACTION_ID_TAG_ENCODE_SIZE = 1;

    public static final int SEQUENCE_NUMBER_NUM = 7;
    public static final int SEQUENCE_NUMBER_TAG = 58;
    public static final int SEQUENCE_NUMBER_TAG_ENCODE_SIZE = 1;


    private static void decodeSequenceNumber(ByteBuf buf, WalLog walLog) {
      walLog.setSequenceNumber(SequenceNumberProtoCodec.decode(buf, Codec.decodeVarInt32(buf)));
    }

    private static void encodeSequenceNumber(ByteBuf buf, WalLog walLog) {
      Codec.encodeVarInt32(buf, SEQUENCE_NUMBER_TAG);
      Codec.encodeVarInt32(buf, SequenceNumberProtoCodec.getByteSize(walLog.getSequenceNumber()));
      SequenceNumberProtoCodec.encode(buf, walLog.getSequenceNumber());
    }

    private static void encodeTransactionId(ByteBuf buf, WalLog walLog) {
      Codec.encodeVarInt32(buf, TRANSACTION_ID_TAG);
      Codec.encodeVarInt64(buf, walLog.getTransactionId());
    }

    private static void decodeTransactionId(ByteBuf buf, WalLog walLog) {
      walLog.setTransactionId(Codec.decodeVarInt64(buf));
    }


    private static void encodeColumnId(ByteBuf buf, WalLog walLog) {
      Codec.encodeVarInt32(buf, COLUMN_ID_TAG);
      Codec.encodeVarInt64(buf, walLog.getColumnId());
    }

    private static void decodeColumnId(ByteBuf buf, WalLog walLog) {
      walLog.setColumnId(Codec.decodeVarInt64(buf));
    }

    private static void encodeKey(ByteBuf buf, WalLog walLog) {
      Codec.encodeVarInt32(buf, KEY_TAG);
      Codec.encodeVarInt32(buf, walLog.getKeyType().getByteSize(walLog.getKey()));
      walLog.getKeyType().encode(buf, walLog.getKey());
    }

    private static void decodeKey(ByteBuf buf, WalLog walLog) {
      walLog.setKey(walLog.getKeyType().decode(buf, Codec.decodeVarInt32(buf)));
    }

    private static void encodeValue(ByteBuf buf, WalLog walLog) {
      Codec.encodeVarInt32(buf, VALUE_TAG);
      Codec.encodeVarInt32(buf, walLog.getValueType().getByteSize(walLog.getValue()));
      walLog.getValueType().encode(buf, walLog.getValue());
    }

    private static void decodeValue(ByteBuf buf, WalLog walLog) {
      walLog.setValue(walLog.getValueType().decode(buf, Codec.decodeVarInt32(buf)));
    }

    private static void encodeKeyType(ByteBuf buf, WalLog walLog) {
      Codec.encodeVarInt32(buf, KEY_TYPE_TAG);
      Codec.encodeVarInt32(buf, walLog.getKeyType().getNum());
    }

    private static void decodeKeyType(ByteBuf buf, WalLog walLog) {
      walLog.setKeyType(KeyType.get(Codec.decodeVarInt32(buf)));
    }

    private static void encodeValueType(ByteBuf buf, WalLog walLog) {
      Codec.encodeVarInt32(buf, VALUE_TYPE_TAG);
      Codec.encodeVarInt32(buf, walLog.getKeyType().getNum());
    }

    private static void decodeValueType(ByteBuf buf, WalLog walLog) {
      walLog.setKeyType(KeyType.get(Codec.decodeVarInt32(buf)));
    }


    public static WalLog decode(ByteBuf buf, int length) {
      WalLog value = new WalLog();
      int end = buf.readerIndex() + length;
      while (buf.readerIndex() < end) {
        int num = Codec.decodeVarInt32(buf);
        switch (num) {
          case COLUMN_ID_TAG:
            decodeColumnId(buf, value);
            break;
          case KEY_TAG:
            decodeKey(buf, value);
            break;
          case VALUE_TAG:
            decodeValue(buf, value);
            break;
          case KEY_TYPE_TAG:
            decodeKeyType(buf, value);
            break;
          case VALUE_TYPE_TAG:
            decodeValueType(buf, value);
            break;
          case TRANSACTION_ID_TAG:
            decodeTransactionId(buf, value);
            break;
          case SEQUENCE_NUMBER_TAG:
            decodeSequenceNumber(buf, value);
            break;
          default:
            Codec.skipUnknownField(num, buf);
        }
      }
      return value;
    }

    public static void encode(ByteBuf buf, WalLog walLog) {
      encodeColumnId(buf, walLog);
      encodeKeyType(buf, walLog);
      encodeValueType(buf, walLog);
      encodeKey(buf, walLog);
      encodeValue(buf, walLog);

      if (walLog.getTransactionId() > 0) {
        encodeTransactionId(buf, walLog);
      }
      encodeSequenceNumber(buf, walLog);

    }

    public static int getByteSize(WalLog walLog) {
      int length = COLUMN_ID_TAG_ENCODE_SIZE;
      length += Codec.computeVarInt64Size(walLog.getColumnId());

      length += KEY_TAG_ENCODE_SIZE;
      int l = walLog.getKeyType().getByteSize(walLog.getKey());
      length += Codec.computeVarInt32Size(l) + l;

      length += VALUE_TAG_ENCODE_SIZE;
      l = walLog.getValueType().getByteSize(walLog.getValue());
      length += Codec.computeVarInt32Size(l) + l;

      length += KEY_TYPE_TAG_ENCODE_SIZE;
      length += Codec.computeVarInt32Size(walLog.getKeyType().getNum());

      length += VALUE_TYPE_TAG_ENCODE_SIZE;
      length += Codec.computeVarInt32Size(walLog.getValueType().getNum());

      if (walLog.getTransactionId() > 0) {
        length += TRANSACTION_ID_TAG_ENCODE_SIZE;
        length += Codec.computeVarInt64Size(walLog.getTransactionId());
      }

      length += SEQUENCE_NUMBER_TAG_ENCODE_SIZE;
      l = SequenceNumberProtoCodec.getByteSize(walLog.getSequenceNumber());
      length += Codec.computeVarInt32Size(l) + l;

      return length;
    }

  }

}
