package net.openio.opendb.tool.codec.key;

import io.netty.buffer.ByteBuf;
import net.openio.opendb.model.SequenceNumber;
import net.openio.opendb.model.key.FloatKey;
import net.openio.opendb.model.key.Key;
import net.openio.opendb.tool.codec.Codec;
import net.openio.opendb.tool.codec.sstable.SequenceNumberProtoCodec;


public class FloatKeyProtoCodec implements KeyProtoCodec {

  public static final int VALUE_NUM = 1;
  public static final int VALUE_TAG = 13;
  public static final int VALUE_TAG_ENCODE_SIZE = 1;


  public static final int SEQUENCE_NUMBER_NUM = 2;
  public static final int SEQUENCE_NUMBER_TAG = 18;
  public static final int SEQUENCE_NUMBER_TAG_ENCODE_SIZE = 1;


  private void encodeValue(ByteBuf buf, Key key) {
    Codec.encodeVarInt32(buf, VALUE_TAG);
    Codec.encodeFloat(buf, (float) key.getKey());
  }

  private void decodeValue(ByteBuf buf, Key key) {
    key.setKey(Codec.decodeFloat(buf));
  }

  private void decodeSequenceNumber(ByteBuf buf, Key key) {
    SequenceNumber value = SequenceNumberProtoCodec
      .decode(buf, Codec.decodeVarInt32(buf));
    key.setSequenceNumber(value);
  }

  private void encodeSequenceNumber(ByteBuf buf, Key key) {
    Codec.encodeVarInt32(buf, SEQUENCE_NUMBER_TAG);
    Codec.encodeVarInt32(buf,
      SequenceNumberProtoCodec.getByteSize(key.getSequenceNumber()));
    SequenceNumberProtoCodec.encode(buf, key.getSequenceNumber());
  }

  @Override
  public Key decode(ByteBuf buf, int length) {
    FloatKey value = new FloatKey();
    int end = buf.readerIndex() + length;
    while (buf.readerIndex() < end) {
      int num = Codec.decodeVarInt32(buf);
      switch (num) {
        case VALUE_TAG:
          decodeValue(buf, value);
          break;
        case SEQUENCE_NUMBER_TAG:
          decodeSequenceNumber(buf, value);
          break;
        default:
          Codec.skipUnknownField(num, buf);
      }
    }
    return value;
  }

  @Override
  public void encode(ByteBuf buf, Key key) {
    encodeValue(buf, key);
    encodeSequenceNumber(buf, key);
  }

  @Override
  public int getByteSize(Key key) {
    int length = VALUE_TAG_ENCODE_SIZE;
    length += 4;
    length += SEQUENCE_NUMBER_TAG_ENCODE_SIZE;
    int l = SequenceNumberProtoCodec.getByteSize(key.getSequenceNumber());
    length += Codec.computeVarInt32Size(l) + l;
    return length;
  }
}
