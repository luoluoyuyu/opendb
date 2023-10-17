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
import net.openio.opendb.db.ColumnFamily;
import net.openio.opendb.storage.metadata.DataMeta;
import net.openio.opendb.tool.codec.Codec;
import net.openio.opendb.tool.codec.sstable.SequenceNumberProtoCodec;

public class MetaDataProtoCodec {

  public static final int MAXNUMBER_NUM = 1;
  public static final int MAXNUMBER_TAG = 10;
  public static final int MAXNUMBER_TAG_ENCODE_SIZE = 1;

  public static final int UNPERSISTEDSEQNUMBERLOW_NUM = 2;
  public static final int UNPERSISTEDSEQNUMBERLOW_TAG = 18;
  public static final int UNPERSISTEDSEQNUMBERLOW_TAG_ENCODE_SIZE = 1;

  public static final int COLUMNFAMILIES_NUM = 3;
  public static final int COLUMNFAMILIES_TAG = 26;
  public static final int COLUMNFAMILIES_TAG_ENCODE_SIZE = 1;

  public static final int WALLOG_NUM = 4;
  public static final int WALLOG_TAG = 34;
  public static final int WALLOG_TAG_ENCODE_SIZE = 1;

  public static final int UNDOLOG_NUM = 5;
  public static final int UNDOLOG_TAG = 42;
  public static final int UNDOLOG_TAG_ENCODE_SIZE = 1;

  public static final int CHECKPOINTFILE_NUM = 6;
  public static final int CHECKPOINTFILE_TAG = 50;
  public static final int CHECKPOINTFILE_TAG_ENCODE_SIZE = 1;


  private static void encodeMaxNumber(ByteBuf buf, DataMeta datameta) {
    Codec.encodeVarInt32(buf, MAXNUMBER_TAG);
    Codec.encodeVarInt32(buf, SequenceNumberProtoCodec.getByteSize(datameta.getMaxNumber()));
    SequenceNumberProtoCodec.encode(buf, datameta.getMaxNumber());
  }

  private static void decodeMaxNumber(ByteBuf buf, DataMeta datameta) {
    datameta.setMaxNumber(SequenceNumberProtoCodec.decode(buf, Codec.decodeVarInt32(buf)));
  }

  private static void encodeUnPersistedSeqNumberLow(ByteBuf buf, DataMeta datameta) {
    Codec.encodeVarInt32(buf, UNPERSISTEDSEQNUMBERLOW_TAG);
    Codec.encodeVarInt32(buf, SequenceNumberProtoCodec.getByteSize(datameta.getUnPersistedSeqNumberLow()));
    SequenceNumberProtoCodec.encode(buf, datameta.getUnPersistedSeqNumberLow());
  }

  private static void decodeUnPersistedSeqNumberLow(ByteBuf buf, DataMeta datameta) {
    datameta.setUnPersistedSeqNumberLow(SequenceNumberProtoCodec.decode(buf, Codec.decodeVarInt32(buf)));
  }

  private static void decodeColumnFamilies(ByteBuf buf, DataMeta datameta) {
    datameta.addColumnFamily(ColumnFamilyProtoCodec.decode(buf, Codec.decodeVarInt32(buf)));
  }

  private static void encodeColumnFamilies(ByteBuf buf, DataMeta datameta) {
    for (ColumnFamily value : datameta.getColumnFamilies()) {
      Codec.encodeVarInt32(buf, COLUMNFAMILIES_TAG);
      Codec.encodeVarInt32(buf, ColumnFamilyProtoCodec.getByteSize(value));
      ColumnFamilyProtoCodec.encode(buf, value);
    }
  }

  private static void decodeWalLog(ByteBuf buf, DataMeta datameta) {
    datameta.addWalLog(Codec.decodeString(buf, Codec.decodeVarInt32(buf)));
  }

  private static void encodeWalLog(ByteBuf buf, DataMeta datameta) {
    for (java.lang.String value : datameta.getWalLog()) {
      Codec.encodeVarInt32(buf, WALLOG_TAG);
      Codec.encodeString(buf, value);
    }
  }

