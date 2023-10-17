package net.openio.opendb.tool.codec.sstable;

import io.netty.buffer.ByteBuf;
import net.openio.opendb.storage.sstable.MetaData;
import net.openio.opendb.storage.sstable.SSTable;
import net.openio.opendb.tool.codec.Codec;


public class MetaDataProtoCodec {

  public static final int INDEX_ID_NUM = 1;
  public static final int INDEX_ID_TAG = 8;
  public static final int INDEX_ID_TAG_ENCODE_SIZE = 1;

  public static final int OFFSET_NUM = 2;
  public static final int OFFSET_TAG = 16;
  public static final int OFFSET_TAG_ENCODE_SIZE = 1;

  public static final int SIZE_NUM = 3;
  public static final int SIZE_TAG = 24;
  public static final int SIZE_TAG_ENCODE_SIZE = 1;

  public static final int KEY_NUM = 4;
  public static final int KEY_TAG = 34;
  public static final int KEY_TAG_ENCODE_SIZE = 1;

  private static void encodeIndexId(ByteBuf buf, MetaData metaData) {
    Codec.encodeVarInt32(buf, INDEX_ID_TAG);
    Codec.encodeVarInt64(buf, Codec.encodeZigzag64(metaData.getIndexId()));
  }

  private static void decodeIndexId(ByteBuf buf, MetaData meta) {
    meta.setIndexId(Codec.decodeZigzag64(Codec.decodeVarInt64(buf)));
  }

  private static void encodeOffset(ByteBuf buf, MetaData metaData) {
    Codec.encodeVarInt32(buf, OFFSET_TAG);
    Codec.encodeVarUInt32(buf, Codec.encodeZigzag32(metaData.getOffset()));
  }

  private static void decodeOffset(ByteBuf buf, MetaData metaData) {
    metaData.setOffset(Codec.decodeZigzag32(Codec.decodeVarInt32(buf)));
  }

  private static void encodeSize(ByteBuf buf, MetaData metaData) {
    Codec.encodeVarInt32(buf, SIZE_TAG);
    Codec.encodeVarUInt32(buf, Codec.encodeZigzag32(metaData.getSize()));
  }

  private static void decodeSize(ByteBuf buf, MetaData metaData) {
    metaData.setSize(Codec.decodeZigzag32(Codec.decodeVarInt32(buf)));
  }

  private static void encodeKey(ByteBuf buf, MetaData metaData) {
    Codec.encodeVarInt32(buf, KEY_TAG);
    Codec.encodeVarInt32(buf, metaData.getSsTable().getKeyType().getByteSize(metaData.getKey()));
    metaData.getSsTable().getKeyType().encode(buf, metaData.getKey());
  }

  private static void decodeKey(ByteBuf buf, MetaData metaData) {
    metaData.setKey(metaData.getSsTable().getKeyType().decode(buf, Codec.decodeVarInt32(buf)));
  }

  public static MetaData decode(ByteBuf buf, int length, SSTable ssTable) {
    MetaData value = new MetaData();
    value.setSsTable(ssTable);
    int end = buf.readerIndex() + length;
    while (buf.readerIndex() < end) {
      int num = Codec.decodeVarInt32(buf);
      switch (num) {
        case INDEX_ID_TAG:
          decodeIndexId(buf, value);
          break;
        case OFFSET_TAG:
          decodeOffset(buf, value);
          break;
        case SIZE_TAG:
          decodeSize(buf, value);
          break;
        case KEY_TAG:
          decodeKey(buf, value);
          break;
        default:
          Codec.skipUnknownField(num, buf);
      }
    }
    return value;
  }

  public static void encode(ByteBuf buf, MetaData metaData) {
    encodeIndexId(buf, metaData);
    encodeOffset(buf, metaData);
    encodeSize(buf, metaData);
    encodeKey(buf, metaData);
  }

  public static int getByteSize(MetaData metaData) {


    int length = metaData.getSsTable().getKeyType().getByteSize(metaData.getKey());

    length += Codec.computeVarInt32Size(length);
    length += KEY_TAG_ENCODE_SIZE;

    length += INDEX_ID_TAG_ENCODE_SIZE;
    length += Codec.computeVarInt64Size(Codec.encodeZigzag64(metaData.getIndexId()));

    length += OFFSET_TAG_ENCODE_SIZE;
    length += Codec.computeVarUInt32Size(Codec.encodeZigzag32(metaData.getOffset()));

    length += SIZE_TAG_ENCODE_SIZE;
    length += Codec.computeVarUInt32Size(Codec.encodeZigzag32(metaData.getSize()));

    return length;
  }
}

