package net.openio.opendb.tool.codec.meta;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import net.openio.opendb.db.ColumnFamily;
import net.openio.opendb.model.key.KeyType;
import net.openio.opendb.model.value.ValueType;
import net.openio.opendb.tool.codec.Codec;

public class ColumnFamilyProtoCodec {

  public static final int COLUMNFAMILYID_NUM = 1;
  public static final int COLUMNFAMILYID_TAG = 8;
  public static final int COLUMNFAMILYID_TAG_ENCODE_SIZE = 1;

  public static final int NAME_NUM = 2;
  public static final int NAME_TAG = 18;
  public static final int NAME_TAG_ENCODE_SIZE = 1;


  public static final int KEYTYPE_NUM = 3;
  public static final int KEYTYPE_TAG = 24;
  public static final int KEYTYPE_TAG_ENCODE_SIZE = 1;

  public static final int VALUETYPE_NUM = 4;
  public static final int VALUETYPE_TAG = 32;
  public static final int VALUETYPE_TAG_ENCODE_SIZE = 1;


  public static final int LEVELS_NUM = 5;
  public static final int LEVELS_TAG = 42;
  public static final int LEVELS_TAG_ENCODE_SIZE = 1;

  public static final int STORAGEBLOCKSIZE_NUM = 6;
  public static final int STORAGEBLOCKSIZE_TAG = 48;
  public static final int STORAGEBLOCKSIZE_TAG_ENCODE_SIZE = 1;

  public static final int CREATETIME_NUM = 7;
  public static final int CREATETIME_TAG = 56;
  public static final int CREATETIME_TAG_ENCODE_SIZE = 1;


  private static void encodeColumnFamilyId(ByteBuf buf, ColumnFamily columnFamily) {
    Codec.encodeVarInt32(buf, COLUMNFAMILYID_TAG);
    Codec.encodeVarInt64(buf, columnFamily.getColumnFamilyId());
  }

  private static void decodeColumnFamilyId(ByteBuf buf, ColumnFamily columnFamily) {
    columnFamily.setColumnFamilyId(Codec.decodeVarInt64(buf));
  }

  private static void encodeName(ByteBuf buf, ColumnFamily columnFamily) {
    Codec.encodeVarInt32(buf, NAME_TAG);
    Codec.encodeString(buf, columnFamily.getName());
  }

  private static void decodeName(ByteBuf buf, ColumnFamily columnFamily) {
    columnFamily.setName(Codec.decodeString(buf, Codec.decodeVarInt32(buf)));
  }

  private static void encodeKeyType(ByteBuf buf, ColumnFamily columnFamily) {
    Codec.encodeVarInt32(buf, KEYTYPE_TAG);
    Codec.encodeVarInt32(buf, columnFamily.getValueType().getNum());
  }

  private static void decodeKeyType(ByteBuf buf, ColumnFamily columnFamily) {
    columnFamily.setKeyType(KeyType.get(Codec.decodeVarInt32(buf)));
  }

  private static void encodeValueType(ByteBuf buf, ColumnFamily columnFamily) {
    Codec.encodeVarInt32(buf, VALUETYPE_TAG);
    Codec.encodeVarInt32(buf, columnFamily.getValueType().getNum());
  }

  private static void decodeValueType(ByteBuf buf, ColumnFamily columnFamily) {
    columnFamily.setValueType(ValueType.get(Codec.decodeVarInt32(buf)));
  }

  private static void encodeLevels(ByteBuf buf, ColumnFamily columnFamily) {
    Codec.encodeVarInt32(buf, LEVELS_TAG);
    Codec.encodeVarInt32(buf, LevelsProtoCodec.getByteSize(columnFamily.getLevels()));
    LevelsProtoCodec.encode(buf, columnFamily.getLevels());
  }

  private static void decodeLevels(ByteBuf buf, ColumnFamily columnFamily) {
    columnFamily.setLevels(LevelsProtoCodec.decode(buf, Codec.decodeVarInt32(buf)));
  }

  private static void encodeStorageBlockSize(ByteBuf buf, ColumnFamily columnFamily) {
    Codec.encodeVarInt32(buf, STORAGEBLOCKSIZE_TAG);
    Codec.encodeVarInt32(buf, columnFamily.getStorageBlockSize());
  }

  private static void decodeStorageBlockSize(ByteBuf buf, ColumnFamily columnFamily) {
    columnFamily.setStorageBlockSize(Codec.decodeVarInt32(buf));
  }

  private static void encodeCreateTime(ByteBuf buf, ColumnFamily columnFamily) {
    Codec.encodeVarInt32(buf, CREATETIME_TAG);
    Codec.encodeVarInt64(buf, columnFamily.getCreatTime());
  }

  private static void decodeCreateTime(ByteBuf buf, ColumnFamily columnFamily) {
    columnFamily.setCreatTime(Codec.decodeVarInt64(buf));
  }

  public static void encode(ByteBuf buf, ColumnFamily columnFamily) {
    encodeColumnFamilyId(buf, columnFamily);
    encodeName(buf, columnFamily);
    encodeKeyType(buf, columnFamily);
    encodeValueType(buf, columnFamily);
    encodeLevels(buf, columnFamily);
    encodeStorageBlockSize(buf, columnFamily);
    encodeCreateTime(buf, columnFamily);


  }

  public static ColumnFamily decode(ByteBuf buf, int length) {
    ColumnFamily value = new ColumnFamily();
    int end = buf.readerIndex() + length;
    while (buf.readerIndex() < end) {
      int num = Codec.decodeVarInt32(buf);
      switch (num) {
        case COLUMNFAMILYID_TAG:
          decodeColumnFamilyId(buf, value);
          break;
        case NAME_TAG:
          decodeName(buf, value);
          break;
        case KEYTYPE_TAG:
          decodeKeyType(buf, value);
          break;
        case VALUETYPE_TAG:
          decodeValueType(buf, value);
          break;
        case LEVELS_TAG:
          decodeLevels(buf, value);
          break;
        case STORAGEBLOCKSIZE_TAG:
          decodeStorageBlockSize(buf, value);
          break;
        case CREATETIME_TAG:
          decodeCreateTime(buf, value);
          break;
        default:
          Codec.skipUnknownField(num, buf);
      }
    }
    return value;
  }

  public static int getByteSize(ColumnFamily columnFamily) {
    int length = COLUMNFAMILYID_TAG_ENCODE_SIZE;
    length += Codec.computeVarInt64Size(columnFamily.getColumnFamilyId());

    length += NAME_TAG_ENCODE_SIZE;
    int i = 0;
    i = ByteBufUtil.utf8Bytes(columnFamily.getName());
    length += Codec.computeVarInt32Size(i);

    length += KEYTYPE_TAG_ENCODE_SIZE;
    length += Codec.computeVarInt32Size(columnFamily.getKeyType().getNum());

    length += VALUETYPE_TAG_ENCODE_SIZE;
    length += Codec.computeVarInt32Size(columnFamily.getValueType().getNum());


    length += LEVELS_TAG_ENCODE_SIZE;
    i += LevelsProtoCodec.getByteSize(columnFamily.getLevels());
    length += Codec.computeVarInt32Size(i) + i;

    length += STORAGEBLOCKSIZE_TAG_ENCODE_SIZE;
    length += Codec.computeVarInt32Size(columnFamily.getStorageBlockSize());


    length += CREATETIME_TAG_ENCODE_SIZE;
    length += Codec.computeVarInt64Size(columnFamily.getCreatTime());

    return length;
  }


}
