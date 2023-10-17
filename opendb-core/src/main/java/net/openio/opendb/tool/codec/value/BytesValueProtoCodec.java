package net.openio.opendb.tool.codec.value;

import io.netty.buffer.ByteBuf;
import net.openio.opendb.model.OperationType;
import net.openio.opendb.model.value.BytesValue;
import net.openio.opendb.model.value.Value;
import net.openio.opendb.tool.codec.Codec;


public class BytesValueProtoCodec implements ValueProtoCodec {

  public static final int VALUE_NUM = 1;
  public static final int VALUE_TAG = 10;
  public static final int VALUE_TAG_ENCODE_SIZE = 1;


  public static final int TYPE_NUM = 2;
  public static final int TYPE_TAG = 16;
  public static final int TYPE_TAG_ENCODE_SIZE = 1;

  private void encodeValue(ByteBuf buf, Value value) {
    Codec.encodeVarInt32(buf, VALUE_TAG);
    Codec.encodeByteString(buf, (byte[]) value.getValue());
  }

  private void decodeValue(ByteBuf buf, Value value) {
    value.setValue(Codec.decodeByteString(buf, Codec.decodeVarInt32(buf)));
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
    BytesValue value = new BytesValue();
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
    int length = ((byte[]) value.getValue()).length;
    length += Codec
      .computeVarInt32Size(length);
    length += VALUE_TAG_ENCODE_SIZE;
    length += TYPE_TAG_ENCODE_SIZE;
    length += Codec.computeVarInt32Size(value.getType().getNum());
    return length;
  }
}
