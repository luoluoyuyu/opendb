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
import net.openio.opendb.storage.sstable.FileHeadBlock;
import net.openio.opendb.tool.codec.Codec;


public class FileHeadProtoCodec {

  public static final int DATA_OFFER_SEEK_NUM = 1;
  public static final int DATA_OFFER_SEEK_TAG = 8;
  public static final int DATA_OFFER_SEEK_TAG_ENCODE_SIZE = 1;

  public static final int DATA_OFFER_SIZE_NUM = 2;
  public static final int DATA_OFFER_SIZE_TAG = 16;
  public static final int DATA_OFFER_SIZE_TAG_ENCODE_SIZE = 1;

  public static final int INDEX_OFFER_SEEK_NUM = 3;
  public static final int INDEX_OFFER_SEEK_TAG = 24;
  public static final int INDEX_OFFER_SEEK_TAG_ENCODE_SIZE = 1;

  public static final int INDEX_OFFER_SIZE_NUM = 4;
  public static final int INDEX_OFFER_SIZE_TAG = 32;
  public static final int INDEX_OFFER_SIZE_TAG_ENCODE_SIZE = 1;

  public static final int META_OFFER_SEEK_NUM = 5;
  public static final int META_OFFER_SEEK_TAG = 40;
  public static final int META_OFFER_SEEK_TAG_ENCODE_SIZE = 1;

  public static final int META_OFFER_SIZE_NUM = 6;
  public static final int META_OFFER_SIZE_TAG = 48;
  public static final int META_OFFER_SIZE_TAG_ENCODE_SIZE = 1;

  public static final int BLOOM_OFFER_SEEK_NUM = 7;
  public static final int BLOOM_OFFER_SEEK_TAG = 56;
  public static final int BLOOM_OFFER_SEEK_TAG_ENCODE_SIZE = 1;

  public static final int BLOOM_OFFER_SIZE_NUM = 8;
  public static final int BLOOM_OFFER_SIZE_TAG = 64;
  public static final int BLOOM_OFFER_SIZE_TAG_ENCODE_SIZE = 1;

  private static void encodeDataOfferSeek(ByteBuf buf, FileHeadBlock fileHeadBlock) {
    Codec.encodeVarInt32(buf, DATA_OFFER_SEEK_TAG);
    Codec.encodeVarInt32(buf, fileHeadBlock.getDataOfferSeek());
  }

  private static void decodeDataOfferSeek(ByteBuf buf, FileHeadBlock fileHeadBlock) {
    fileHeadBlock.setDataOfferSeek(Codec.decodeVarInt32(buf));
  }

  private static void encodeDataOfferSize(ByteBuf buf, FileHeadBlock fileHeadBlock) {
    Codec.encodeVarInt32(buf, DATA_OFFER_SIZE_TAG);
    Codec.encodeVarInt32(buf, fileHeadBlock.getDataOfferSize());
  }

  private static void decodeDataOfferSize(ByteBuf buf, FileHeadBlock fileHeadBlock) {
    fileHeadBlock.setDataOfferSize(Codec.decodeVarInt32(buf));
  }

  private static void encodeIndexOfferSeek(ByteBuf buf, FileHeadBlock fileHeadBlock) {
    Codec.encodeVarInt32(buf, INDEX_OFFER_SEEK_TAG);
    Codec.encodeVarInt32(buf, fileHeadBlock.getIndexOfferSeek());
  }

  private static void decodeIndexOfferSeek(ByteBuf buf, FileHeadBlock fileHeadBlock) {
    fileHeadBlock.setIndexOfferSeek(Codec.decodeVarInt32(buf));
  }

  private static void encodeIndexOfferSize(ByteBuf buf, FileHeadBlock fileHeadBlock) {
    Codec.encodeVarInt32(buf, INDEX_OFFER_SIZE_TAG);
    Codec.encodeVarInt32(buf, fileHeadBlock.getIndexOfferSize());
  }

  private static void decodeIndexOfferSize(ByteBuf buf, FileHeadBlock fileHeadBlock) {
    fileHeadBlock.setIndexOfferSize(Codec.decodeVarInt32(buf));
  }

  private static void encodeMetaOfferSeek(ByteBuf buf, FileHeadBlock fileHeadBlock) {
    Codec.encodeVarInt32(buf, META_OFFER_SEEK_TAG);
    Codec.encodeVarInt32(buf, fileHeadBlock.getMetaOfferSeek());
  }

  private static void decodeMetaOfferSeek(ByteBuf buf, FileHeadBlock fileHeadBlock) {
    fileHeadBlock.setMetaOfferSeek(Codec.decodeVarInt32(buf));
  }

  private static void encodeMetaOfferSize(ByteBuf buf, FileHeadBlock fileHeadBlock) {
    Codec.encodeVarInt32(buf, META_OFFER_SIZE_TAG);
    Codec.encodeVarInt32(buf, fileHeadBlock.getMetaOfferSize());
  }

