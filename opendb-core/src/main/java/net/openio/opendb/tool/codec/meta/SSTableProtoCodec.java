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
import io.netty.buffer.ByteBufUtil;
import net.openio.opendb.model.key.KeyType;
import net.openio.opendb.model.value.ValueType;
import net.openio.opendb.storage.sstable.SSTable;
import net.openio.opendb.tool.codec.Codec;

public class SSTableProtoCodec {

  public static final int FILENAME_NUM = 1;
  public static final int FILENAME_TAG = 10;
  public static final int FILENAME_TAG_ENCODE_SIZE = 1;

  public static final int CREATETIME_NUM = 2;
  public static final int CREATETIME_TAG = 16;
  public static final int CREATETIME_TAG_ENCODE_SIZE = 1;


  public static final int SIZE_NUM = 3;
  public static final int SIZE_TAG = 24;
  public static final int SIZE_TAG_ENCODE_SIZE = 1;


  public static final int KEYTYPE_NUM = 4;
  public static final int KEYTYPE_TAG = 32;
  public static final int KEYTYPE_TAG_ENCODE_SIZE = 1;

  public static final int VALUETYPE_NUM = 5;
  public static final int VALUETYPE_TAG = 40;
  public static final int VALUETYPE_TAG_ENCODE_SIZE = 1;


  public static final int MINKEY_NUM = 6;
  public static final int MINKEY_TAG = 50;
  public static final int MINKEY_TAG_ENCODE_SIZE = 1;

  public static final int MAXVALUE_NUM = 7;
  public static final int MAXVALUE_TAG = 58;
  public static final int MAXVALUE_TAG_ENCODE_SIZE = 1;

  public static final int FILEHEADSEEK_NUM = 8;
  public static final int FILEHEADSEEK_TAG = 64;
  public static final int FILEHEADSEEK_TAG_ENCODE_SIZE = 1;


  public static final int FILEHEADSIZE_NUM = 9;
  public static final int FILEHEADSIZE_TAG = 72;
  public static final int FILEHEADSIZE_TAG_ENCODE_SIZE = 1;


  private static void encodeFilename(ByteBuf buf, SSTable sstable) {
    Codec.encodeVarInt32(buf, FILENAME_TAG);
    Codec.encodeString(buf, sstable.getFileName());
  }

  private static void decodeFilename(ByteBuf buf, SSTable sstable) {
    sstable.setFileName(Codec.decodeString(buf, Codec.decodeVarInt32(buf)));
  }

  private static void encodeCreatetime(ByteBuf buf, SSTable sstable) {
    Codec.encodeVarInt32(buf, CREATETIME_TAG);
    Codec.encodeVarInt64(buf, sstable.getCreateTime());
  }

  private static void decodeCreatetime(ByteBuf buf, SSTable sstable) {
    sstable.setCreateTime(Codec.decodeVarInt64(buf));
  }

  private static void encodeSize(ByteBuf buf, SSTable sstable) {
    Codec.encodeVarInt32(buf, SIZE_TAG);
    Codec.encodeVarInt32(buf, sstable.getSize());
  }

  private static void decodeSize(ByteBuf buf, SSTable sstable) {
    sstable.setSize(Codec.decodeVarInt32(buf));
  }

  private static void encodeKeytype(ByteBuf buf, SSTable sstable) {
    Codec.encodeVarInt32(buf, KEYTYPE_TAG);
    Codec.encodeVarInt32(buf, sstable.getKeyType().getNum());
  }

  private static void decodeKeytype(ByteBuf buf, SSTable sstable) {
    sstable.setKeyType(KeyType.get(Codec.decodeVarInt32(buf)));
  }

  private static void encodeValuetype(ByteBuf buf, SSTable sstable) {
    Codec.encodeVarInt32(buf, VALUETYPE_TAG);
    Codec.encodeVarInt32(buf, sstable.getValueType().getNum());
  }

  private static void decodeValuetype(ByteBuf buf, SSTable sstable) {
    sstable.setValueType(ValueType.get(Codec.decodeVarInt32(buf)));
  }

  private static void encodeMinkey(ByteBuf buf, SSTable sstable) {
    Codec.encodeVarInt32(buf, MINKEY_TAG);
    Codec.encodeVarInt32(buf, sstable.getKeyType().getByteSize(sstable.getMinKey()));
    sstable.getKeyType().encode(buf, sstable.getMinKey());
  }

  private static void decodeMinkey(ByteBuf buf, SSTable sstable) {
    sstable.setMinKey(sstable.getKeyType().decode(buf, Codec.decodeVarInt32(buf)));
  }

