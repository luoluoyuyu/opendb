package net.openio.opendb.tool.codec.sstable;

import io.netty.buffer.ByteBuf;
import net.openio.opendb.mem.BloomFilter;
import net.openio.opendb.tool.codec.Codec;

public class BloomFilterProtoCodec {

  public static final int DATA_NUM = 1;
  public static final int DATA_TAG = 10;
  public static final int DATA_TAG_ENCODE_SIZE = 1;


  private static void encodeData(ByteBuf buf, BloomFilter filter) {
    Codec.encodeVarInt32(buf, DATA_TAG);
    Codec.encodeByteString(buf, filter.getData());
  }

  private static void decodeData(ByteBuf buf, BloomFilter filter) {
    filter.setData(Codec.decodeByteString(buf, Codec.decodeVarInt32(buf)));

  }

  public static BloomFilter decode(ByteBuf buf) {
    BloomFilter value = new BloomFilter();
    while (buf.readerIndex() < buf.writerIndex()) {
      int num = Codec.decodeVarInt32(buf);
      switch (num) {
        case DATA_TAG:
          decodeData(buf, value);
          break;
        default:
          return value;
      }
    }
    return value;
  }

  public static void encode(ByteBuf buf, BloomFilter filter) {
    encodeData(buf, filter);
    if (buf.capacity() > buf.writerIndex()) {
      buf.writeByte(0);
    }
  }

  public static int getByteSize(BloomFilter filter) {
    int length = filter.getData().length;
    length += Codec.computeVarInt32Size(length);
    length += DATA_TAG_ENCODE_SIZE;
    return length;

  }
}
