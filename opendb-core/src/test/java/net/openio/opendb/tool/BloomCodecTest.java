package net.openio.opendb.tool;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.openio.opendb.mem.BloomFilter;
import net.openio.opendb.mem.KeyValueGenerator;
import net.openio.opendb.model.key.BytesKey;
import net.openio.opendb.model.key.Key;
import net.openio.opendb.tool.codec.sstable.BloomFilterProtoCodec;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;


public class BloomCodecTest {

  private final static BloomFilter bloomFilter;


  private final static int count = 1 << 10;

  private final static int pageSize = 1 << 16;

  static {
    bloomFilter = new BloomFilter(1 << 15);
  }

  private int getPageNum(int s) {
    return s % pageSize != 0 ? s / pageSize + 1 : s / pageSize;
  }

  @AfterEach
  public void endTest() {
    ByteBuf buf = Unpooled.buffer(
      getPageNum(BloomFilterProtoCodec
        .getByteSize(bloomFilter)) * pageSize);
    BloomFilterProtoCodec.encode(buf, bloomFilter);
    Assertions.assertEquals(BloomFilterProtoCodec.getByteSize(bloomFilter), buf.writerIndex());
    buf.writerIndex(buf.capacity());
    BloomFilter bloom = BloomFilterProtoCodec.decode(buf);
    Assertions.assertArrayEquals(bloomFilter.getData(), bloom.getData());
  }


  @ParameterizedTest
  @MethodSource("bytesKeyParameters")
  public void testBloom(Key key) {
    bloomFilter.add(key);
  }

  public static Stream<Arguments> bytesKeyParameters() {
    Stream<Arguments> argumentsStream = Stream.of(
      Arguments.of(new BytesKey(new byte[]{0x01, 0x02, 0x03}, KeyValueGenerator.generateRandomSequenceNumber())),
      Arguments.of(new BytesKey(new byte[]{0x04, 0x05, 0x06}, KeyValueGenerator.generateRandomSequenceNumber()))
    );

    return Stream.concat(argumentsStream, generateRandomBytesData(count));
  }

  private static Stream<Arguments> generateRandomBytesData(int count) {
    return Stream.generate(() -> Arguments.of(KeyValueGenerator.generateRandomBytesKey())).limit(count);
  }
}
