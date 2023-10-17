/**
 * Licensed to the OpenIO.Net under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.openio.opendb.tool;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.openio.opendb.model.OperationType;
import net.openio.opendb.model.value.Value;
import net.openio.opendb.model.value.IntValue;
import net.openio.opendb.model.value.LongValue;
import net.openio.opendb.model.value.StringValue;
import net.openio.opendb.model.value.BytesValue;
import net.openio.opendb.model.value.DoubleValue;
import net.openio.opendb.model.value.FloatValue;
import net.openio.opendb.tool.codec.value.IntValueProtoCodec;
import net.openio.opendb.tool.codec.value.LongValueProtoCodec;
import net.openio.opendb.tool.codec.value.BytesValueProtoCodec;
import net.openio.opendb.tool.codec.value.DoubleValueProtoCodec;
import net.openio.opendb.tool.codec.value.FloatValueProtoCodec;
import net.openio.opendb.tool.codec.value.StringValueProtoCodec;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.nio.charset.StandardCharsets;
import java.util.stream.Stream;

public class ValueCodecTest {


  @ParameterizedTest
  @MethodSource("intValueParameters")
  public void intValueTest(int a, OperationType type) {

    IntValue value = new IntValue(a, type);
    IntValueProtoCodec protoCodec = new IntValueProtoCodec();
    int length = protoCodec.getByteSize(value);
    ByteBuf buf = Unpooled.buffer(length);
    protoCodec.encode(buf, value);
    Value value1 = protoCodec.decode(buf, length);
    Assertions.assertEquals(protoCodec.getByteSize(value), buf.writerIndex());
    Assertions.assertEquals(a, value1.getValue());
    Assertions.assertEquals(type, value1.getType());
  }

  @ParameterizedTest
  @MethodSource("longValueParameters")
  public void longValueTest(long value, OperationType type) {
    LongValue longValue = new LongValue(value, type);
    LongValueProtoCodec protoCodec = new LongValueProtoCodec();
    int length = protoCodec.getByteSize(longValue);
    ByteBuf buf = Unpooled.buffer(length);
    protoCodec.encode(buf, longValue);
    Value value1 = protoCodec.decode(buf, length);
    Assertions.assertEquals(protoCodec.getByteSize(longValue), buf.writerIndex());
    Assertions.assertEquals(value, value1.getValue());
    Assertions.assertEquals(type, value1.getType());
  }

  @ParameterizedTest
  @MethodSource("doubleValueParameters")
  public void doubleValueTest(double value, OperationType type) {
    DoubleValue doubleValue = new DoubleValue(value, type);
    DoubleValueProtoCodec protoCodec = new DoubleValueProtoCodec();
    int length = protoCodec.getByteSize(doubleValue);
    ByteBuf buf = Unpooled.buffer(length);
    protoCodec.encode(buf, doubleValue);
    Value value1 = protoCodec.decode(buf, length);
    Assertions.assertEquals(protoCodec.getByteSize(doubleValue), buf.writerIndex());
    Assertions.assertEquals(value, value1.getValue());
    Assertions.assertEquals(type, value1.getType());
  }

  @ParameterizedTest
  @MethodSource("floatValueParameters")
  public void floatValueTest(float value, OperationType type) {
    FloatValue floatValue = new FloatValue(value, type);
    FloatValueProtoCodec protoCodec = new FloatValueProtoCodec();
    int length = protoCodec.getByteSize(floatValue);
    ByteBuf buf = Unpooled.buffer(length);
    protoCodec.encode(buf, floatValue);
    Value value1 = protoCodec.decode(buf, length);
    Assertions.assertEquals(protoCodec.getByteSize(floatValue), buf.writerIndex());
    Assertions.assertEquals(value, value1.getValue());
    Assertions.assertEquals(type, value1.getType());
  }

  @ParameterizedTest
  @MethodSource("stringValueParameters")
  public void stringValueTest(String value, OperationType type) {
    StringValue stringValue = new StringValue(value, type);
    StringValueProtoCodec protoCodec = new StringValueProtoCodec();
    int length = protoCodec.getByteSize(stringValue);
    ByteBuf buf = Unpooled.buffer(length);
    protoCodec.encode(buf, stringValue);
    Value value1 = protoCodec.decode(buf, length);
    Assertions.assertEquals(protoCodec.getByteSize(stringValue), buf.writerIndex());
    Assertions.assertEquals(value, value1.getValue());
    Assertions.assertEquals(type, value1.getType());
  }

  @ParameterizedTest
  @MethodSource("stringValueParameters")
  public void bytesValueTest(String data, OperationType type) {
    byte[] byteArray = data.getBytes(StandardCharsets.UTF_8);
    BytesValue bytesValue = new BytesValue(byteArray, type);
    BytesValueProtoCodec protoCodec = new BytesValueProtoCodec();
    int length = protoCodec.getByteSize(bytesValue);
    ByteBuf buf = Unpooled.buffer(length);
    protoCodec.encode(buf, bytesValue);
    Value value1 = protoCodec.decode(buf, length);
    Assertions.assertEquals(protoCodec.getByteSize(bytesValue), buf.writerIndex());
    Assertions.assertArrayEquals(byteArray, (byte[]) value1.getValue());
    Assertions.assertEquals(type, value1.getType());
  }

  public static Stream<Object[]> stringValueParameters() {
    return Stream.of(
      new Object[]{"Hello", OperationType.delete},
      new Object[]{"Test", OperationType.insert},
      new Object[]{"StreamPipes", OperationType.update},
      new Object[]{"Apache", OperationType.delete},
      new Object[]{"Java", OperationType.insert},
      new Object[]{"Hello", OperationType.update},
      new Object[]{"こんにちは", OperationType.insert},
      new Object[]{"안녕하세요", OperationType.insert},
      new Object[]{"مرحبا", OperationType.update},
      new Object[]{"नमस्ते", OperationType.insert},
      new Object[]{"Hello", OperationType.insert},
      new Object[]{"Test", OperationType.update},
      new Object[]{"Example", OperationType.insert},
      new Object[]{"LongString", OperationType.insert},
      new Object[]{"Short", OperationType.update},
      new Object[]{"你好", OperationType.insert}
    );
  }

  public static Stream<Object[]> floatValueParameters() {
    return Stream.of(
      new Object[]{1.0f, OperationType.delete},
      new Object[]{0.0f, OperationType.insert},
      new Object[]{-1.0f, OperationType.update},
      new Object[]{Float.MAX_VALUE, OperationType.delete},
      new Object[]{Float.MIN_VALUE, OperationType.insert}
    );
  }

  public static Stream<Object[]> doubleValueParameters() {
    return Stream.of(
      new Object[]{1.0, OperationType.update},
      new Object[]{0.0, OperationType.insert},
      new Object[]{-1.0, OperationType.delete},
      new Object[]{Double.MAX_VALUE, OperationType.delete},
      new Object[]{Double.MIN_VALUE, OperationType.insert}
    );
  }

  public static Stream<Object[]> longValueParameters() {
    return Stream.of(
      new Object[]{1L, OperationType.delete},
      new Object[]{0L, OperationType.insert},
      new Object[]{-1L, OperationType.update},
      new Object[]{Long.MAX_VALUE, OperationType.delete},
      new Object[]{Long.MIN_VALUE, OperationType.insert}
    );
  }

  public static Stream<Object[]> intValueParameters() {
    return Stream.of(
      new Object[]{1, OperationType.delete},
      new Object[]{0, OperationType.insert},
      new Object[]{-1, OperationType.update},
      new Object[]{Integer.MAX_VALUE, OperationType.delete},
      new Object[]{Integer.MIN_VALUE, OperationType.insert}
    );
  }
}
