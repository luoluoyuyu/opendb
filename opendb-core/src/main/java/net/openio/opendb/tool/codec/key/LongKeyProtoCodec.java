package net.openio.opendb.tool.codec.key;


import io.netty.buffer.ByteBuf;
import net.openio.opendb.model.SequenceNumber;
import net.openio.opendb.model.key.Key;
import net.openio.opendb.model.key.LongKey;
import net.openio.opendb.tool.codec.Codec;
import net.openio.opendb.tool.codec.sstable.SequenceNumberProtoCodec;

public class LongKeyProtoCodec implements KeyProtoCodec {

  public static final int VALUE_NUM = 1;
  public static final int VALUE_TAG = 8;
  public static final int VALUE_TAG_ENCODE_SIZE = 1;


  public static final int SEQUENCE_NUMBER_NUM = 2;
  public static final int SEQUENCE_NUMBER_TAG = 18;
  public static final int SEQUENCE_NUMBER_TAG_ENCODE_SIZE = 1;


  private void encodeValue(ByteBuf buf, Key key) {
    Codec.encodeVarInt32(buf, VALUE_TAG);
    Codec.encodeVarInt64(buf, Codec.encodeZigzag64((long) key.getKey()));
  }

  private void decodeValue(ByteBuf buf, Key key) {
    key.setKey(Codec.decodeZigzag64(Codec.decodeVarInt64(buf)));
  }

  private void decodeSequenceNumber(ByteBuf buf, Key longKey) {
    SequenceNumber value = SequenceNumberProtoCodec
      .decode(buf, Codec.decodeVarInt32(buf));
    longKey.setSequenceNumber(value);
  }

  private void encodeSequenceNumber(ByteBuf buf, Key key) {
    Codec.encodeVarInt32(buf, SEQUENCE_NUMBER_TAG);
    Codec.encodeVarInt32(buf,
      SequenceNumberProtoCodec.getByteSize(key.getSequenceNumber()));
    SequenceNumberProtoCodec.encode(buf, key.getSequenceNumber());
  }

  public Key decode(ByteBuf buf, int length) {
    LongKey value = new LongKey();
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

  public void encode(ByteBuf buf, Key key) {
    encodeValue(buf, key);
    encodeSequenceNumber(buf, key);
  }

  public int getByteSize(Key key) {
    int length = VALUE_TAG_ENCODE_SIZE;
    length += Codec.computeVarInt64Size(
      Codec.encodeZigzag64((long) key.getKey()));
    length += SEQUENCE_NUMBER_TAG_ENCODE_SIZE;
    int l = SequenceNumberProtoCodec.getByteSize(key.getSequenceNumber());
    length += Codec.computeVarInt32Size(l) + l;
    return length;
  }

}
