package net.openio.opendb.tool.codec.sstable;

import io.netty.buffer.ByteBuf;
import net.openio.opendb.model.value.Value;
import net.openio.opendb.model.value.ValueType;
import net.openio.opendb.storage.sstable.DataBlock;
import net.openio.opendb.storage.sstable.SSTable;
import net.openio.opendb.tool.codec.Codec;

import java.util.ArrayList;
import java.util.List;


public class DataBlockProtoCodec {

  public static final int ID_NUM = 1;
  public static final int ID_TAG = 9;
  public static final int ID_TAG_ENCODE_SIZE = 1;


  public static final int SIZE_NUM = 2;
  public static final int SIZE_TAG = 21;
  public static final int SIZE_TAG_ENCODE_SIZE = 1;


  public static final int VALUE_NUM_NUM = 3;
  public static final int VALUE_NUM_TAG = 29;
  public static final int VALUE_NUM_TAG_ENCODE_SIZE = 1;


  public static final int VALUE_NUM = 4;
  public static final int VALUE_TAG = 34;
  public static final int VALUE_TAG_ENCODE_SIZE = 1;

  public static int getHeadLength() {
    int length = ID_TAG_ENCODE_SIZE + 8;
    length += SIZE_TAG_ENCODE_SIZE + 4;
    length += VALUE_NUM_TAG_ENCODE_SIZE + 4;
    return length;
  }

  public static int getByteSize(DataBlock dataBlock) {
    int length = ID_TAG_ENCODE_SIZE + 8;
    length += SIZE_TAG_ENCODE_SIZE + 4;
    length += VALUE_NUM_TAG_ENCODE_SIZE + 4;
    length += VALUE_TAG_ENCODE_SIZE * dataBlock.getValues().size();
    for (Value value : dataBlock.getValues()) {
      int i = dataBlock.getValueType().getByteSize(value);
      length += Codec.computeVarInt32Size(i);
      length += i;
    }
    return length;
  }

  public static DataBlock decode(ByteBuf buf, SSTable ssTable, int offset) {
    DataBlock dataBlock = new DataBlock();
    dataBlock.setSsTable(ssTable);
    dataBlock.setOfferSet(offset);
    while (buf.readerIndex() < buf.writerIndex()) {
      int num = Codec.decodeVarInt32(buf);
      switch (num) {
        case ID_TAG:
          decodeId(buf, dataBlock);
          break;
        case SIZE_TAG:
          decodeSize(buf, dataBlock);
          break;
        case VALUE_NUM_TAG:
          decodeValueNum(buf, dataBlock);
          dataBlock.setValues(new ArrayList<>(dataBlock.getValueNum()));
          break;
        case VALUE_TAG:
          decodeValue(buf, dataBlock);
          break;
        default:
          if (dataBlock.getValues().size() >= dataBlock.getValueNum()){
            return dataBlock;
          }
          Codec.skipUnknownField(num, buf);
      }
    }
    return dataBlock;
  }


  public static void encode(ByteBuf buf, DataBlock dataBlock) {
    encodeId(buf, dataBlock);

    encodeValueNum(buf, dataBlock);

    encodeSize(buf, dataBlock);


    encodeValue(buf, dataBlock);
    if (buf.capacity() > buf.writerIndex()) {
      buf.writeByte(0);
    }
  }


  public static int getAddValueSize(DataBlock dataBlock, Value value) {
    int l = getValueSize(value, dataBlock.getValueType());
    l += Codec.computeVarInt32Size(l);
    return l + DataBlockProtoCodec.VALUE_NUM_TAG_ENCODE_SIZE;
  }

  public static int getAddValueSize(DataBlock dataBlock, List<Value> value) {
    int length = 0;
    for (Value v : value) {
      length += getAddValueSize(dataBlock, v);
    }
    return length;
  }

  private static int getValueSize(Value value, ValueType valueType) {
    return valueType.getByteSize(value);
  }


  private static void encodeId(ByteBuf buf, DataBlock dataBlock) {
    Codec.encodeVarInt32(buf, ID_TAG);
    Codec.encode64(buf, dataBlock.getId());
  }

  private static void decodeId(ByteBuf buf, DataBlock dataBlock) {
    dataBlock.setId(Codec.decode64(buf));
  }


  private static void encodeSize(ByteBuf buf, DataBlock dataBlock) {
    Codec.encodeVarInt32(buf, SIZE_TAG);
    Codec.encode32(buf, dataBlock.getSize());
  }

  private static void decodeSize(ByteBuf buf, DataBlock dataBlock) {
    dataBlock.setSize(Codec.decode32(buf));
  }


  private static void encodeValueNum(ByteBuf buf, DataBlock dataBlock) {
    Codec.encodeVarInt32(buf, VALUE_NUM_TAG);
    Codec.encode32(buf, dataBlock.getValueNum());
  }

  private static void decodeValueNum(ByteBuf buf, DataBlock dataBlock) {
    dataBlock.setValueNum(Codec.decode32(buf));
  }


  private static void decodeValue(ByteBuf buf, DataBlock dataBlock) {
    dataBlock.addNotComSize(dataBlock.getValueType().decode(buf, Codec.decodeVarInt32(buf)));
  }

  private static void encodeValue(ByteBuf buf, DataBlock dataBlock) {
    for (Value value : dataBlock.getValues()) {
      Codec.encodeVarInt32(buf, VALUE_TAG);
      Codec.encodeVarInt32(buf, dataBlock.getValueType().getByteSize(value));
      dataBlock.getValueType().encode(buf, value);
    }
  }

}
