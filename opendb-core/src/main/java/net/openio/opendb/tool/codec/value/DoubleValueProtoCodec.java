package net.openio.opendb.tool.codec.value;

import io.netty.buffer.ByteBuf;
import net.openio.opendb.model.OperationType;
import net.openio.opendb.model.value.DoubleValue;
import net.openio.opendb.model.value.Value;
import net.openio.opendb.tool.codec.Codec;


public class DoubleValueProtoCodec implements ValueProtoCodec {


  public static final int VALUE_NUM = 1;
  public static final int VALUE_TAG = 9;
  public static final int VALUE_TAG_ENCODE_SIZE = 1;


  public static final int TYPE_NUM = 2;
  public static final int TYPE_TAG = 16;
  public static final int TYPE_TAG_ENCODE_SIZE = 1;

  private void encodeValue(ByteBuf buf, Value value) {
    Codec.encodeVarInt32(buf, VALUE_TAG);
    Codec.encodeDouble(buf, (double) value.getValue());
  }

  private void decodeValue(ByteBuf buf, Value value) {
    value.setValue(Codec.decodeDouble(buf));
  }

  private void encodeType(ByteBuf buf, Value value) {
    Codec.encodeVarInt32(buf, TYPE_TAG);
    Codec.encodeVarInt32(buf, value.getType().getNum());
  }

  private void decodeType(ByteBuf buf, Value value) {
    value.setType(OperationType.get(Codec.decodeVarInt32(buf)));
  }

  @Override
  public Value decode(ByteBuf buf, int length) {
    DoubleValue value = new DoubleValue();
    int end = buf.readerIndex() + length;
    while (buf.readerIndex() < end) {
      int num = Codec.decodeVarInt32(buf);
      switch (num) {
        case VALUE_TAG:
          decodeValue(buf, value);
          break;
        case TYPE_TAG:
          decodeType(buf, value);
          break;
        default:
          Codec.skipUnknownField(num, buf);
      }
    }

    return value;
  }

  @Override
  public void encode(ByteBuf buf, Value value) {

    this.encodeValue(buf, value);

    this.encodeType(buf, value);
  }

  @Override
  public int getByteSize(Value value) {
    int length = VALUE_TAG_ENCODE_SIZE;
    length += 8;
    length += TYPE_TAG_ENCODE_SIZE;
    length += Codec.computeVarInt32Size(value.getType().getNum());
    return length;
  }
}