  private static void decodeMetaOfferSize(ByteBuf buf, FileHeadBlock fileHeadBlock) {
    fileHeadBlock.setMetaOfferSize(Codec.decodeVarInt32(buf));
  }

  private static void encodeBloomOfferSeek(ByteBuf buf, FileHeadBlock fileHeadBlock) {
    Codec.encodeVarInt32(buf, BLOOM_OFFER_SEEK_TAG);
    Codec.encodeVarInt32(buf, fileHeadBlock.getBloomOfferSeek());
  }

  private static void decodeBloomOfferSeek(ByteBuf buf, FileHeadBlock fileHeadBlock) {
    fileHeadBlock.setBloomOfferSeek(Codec.decodeVarInt32(buf));
  }

  private static void encodeBloomOfferSize(ByteBuf buf, FileHeadBlock fileHeadBlock) {
    Codec.encodeVarInt32(buf, BLOOM_OFFER_SIZE_TAG);
    Codec.encodeVarInt32(buf, fileHeadBlock.getBloomOfferSize());
  }

  private static void decodeBloomOfferSize(ByteBuf buf, FileHeadBlock fileHeadBlock) {
    fileHeadBlock.setBloomOfferSize(Codec.decodeVarInt32(buf));
  }


  public static int getByteSize(FileHeadBlock fileHeadBlock) {
    int length = DATA_OFFER_SEEK_TAG_ENCODE_SIZE;
    length += Codec.computeVarInt32Size(fileHeadBlock.getDataOfferSeek());

    length += DATA_OFFER_SIZE_TAG_ENCODE_SIZE;
    length += Codec.computeVarInt32Size(fileHeadBlock.getDataOfferSize());

    length += INDEX_OFFER_SEEK_TAG_ENCODE_SIZE;
    length += Codec.computeVarInt32Size(fileHeadBlock.getIndexOfferSeek());

    length += INDEX_OFFER_SIZE_TAG_ENCODE_SIZE;
    length += Codec.computeVarInt32Size(fileHeadBlock.getIndexOfferSize());

    length += META_OFFER_SEEK_TAG_ENCODE_SIZE;
    length += Codec.computeVarInt32Size(fileHeadBlock.getMetaOfferSeek());

    length += META_OFFER_SIZE_TAG_ENCODE_SIZE;
    length += Codec.computeVarInt32Size(fileHeadBlock.getMetaOfferSize());

    length += BLOOM_OFFER_SEEK_TAG_ENCODE_SIZE;
    length += Codec.computeVarInt32Size(fileHeadBlock.getBloomOfferSeek());

    length += BLOOM_OFFER_SIZE_TAG_ENCODE_SIZE;
    length += Codec.computeVarInt32Size(fileHeadBlock.getMetaOfferSize());
    return length;
  }

  public static FileHeadBlock decode(ByteBuf buf) {
    FileHeadBlock value = new FileHeadBlock();
    while (buf.readerIndex() < buf.writerIndex()) {
      int num = Codec.decodeVarInt32(buf);
      switch (num) {
        case DATA_OFFER_SEEK_TAG:
          decodeDataOfferSeek(buf, value);
          break;
        case DATA_OFFER_SIZE_TAG:
          decodeDataOfferSize(buf, value);
          break;
        case INDEX_OFFER_SEEK_TAG:
          decodeIndexOfferSeek(buf, value);
          break;
        case INDEX_OFFER_SIZE_TAG:
          decodeIndexOfferSize(buf, value);
          break;
        case META_OFFER_SEEK_TAG:
          decodeMetaOfferSeek(buf, value);
          break;
        case META_OFFER_SIZE_TAG:
          decodeMetaOfferSize(buf, value);
          break;
        case BLOOM_OFFER_SEEK_TAG:
          decodeBloomOfferSeek(buf, value);
          break;
        case BLOOM_OFFER_SIZE_TAG:
          decodeBloomOfferSize(buf, value);
          break;
        default:
          return value;
      }
    }
    return value;
  }

  public static void encode(ByteBuf buf, FileHeadBlock fileHeadBlock) {

    encodeDataOfferSeek(buf, fileHeadBlock);


    encodeDataOfferSize(buf, fileHeadBlock);


    encodeIndexOfferSeek(buf, fileHeadBlock);


    encodeIndexOfferSize(buf, fileHeadBlock);


    encodeMetaOfferSeek(buf, fileHeadBlock);


    encodeMetaOfferSize(buf, fileHeadBlock);


    encodeBloomOfferSeek(buf, fileHeadBlock);


    encodeBloomOfferSize(buf, fileHeadBlock);

    if (buf.capacity() > buf.writerIndex()) {
      buf.writeByte(0);
    }

  }
}
