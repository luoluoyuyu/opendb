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
import net.openio.opendb.model.key.Key;
import net.openio.opendb.model.key.KeyType;
import net.openio.opendb.storage.sstable.IndexData;
import net.openio.opendb.storage.sstable.SSTable;
import net.openio.opendb.tool.codec.Codec;

import java.util.ArrayList;
import java.util.List;


public class IndexDataProtoCodec {

  public static final int DATA_ID_NUM = 1;
  public static final int DATA_ID_TAG = 8;
  public static final int DATA_ID_TAG_ENCODE_SIZE = 1;


  public static final int OFFSET_NUM = 2;
  public static final int OFFSET_TAG = 16;
  public static final int OFFSET_TAG_ENCODE_SIZE = 1;

  public static final int NUM_NUM = 3;
  public static final int NUM_TAG = 24;
  public static final int NUM_TAG_ENCODE_SIZE = 1;


  public static final int KEYS_NUM = 4;
  public static final int KEYS_TAG = 34;
  public static final int KEYS_TAG_ENCODE_SIZE = 1;


  public static final int DATA_BLOCK_SIZE_NUM = 5;
  public static final int DATA_BLOCK_SIZE_TAG = 40;
  public static final int DATA_BLOCK_SIZE_TAG_ENCODE_SIZE = 1;

  public static final int FIRST_INDEX_NUM = 6;
  public static final int FIRST_INDEX_TAG = 48;
  public static final int FIRST_INDEX_TAG_ENCODE_SIZE = 1;

  private static void encodeFirstIndex(ByteBuf buf, IndexData indexData) {
    Codec.encodeVarInt32(buf, FIRST_INDEX_TAG);
    Codec.encodeVarInt32(buf, indexData.getFirstIndex());
  }

  private static void decodeFirstIndex(ByteBuf buf, IndexData indexData) {
    indexData.setFirstIndex(Codec.decodeVarInt32(buf));
  }

  private static void encodeDataId(ByteBuf buf, IndexData indexBlock) {
    Codec.encodeVarInt32(buf, DATA_ID_TAG);
    Codec.encodeVarInt64(buf, indexBlock.getDataId());
  }

  private static void decodeDataId(ByteBuf buf, IndexData indexBlock) {
    indexBlock.setDataId(Codec.decodeVarInt64(buf));
  }


  private static void encodeOffset(ByteBuf buf, IndexData indexBlock) {
    Codec.encodeVarInt32(buf, OFFSET_TAG);
    Codec.encodeVarInt32(buf, indexBlock.getOffset());
  }

  private static void decodeOffset(ByteBuf buf, IndexData indexBlock) {
    indexBlock.setOffset(Codec.decodeVarInt32(buf));
  }


  private static void encodeNum(ByteBuf buf, IndexData indexBlock) {
    Codec.encodeVarInt32(buf, NUM_TAG);
    Codec.encodeVarInt32(buf, indexBlock.getNum());
  }

  private static void decodeNum(ByteBuf buf, IndexData indexBlock) {
    indexBlock.setNum(Codec.decodeVarInt32(buf));
  }

  private static void decodeKeys(ByteBuf buf, IndexData indexBlock) {
    indexBlock.add(indexBlock.getKeyType().decode(buf, Codec.decodeVarInt32(buf)));
  }

  private static void encodeKeys(ByteBuf buf, IndexData indexBlock) {
    for (Key key : indexBlock.getKeys()) {
      Codec.encodeVarInt32(buf, KEYS_TAG);
      Codec.encodeVarInt32(buf, indexBlock.getKeyType().getByteSize(key));
      indexBlock.getKeyType().encode(buf, key);
    }
  }

  private static void encodeDataBlockSize(ByteBuf buf, IndexData indexData) {
    Codec.encodeVarInt32(buf, DATA_BLOCK_SIZE_TAG);
    Codec.encodeVarInt32(buf, indexData.getDataBlockSize());
  }

  private static void decodeDataBlockSize(ByteBuf buf, IndexData indexData) {
    indexData.setDataBlockSize(Codec.decodeVarInt32(buf));
  }


