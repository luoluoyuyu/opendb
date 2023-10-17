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
package net.openio.opendb.mem;


import net.openio.opendb.model.key.IntKey;
import net.openio.opendb.model.key.LongKey;
import net.openio.opendb.model.key.BytesKey;
import net.openio.opendb.model.key.DoubleKey;
import net.openio.opendb.model.key.FloatKey;
import net.openio.opendb.model.key.StringKey;
import net.openio.opendb.model.key.Key;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

public class BloomFilterTest {

  private BloomFilter bloomFilter;


  private static int count = 10;

  @BeforeEach
  public void setup() {
    bloomFilter = new BloomFilter(1024);
  }


  @ParameterizedTest
  @MethodSource("intKeyParameters")
  public void testIntKey(Key key) {
    bloomFilter.add(key);
    Assertions.assertTrue(bloomFilter.get(key));
  }

  @ParameterizedTest
  @MethodSource("floatKeyParameters")
  public void testFloatKey(Key key) {
    bloomFilter.add(key);
    Assertions.assertTrue(bloomFilter.get(key));
  }


  @ParameterizedTest
  @MethodSource("doubleKeyParameters")
  public void testDoubleKey(Key key) {
    bloomFilter.add(key);
    Assertions.assertTrue(bloomFilter.get(key));
  }


  @ParameterizedTest
  @MethodSource("stringKeyParameters")
  public void testStringKey(Key key) {
    bloomFilter.add(key);
    Assertions.assertTrue(bloomFilter.get(key));
  }


  @ParameterizedTest
  @MethodSource("longKeyParameters")
  public void testLongKey(Key key) {
    bloomFilter.add(key);
    Assertions.assertTrue(bloomFilter.get(key));
  }

  @ParameterizedTest
  @MethodSource("bytesKeyParameters")
  public void testBytesKey(Key key) {
    bloomFilter.add(key);
    Assertions.assertTrue(bloomFilter.get(key));
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


  public static Stream<Arguments> longKeyParameters() {
    Stream<Arguments> argumentsStream = Stream.of(
      Arguments.of(new LongKey(Long.MIN_VALUE, KeyValueGenerator.generateRandomSequenceNumber())),
      Arguments.of(new LongKey(Long.MAX_VALUE, KeyValueGenerator.generateRandomSequenceNumber())),
      Arguments.of(new LongKey(0L, KeyValueGenerator.generateRandomSequenceNumber())),
      Arguments.of(new LongKey(1L, KeyValueGenerator.generateRandomSequenceNumber())),
      Arguments.of(new LongKey(2L, KeyValueGenerator.generateRandomSequenceNumber()))
    );

    return Stream.concat(argumentsStream, generateRandomLongData(count));
  }

  private static Stream<Arguments> generateRandomLongData(int count) {
    return Stream.generate(() -> Arguments.of(KeyValueGenerator.generateRandomLongKey())).limit(count);
  }


  public static Stream<Arguments> stringKeyParameters() {
    Stream<Arguments> argumentsStream = Stream.of(
      Arguments.of(new StringKey("Hello", KeyValueGenerator.generateRandomSequenceNumber())),
      Arguments.of(new StringKey("Test", KeyValueGenerator.generateRandomSequenceNumber())),
      Arguments.of(new StringKey("StreamPipes", KeyValueGenerator.generateRandomSequenceNumber()))
    );

    return Stream.concat(argumentsStream, generateRandomStringData(count));
  }

  private static Stream<Arguments> generateRandomStringData(int count) {
    return Stream.generate(() -> Arguments.of(KeyValueGenerator.generateRandomStringKey())).limit(count);
  }


  public static Stream<Arguments> doubleKeyParameters() {
    Stream<Arguments> argumentsStream = Stream.of(
      Arguments.of(new DoubleKey(0.0, KeyValueGenerator.generateRandomSequenceNumber())),
      Arguments.of(new DoubleKey(-1.0, KeyValueGenerator.generateRandomSequenceNumber())),
      Arguments.of(new DoubleKey(1.0, KeyValueGenerator.generateRandomSequenceNumber()))
    );

    return Stream.concat(argumentsStream, generateRandomDoubleData(count));
  }

  private static Stream<Arguments> generateRandomDoubleData(int count) {
    return Stream.generate(() -> Arguments.of(KeyValueGenerator.generateRandomDoubleKey())).limit(count);
  }


  public static Stream<Arguments> floatKeyParameters() {
    Stream<Arguments> argumentsStream = Stream.of(
      Arguments.of(new FloatKey(0.0f, KeyValueGenerator.generateRandomSequenceNumber())),
      Arguments.of(new FloatKey(-1.0f, KeyValueGenerator.generateRandomSequenceNumber())),
      Arguments.of(new FloatKey(1.0f, KeyValueGenerator.generateRandomSequenceNumber()))
    );

    return Stream.concat(argumentsStream, generateRandomFloatData(count));
  }

  private static Stream<Arguments> generateRandomFloatData(int count) {
    return Stream.generate(() -> Arguments.of(KeyValueGenerator.generateRandomFloatKey())).limit(count);
  }


  public static Stream<Arguments> intKeyParameters() {
    Stream<Arguments> argumentsStream = Stream.of(
      Arguments.of(new IntKey(Integer.MIN_VALUE,
        KeyValueGenerator.generateRandomSequenceNumber())),
      Arguments.of(new IntKey(Integer.MAX_VALUE, KeyValueGenerator.generateRandomSequenceNumber())),
      Arguments.of(new IntKey(0, KeyValueGenerator.generateRandomSequenceNumber())),
      Arguments.of(new IntKey(1, KeyValueGenerator.generateRandomSequenceNumber())),
      Arguments.of(new IntKey(2, KeyValueGenerator.generateRandomSequenceNumber()))
    );

    return Stream.concat(argumentsStream, generateRandomIntData(count));
  }

  private static Stream<Arguments> generateRandomIntData(int count) {
    return Stream.generate(() -> Arguments.of(KeyValueGenerator.generateRandomIntKey())).limit(count);
  }

}
