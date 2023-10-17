package net.openio.opendb.tool;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.openio.opendb.model.SequenceNumber;
import net.openio.opendb.model.key.IntKey;
import net.openio.opendb.model.key.LongKey;
import net.openio.opendb.model.key.BytesKey;
import net.openio.opendb.model.key.DoubleKey;
import net.openio.opendb.model.key.FloatKey;
import net.openio.opendb.model.key.StringKey;
import net.openio.opendb.model.key.Key;
import net.openio.opendb.tool.codec.sstable.SequenceNumberProtoCodec;
import net.openio.opendb.tool.codec.key.IntKeyProtoCodec;
import net.openio.opendb.tool.codec.key.LongKeyProtoCodec;
import net.openio.opendb.tool.codec.key.BytesKeyProtoCodec;
import net.openio.opendb.tool.codec.key.DoubleKeyProtoCodec;
import net.openio.opendb.tool.codec.key.FloatKeyProtoCodec;
import net.openio.opendb.tool.codec.key.StringKeyProtoCodec;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.nio.charset.StandardCharsets;
import java.util.stream.Stream;

public class KeyCodecTest {


  @ParameterizedTest
  @MethodSource("intKeyParameters")
  public void IntKeyTest(int a, long time) {
    IntKey key = new IntKey(a, new SequenceNumber(time));
    IntKeyProtoCodec protoCodec = new IntKeyProtoCodec();
    int length = 0;
    ByteBuf buf = Unpooled.buffer(length = protoCodec.getByteSize(key));
    protoCodec.encode(buf, key);
    Key key1 = protoCodec.decode(buf, length);
    Assertions.assertEquals(protoCodec.getByteSize(key), buf.writerIndex());
    Assertions.assertEquals(key1.getKey(), key.getKey());
    Assertions.assertEquals(key1.getSequenceNumber().getTimes(), key.getSequenceNumber().getTimes());
  }


  @ParameterizedTest
  @MethodSource("longKeyParameters")
  public void LongKeyTest(long a, long time) {
    LongKey key = new LongKey(a, new SequenceNumber(time));
    LongKeyProtoCodec protoCodec = new LongKeyProtoCodec();
    int length = 0;
    ByteBuf buf = Unpooled.buffer(length = protoCodec.getByteSize(key));
    protoCodec.encode(buf, key);
    Key key1 = protoCodec.decode(buf, length);
    Assertions.assertEquals(protoCodec.getByteSize(key), buf.writerIndex());
    Assertions.assertEquals(key1.getKey(), key.getKey());
    Assertions.assertEquals(key1.getSequenceNumber().getTimes(), key.getSequenceNumber().getTimes());
  }


  @ParameterizedTest
  @MethodSource("doubleKeyParameters")
  public void DoubleKeyTest(double value, long time) {
    DoubleKey key = new DoubleKey(value, new SequenceNumber(time));
    DoubleKeyProtoCodec protoCodec = new DoubleKeyProtoCodec();
    int length = 0;
    ByteBuf buf = Unpooled.buffer(length = protoCodec.getByteSize(key));
    protoCodec.encode(buf, key);
    Key key1 = protoCodec.decode(buf, length);

    ByteBuf buf1 = Unpooled.buffer(length = SequenceNumberProtoCodec.getByteSize(key.getSequenceNumber()));
    Assertions.assertEquals(protoCodec.getByteSize(key), buf.writerIndex());
    Assertions.assertEquals(key1.getKey(), key.getKey());
    Assertions.assertEquals(key1.getSequenceNumber().getTimes(), key.getSequenceNumber().getTimes());
  }


  @ParameterizedTest
  @MethodSource("floatKeyParameters")
  public void FloatKeyTest(float value, long time) {
    FloatKey key = new FloatKey(value, new SequenceNumber(time));
    FloatKeyProtoCodec protoCodec = new FloatKeyProtoCodec();
    int length = 0;
    ByteBuf buf = Unpooled.buffer(length = protoCodec.getByteSize(key));
    protoCodec.encode(buf, key);
    Key key1 = protoCodec.decode(buf, length);
    Assertions.assertEquals(protoCodec.getByteSize(key), buf.writerIndex());
    Assertions.assertEquals(key1.getKey(), key.getKey());
    Assertions.assertEquals(key1.getSequenceNumber().getTimes(), key.getSequenceNumber().getTimes());
  }