  private static void decodeUndoLog(ByteBuf buf, DataMeta datameta) {
    datameta.addUndoWalLog(Codec.decodeString(buf, Codec.decodeVarInt32(buf)));
  }

  private static void encodeUndoLog(ByteBuf buf, DataMeta datameta) {
    for (java.lang.String value : datameta.getUndoLog()) {
      Codec.encodeVarInt32(buf, UNDOLOG_TAG);
      Codec.encodeString(buf, value);
    }
  }

  private static void decodeCheckPointFile(ByteBuf buf, DataMeta datameta) {
    datameta.addCheckPointFile(Codec.decodeString(buf, Codec.decodeVarInt32(buf)));
  }

  private static void encodeCheckPointFile(ByteBuf buf, DataMeta datameta) {
    for (java.lang.String value : datameta.getCheckpointFile()) {
      Codec.encodeVarInt32(buf, CHECKPOINTFILE_TAG);
      Codec.encodeString(buf, value);
    }
  }


  public static void encode(ByteBuf buf, DataMeta datameta) {
    encodeMaxNumber(buf, datameta);
    encodeUnPersistedSeqNumberLow(buf, datameta);
    encodeColumnFamilies(buf, datameta);
    encodeWalLog(buf, datameta);
    encodeUndoLog(buf, datameta);
    encodeCheckPointFile(buf, datameta);
  }

  public static DataMeta decode(ByteBuf buf, int length) {
    DataMeta value = new DataMeta();
    int end = buf.readerIndex() + length;
    while (buf.readerIndex() < end) {
      int num = Codec.decodeVarInt32(buf);
      switch (num) {
        case MAXNUMBER_TAG:
          decodeMaxNumber(buf, value);
          break;
        case UNPERSISTEDSEQNUMBERLOW_TAG:
          decodeUnPersistedSeqNumberLow(buf, value);
          break;
        case COLUMNFAMILIES_TAG:
          decodeColumnFamilies(buf, value);
          break;
        case WALLOG_TAG:
          decodeWalLog(buf, value);
          break;
        case UNDOLOG_TAG:
          decodeUndoLog(buf, value);
          break;
        case CHECKPOINTFILE_TAG:
          decodeCheckPointFile(buf, value);
          break;
        default:
          Codec.skipUnknownField(num, buf);
      }
    }
    return value;
  }

  public static int getByteSize(DataMeta metaData) {
    int length = MAXNUMBER_TAG_ENCODE_SIZE;
    int i = SequenceNumberProtoCodec.getByteSize(metaData.getMaxNumber());
    length += Codec.computeVarInt32Size(i) + i;


    length += UNPERSISTEDSEQNUMBERLOW_TAG_ENCODE_SIZE;
    i = SequenceNumberProtoCodec.getByteSize(metaData.getUnPersistedSeqNumberLow());
    length += Codec.computeVarInt32Size(i) + i;


    length += COLUMNFAMILIES_TAG_ENCODE_SIZE * metaData.getColumnFamilies().size();
    for (ColumnFamily value : metaData.getColumnFamilies()) {
      i = ColumnFamilyProtoCodec.getByteSize(value);
      length += Codec.computeVarInt32Size(i) + i;
    }

    length += WALLOG_TAG_ENCODE_SIZE * metaData.getWalLog().size();
    for (java.lang.String value : metaData.getWalLog()) {
      i = ByteBufUtil.utf8Bytes(value);
      length += Codec.computeVarInt32Size(i) + i;
    }

    length += UNDOLOG_TAG_ENCODE_SIZE * metaData.getUndoLog().size();
    for (java.lang.String value : metaData.getUndoLog()) {
      i = ByteBufUtil.utf8Bytes(value);
      length += Codec.computeVarInt32Size(i) + i;

    }

    length += CHECKPOINTFILE_TAG_ENCODE_SIZE * metaData.getCheckpointFile().size();
    for (java.lang.String value : metaData.getCheckpointFile()) {
      i = ByteBufUtil.utf8Bytes(value);
      length += Codec.computeVarInt32Size(i) + i;
    }
    return length;

  }

}