  private static void encodeMaxvalue(ByteBuf buf, SSTable sstable) {
    Codec.encodeVarInt32(buf, MAXVALUE_TAG);
    Codec.encodeVarInt32(buf, sstable.getKeyType().getByteSize(sstable.getMaxValue()));
    sstable.getKeyType().encode(buf, sstable.getMaxValue());
  }

  private static void decodeMaxvalue(ByteBuf buf, SSTable sstable) {
    sstable.setMaxValue(sstable.getKeyType().decode(buf, Codec.decodeVarInt32(buf)));
  }

  private static void encodeFileHeadSeek(ByteBuf buf, SSTable sstable) {
    Codec.encodeVarInt32(buf, FILEHEADSEEK_TAG);
    Codec.encodeVarInt32(buf, sstable.getFileHeadSeek());
  }

  private static void decodeFileHeadSeek(ByteBuf buf, SSTable sstable) {
    sstable.setFileHeadSeek(Codec.decodeVarInt32(buf));
  }

  private static void encodeFileHeadSize(ByteBuf buf, SSTable sstable) {
    Codec.encodeVarInt32(buf, FILEHEADSIZE_TAG);
    Codec.encodeVarInt32(buf, sstable.getFileHeadSize());
  }

  private static void decodeFileHeadSize(ByteBuf buf, SSTable sstable) {
    sstable.setFileHeadSize(Codec.decodeVarInt32(buf));
  }

  public static void encode(ByteBuf buf, SSTable sstable) {

    encodeFilename(buf, sstable);

    encodeCreatetime(buf, sstable);

    encodeSize(buf, sstable);

    encodeKeytype(buf, sstable);

    encodeValuetype(buf, sstable);

    encodeMinkey(buf, sstable);

    encodeMaxvalue(buf, sstable);

    encodeFileHeadSeek(buf, sstable);

    encodeFileHeadSize(buf, sstable);
  }

  public static SSTable decode(ByteBuf buf, int length) {
    SSTable value = new SSTable();
    int end = buf.readerIndex() + length;
    while (buf.readerIndex() < end) {
      int num = Codec.decodeVarInt32(buf);
      switch (num) {
        case FILENAME_TAG:
          decodeFilename(buf, value);
          break;
        case CREATETIME_TAG:
          decodeCreatetime(buf, value);
          break;
        case SIZE_TAG:
          decodeSize(buf, value);
          break;
        case KEYTYPE_TAG:
          decodeKeytype(buf, value);
          break;
        case VALUETYPE_TAG:
          decodeValuetype(buf, value);
          break;
        case MINKEY_TAG:
          decodeMinkey(buf, value);
          break;
        case MAXVALUE_TAG:
          decodeMaxvalue(buf, value);
          break;
        case FILEHEADSEEK_TAG:
          decodeFileHeadSeek(buf, value);
          break;
        case FILEHEADSIZE_TAG:
          decodeFileHeadSize(buf, value);
          break;
        default:
          Codec.skipUnknownField(num, buf);
      }
    }
    return value;
  }

  public static int getByteSize(SSTable ssTable) {
    int length = FILENAME_TAG_ENCODE_SIZE;

    int i = ByteBufUtil.utf8Bytes(ssTable.getFileName());
    length = Codec.computeVarInt32Size(i) + i + length;

    length += CREATETIME_TAG_ENCODE_SIZE;
    length += Codec.computeVarInt64Size(ssTable.getCreateTime());

    length += SIZE_TAG_ENCODE_SIZE;
    length += Codec.computeVarInt32Size(ssTable.getSize());

    length += KEYTYPE_TAG_ENCODE_SIZE;
    length += Codec.computeVarInt32Size(ssTable.getKeyType().getNum());

    length += VALUETYPE_TAG_ENCODE_SIZE;
    length += Codec.computeVarInt32Size(ssTable.getValueType().getNum());

    length += MINKEY_TAG_ENCODE_SIZE;
    i = ssTable.getKeyType().getByteSize(ssTable.getMinKey());
    length = length + Codec.computeVarInt32Size(i) + i;


    length += MAXVALUE_TAG_ENCODE_SIZE;
    i = ssTable.getKeyType().getByteSize(ssTable.getMaxValue());
    length = Codec.computeVarInt32Size(i) + length + i;

    length += FILEHEADSEEK_TAG_ENCODE_SIZE;
    length += Codec.computeVarInt32Size(ssTable.getFileHeadSeek());

    length += FILEHEADSIZE_TAG_ENCODE_SIZE;
    length += Codec.computeVarInt32Size(ssTable.getFileHeadSize());
    return length;
  }
}
