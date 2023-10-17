package net.openio.opendb.tool.codec.sstable;

import io.netty.buffer.ByteBuf;
import net.openio.opendb.model.SequenceNumber;
import net.openio.opendb.tool.codec.Codec;


public class SequenceNumberProtoCodec {


  public static final int TIMES_NUM = 1;
  public static final int TIMES_TAG = 8;
  public static final int TIMES_TAG_ENCODE_SIZE = 1;

  public static SequenceNumber decode(ByteBuf buf, int length) {
    SequenceNumber sequenceNumber = new SequenceNumber();
    int fIndex = buf.readerIndex();
    while (buf.readerIndex() < fIndex + length) {
      int num = Codec.decodeVarInt32(buf);
      switch (num) {
        case TIMES_TAG:
          decodeTimes(buf, sequenceNumber);
          break;
        default:
          Codec.skipUnknownField(num, buf);
      }
    }
    return sequenceNumber;
  }

  public static int getByteSize(SequenceNumber sequenceNumber) {
    return TIMES_TAG_ENCODE_SIZE + Codec
      .computeVarInt64Size(Codec.encodeZigzag64(sequenceNumber.getTimes()));
  }

  private static void decodeTimes(ByteBuf buf, SequenceNumber sequenceNumber) {
    Long value = Codec.decodeZigzag64(Codec.decodeVarInt64(buf));
    sequenceNumber.setTimes(value);
  }


  public static void encode(ByteBuf buf, SequenceNumber sequenceNumber) {
    Codec.encodeVarInt32(buf, TIMES_TAG);
    Codec.encodeVarInt64(buf, Codec.encodeZigzag64(sequenceNumber.getTimes()));
  }


}
