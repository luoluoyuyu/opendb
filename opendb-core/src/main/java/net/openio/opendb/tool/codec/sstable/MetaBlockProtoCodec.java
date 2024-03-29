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
import net.openio.opendb.storage.sstable.MetaBlock;
import net.openio.opendb.storage.sstable.MetaData;
import net.openio.opendb.storage.sstable.SSTable;
import net.openio.opendb.tool.codec.Codec;

import java.util.ArrayList;


public class MetaBlockProtoCodec {

  public static final int META_DATA_NUM = 1;
  public static final int META_DATA_TAG = 10;
  public static final int META_DATA_TAG_ENCODE_SIZE = 1;


  private static void decodeMetadata(ByteBuf buf, MetaBlock metaBlock) {
    metaBlock.add(MetaDataProtoCodec.decode(buf, Codec.decodeVarInt32(buf), metaBlock.getSsTable()));
  }

  private static void encodeMetadata(ByteBuf buf, MetaBlock metaBlock) {
    for (MetaData value : metaBlock.getMetaData()) {
      Codec.encodeVarInt32(buf, META_DATA_TAG);
      int length = MetaDataProtoCodec.getByteSize(value);
      Codec.encodeVarInt32(buf, length);
      MetaDataProtoCodec.encode(buf, value);
    }
  }

  public static MetaBlock decode(ByteBuf buf, SSTable ssTable) {
    MetaBlock value = new MetaBlock(new ArrayList<>(), ssTable);
    while (buf.readerIndex() < buf.writerIndex()) {
      int num = Codec.decodeVarInt32(buf);
      switch (num) {
        case META_DATA_TAG:
          decodeMetadata(buf, value);
          break;
        default:
          return value;
      }
    }
    return value;
  }

  public static void encode(ByteBuf buf, MetaBlock meta) {

    encodeMetadata(buf, meta);
    if (buf.writerIndex() < buf.capacity()) {
      buf.writeByte(0);
    }
  }

  public static MetaBlock decode(ByteBuf buf, int length, SSTable ssTable) {
    MetaBlock value = new MetaBlock(new ArrayList<>(), ssTable);
    int end = buf.readerIndex() + length;
    while (buf.readerIndex() < end) {
      int num = Codec.decodeVarInt32(buf);
      switch (num) {
        case META_DATA_TAG:
          decodeMetadata(buf, value);
          break;
        default:
          Codec.skipUnknownField(num, buf);
      }
    }
    return value;
  }

  public static int getByteSize(MetaBlock metaBlock) {
    int length = META_DATA_TAG_ENCODE_SIZE * metaBlock.getMetaData().size();
    for (MetaData value : metaBlock.getMetaData()) {
      int byteSize = MetaDataProtoCodec.getByteSize(value);
      byteSize += Codec.computeVarInt32Size(byteSize);
      length += byteSize;
    }
    return length;
  }


  public static int getAddMetaSize(MetaData metaData) {

    int byteSize = MetaDataProtoCodec.getByteSize(metaData);
    byteSize += Codec.computeVarInt32Size(byteSize);
    byteSize += META_DATA_TAG_ENCODE_SIZE;

    return byteSize;
  }

}