  public static IndexData decode(ByteBuf buf, SSTable ssTable) {
    IndexData value = new IndexData();
    value.setSsTable(ssTable);
    while (buf.readerIndex() < buf.writerIndex()) {
      int num = Codec.decodeVarInt32(buf);
      switch (num) {
        case DATA_ID_TAG:
          decodeDataId(buf, value);
          break;
        case OFFSET_TAG:
          decodeOffset(buf, value);
          break;
        case NUM_TAG:
          decodeNum(buf, value);
          value.setKeys(new ArrayList<>(value.getNum()));
          break;
        case DATA_BLOCK_SIZE_TAG:
          decodeDataBlockSize(buf, value);
          break;
        case FIRST_INDEX_TAG:
          decodeFirstIndex(buf, value);
          break;
        case KEYS_TAG:
          decodeKeys(buf, value);
          break;
        default:
          if (value.getKeys().size() >= value.getNum()) {
            return value;
          }
          Codec.skipUnknownField(num, buf);
      }
    }
    return value;
  }

  public static IndexData decode(ByteBuf buf, int length, SSTable ssTable) {
    IndexData value = new IndexData();
    value.setSsTable(ssTable);
    int end = buf.readerIndex() + length;
    while (buf.readerIndex() < end) {
      int num = Codec.decodeVarInt32(buf);
      switch (num) {
        case DATA_ID_TAG:
          decodeDataId(buf, value);
          break;
        case OFFSET_TAG:
          decodeOffset(buf, value);
          break;
        case NUM_TAG:
          decodeNum(buf, value);
          value.setKeys(new ArrayList<>(value.getNum()));
          break;
        case DATA_BLOCK_SIZE_TAG:
          decodeDataBlockSize(buf, value);
          break;
        case FIRST_INDEX_TAG:
          decodeFirstIndex(buf, value);
          break;
        case KEYS_TAG:
          decodeKeys(buf, value);
          break;
        default:
          Codec.skipUnknownField(num, buf);
      }
    }
    return value;
  }

  public static void encode(ByteBuf buf, IndexData indexBlock) {

    encodeDataId(buf, indexBlock);

    encodeOffset(buf, indexBlock);

    encodeNum(buf, indexBlock);

    encodeDataBlockSize(buf, indexBlock);

    encodeFirstIndex(buf, indexBlock);

    encodeKeys(buf, indexBlock);

  }

  public static int getByteSize(IndexData indexBlock) {
    int length = getHeadByteSize(indexBlock);
    length += indexBlock.getKeys().size() * KEYS_TAG_ENCODE_SIZE;
    for (Key key : indexBlock.getKeys()) {
      int l = indexBlock.getKeyType().getByteSize(key);
      l += Codec.computeVarInt32Size(l);
      length += l;
    }
    return length;
  }

  public static int getHeadByteSize(IndexData indexBlock) {
    int length = DATA_ID_TAG_ENCODE_SIZE;
    length += Codec.computeVarInt64Size(indexBlock.getDataId());

    length += OFFSET_TAG_ENCODE_SIZE;
    length += Codec.computeVarInt32Size(indexBlock.getOffset());

    length += NUM_TAG_ENCODE_SIZE;
    length += Codec.computeVarInt32Size(indexBlock.getNum());

    length += DATA_BLOCK_SIZE_TAG_ENCODE_SIZE;
    length += Codec.computeVarInt32Size(indexBlock.getDataBlockSize());

    length += FIRST_INDEX_TAG_ENCODE_SIZE;
    length += Codec.computeVarInt32Size(indexBlock.getFirstIndex());
    return length;

  }

  public static int getKeyByteSize(KeyType keyType, Key key) {
    int length = keyType.getByteSize(key);
    length += Codec.computeVarInt32Size(length);
    length += KEYS_TAG_ENCODE_SIZE;
    return length;
  }

  public static int getKeyByteSize(KeyType keyType, List<Key> key) {
    int l = 0;
    for (Key k : key) {
      int length = keyType.getByteSize(k);
      length += Codec.computeVarInt32Size(length);
      l += KEYS_TAG_ENCODE_SIZE + length;
    }
    return l;
  }

}