  @ParameterizedTest
  @MethodSource("stringKeyParameters")
  public void StringKeyTest(String value, long time) {
    StringKey key = new StringKey(value, new SequenceNumber(time));
    StringKeyProtoCodec protoCodec = new StringKeyProtoCodec();
    int length = 0;
    ByteBuf buf = Unpooled.buffer(length = protoCodec.getByteSize(key));
    protoCodec.encode(buf, key);
    Key key1 = protoCodec.decode(buf, length);
    Assertions.assertEquals(protoCodec.getByteSize(key), buf.writerIndex());
    Assertions.assertEquals(key1.getKey(), key.getKey());
    Assertions.assertEquals(key1.getSequenceNumber().getTimes(), key.getSequenceNumber().getTimes());
  }


  @ParameterizedTest
  @MethodSource("stringKeyParameters")
  public void BytesKeyTest(String data, long time) {
    byte[] byteArray = data.getBytes(StandardCharsets.UTF_8);
    BytesKey key = new BytesKey(byteArray, new SequenceNumber(time));
    BytesKeyProtoCodec protoCodec = new BytesKeyProtoCodec();
    int length = 0;
    ByteBuf buf = Unpooled.buffer(length = protoCodec.getByteSize(key));
    protoCodec.encode(buf, key);
    Key key1 = protoCodec.decode(buf, length);
    Assertions.assertEquals(protoCodec.getByteSize(key), buf.writerIndex());
    Assertions.assertArrayEquals((byte[]) key1.getKey(), (byte[]) key.getKey());
    Assertions.assertEquals(key1.getSequenceNumber().getTimes(), key.getSequenceNumber().getTimes());
  }


  public static Stream<Object[]> intKeyParameters() {
    return Stream.of(
      new Object[]{1, 1},
      new Object[]{0, 0},
      new Object[]{-1, -2},
      new Object[]{Integer.MAX_VALUE, Long.MAX_VALUE},
      new Object[]{Integer.MIN_VALUE, Long.MIN_VALUE}
    );
  }

  public static Stream<Object[]> longKeyParameters() {
    return Stream.of(
      new Object[]{1L, 1L},
      new Object[]{0L, 0L},
      new Object[]{-1L, -2L},
      new Object[]{Long.MAX_VALUE, Long.MAX_VALUE},
      new Object[]{Long.MIN_VALUE, Long.MIN_VALUE}
    );
  }

  public static Stream<Object[]> doubleKeyParameters() {
    return Stream.of(
      new Object[]{1.0, 1},
      new Object[]{0.5, 0},
      new Object[]{-2.5, -2},
      new Object[]{Double.MAX_VALUE, Long.MAX_VALUE},
      new Object[]{Double.MIN_VALUE, Long.MIN_VALUE}
    );
  }

  public static Stream<Object[]> floatKeyParameters() {
    return Stream.of(
      new Object[]{1.0f, 1},
      new Object[]{0.0f, 0},
      new Object[]{-1.5f, -2},
      new Object[]{Float.MAX_VALUE, Long.MAX_VALUE},
      new Object[]{Float.MIN_VALUE, Long.MIN_VALUE}
    );
  }

  public static Stream<Object[]> stringKeyParameters() {
    return Stream.of(
      new Object[]{"Hello", 1},
      new Object[]{"こんにちは", 0},
      new Object[]{"안녕하세요", Long.MAX_VALUE},
      new Object[]{"مرحبا", 2},
      new Object[]{"नमस्ते", 3},
      new Object[]{"Hello", 1},
      new Object[]{"Test", 0},
      new Object[]{"Example", -2},
      new Object[]{"LongString", Long.MAX_VALUE},
      new Object[]{"Short", Long.MIN_VALUE},
      new Object[]{"你好", Long.MIN_VALUE}
    );
  }

}
