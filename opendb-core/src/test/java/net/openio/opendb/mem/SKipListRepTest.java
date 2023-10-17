package net.openio.opendb.mem;


import net.openio.opendb.db.KeyValueEntry;
import net.openio.opendb.model.key.Key;
import net.openio.opendb.model.value.IntValue;
import net.openio.opendb.model.value.LongValue;
import net.openio.opendb.model.value.StringValue;
import net.openio.opendb.model.value.BytesValue;
import net.openio.opendb.model.value.DoubleValue;
import net.openio.opendb.model.value.FloatValue;
import net.openio.opendb.model.key.IntKey;
import net.openio.opendb.model.key.LongKey;
import net.openio.opendb.model.key.BytesKey;
import net.openio.opendb.model.key.DoubleKey;
import net.openio.opendb.model.key.FloatKey;
import net.openio.opendb.model.key.StringKey;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.provider.Arguments;

import java.util.ArrayList;
import java.util.Comparator;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

public class SKipListRepTest {

  private final static SkipListRep<StringKey, StringValue> stringSkipListRep;

  private final static SkipListRep<IntKey, IntValue> intSkipListRep;

  private final static SkipListRep<LongKey, LongValue> longSkipListRep;

  private final static SkipListRep<DoubleKey, DoubleValue> doubleSkipListRep;

  private final static SkipListRep<FloatKey, FloatValue> floatSkipListRep;

  private final static SkipListRep<BytesKey, BytesValue> bytesSkipListRep;
  private final static List<Key> stringKeyValueList;
  private final static List<Key> intKeyValueList;
  private final static List<Key> longKeyValueList;
  private final static List<Key> doubleKeyValueList;
  private final static List<Key> floatKeyValueList;
  private final static List<Key> bytesKeyValueList;


  private static int count = 1 << 10;


  static {
    stringSkipListRep = new SkipListRep<>();
    intSkipListRep = new SkipListRep<>();
    longSkipListRep = new SkipListRep<>();
    doubleSkipListRep = new SkipListRep<>();
    floatSkipListRep = new SkipListRep<>();
    bytesSkipListRep = new SkipListRep<>();

    stringKeyValueList = new ArrayList<>(count);
    intKeyValueList = new ArrayList<>(count);
    longKeyValueList = new ArrayList<>(count);
    doubleKeyValueList = new ArrayList<>(count);
    floatKeyValueList = new ArrayList<>(count);
    bytesKeyValueList = new ArrayList<>(count);
  }

  @AfterEach
  public void end() {
    intKeyValueList.sort((a, b) -> {
        int d = a.compareTo(b);
        if (d == 0) {
          d = a.getSequenceNumber().compareTo(b.getSequenceNumber());
        }
        return d;

      }
    );
    Iterator<Key> i = intKeyValueList.iterator();
    for (KeyValueEntry keyValueEntry : intSkipListRep) {
      Assertions.assertEquals(keyValueEntry.getKey(), i.next());

    }
    Assertions.assertEquals(intSkipListRep.getValueNum(), intKeyValueList.size());

  }

  private void addAndAssertKeyValueEntry(SkipListRep skipListRep, KeyValueEntry keyValueEntry) {
    skipListRep.addKeyValue(keyValueEntry);
  }


  @RepeatedTest(1024)
  public void testIntKeyValueEntry() {
    KeyValueEntry keyValueEntry = KeyValueGenerator.generateRandomBytesKeyValueEntry();
    addAndAssertKeyValueEntry(intSkipListRep, keyValueEntry);
    synchronized (intKeyValueList) {
      intKeyValueList.add(keyValueEntry.getKey());
    }
  }

//  @ParameterizedTest
//  @MethodSource("longKeyValueParameters")
//  public void testLongKeyValueEntry(KeyValueEntry keyValueEntry) {
//    longKeyValueList.add(keyValueEntry.getKey());
//    addAndAssertKeyValueEntry(longSkipListRep,keyValueEntry);
//  }
//
//  @ParameterizedTest
//  @MethodSource("floatKeyValueParameters")
//  public void testFloatKeyValueEntry(KeyValueEntry keyValueEntry) {
//    floatKeyValueList.add(keyValueEntry.getKey());
//    addAndAssertKeyValueEntry(floatSkipListRep,keyValueEntry);
//  }
//
//  @ParameterizedTest
//  @MethodSource("doubleKeyValueParameters")
//  public void testDoubleKeyValueEntry(KeyValueEntry keyValueEntry) {
//    doubleKeyValueList.add(keyValueEntry.getKey());
//    addAndAssertKeyValueEntry(doubleSkipListRep,keyValueEntry);
//  }
//
//  @ParameterizedTest
//  @MethodSource("bytesKeyValueParameters")
//  public void testBytesKeyValueEntry(KeyValueEntry keyValueEntry) {
//    bytesKeyValueList.add(keyValueEntry.getKey());
//    addAndAssertKeyValueEntry(bytesSkipListRep,keyValueEntry);
//  }
//
//  @ParameterizedTest
//  @MethodSource("stringKeyValueParameters")
//  public void testStringKeyValueEntry(KeyValueEntry keyValueEntry) {
//    stringKeyValueList.add(keyValueEntry.getKey());
//    addAndAssertKeyValueEntry(stringSkipListRep,keyValueEntry);
//  }

  public static Stream<Arguments> stringKeyValueParameters() {
    Stream<Arguments> argumentsStream = Stream.of(
      Arguments.of(new KeyValueEntry(new StringKey("Hello",
        KeyValueGenerator.generateRandomSequenceNumber()),
        new StringValue("Hello", KeyValueGenerator.generateRandomOperationType()))),
      Arguments.of(new KeyValueEntry(new StringKey("Test",
        KeyValueGenerator.generateRandomSequenceNumber()),
        new StringValue("Test", KeyValueGenerator.generateRandomOperationType()))),
      Arguments.of(new KeyValueEntry(new StringKey("StreamPipes",
        KeyValueGenerator.generateRandomSequenceNumber()),
        new StringValue("StreamPipes", KeyValueGenerator.generateRandomOperationType())))
    );

    return Stream.concat(argumentsStream, generateRandomStringData(count));
  }

  private static Stream<Arguments> generateRandomStringData(int count) {
    return Stream.generate(() -> Arguments.of(KeyValueGenerator.generateRandomStringKeyValueEntry())).limit(count);
  }

  public static Stream<Arguments> bytesKeyValueParameters() {
    Stream<Arguments> argumentsStream = Stream.of(
      Arguments.of(new KeyValueEntry(new BytesKey(new byte[]{0x01, 0x02, 0x03},
        KeyValueGenerator.generateRandomSequenceNumber()),
        new BytesValue(new byte[]{0x01, 0x02, 0x03}, KeyValueGenerator.generateRandomOperationType()))),
      Arguments.of(new KeyValueEntry(new BytesKey(new byte[]{0x04, 0x05, 0x06},
        KeyValueGenerator.generateRandomSequenceNumber()),
        new BytesValue(new byte[]{0x04, 0x05, 0x06}, KeyValueGenerator.generateRandomOperationType()))),
      Arguments.of(new KeyValueEntry(new BytesKey(new byte[]{0x07, 0x08, 0x09},
        KeyValueGenerator.generateRandomSequenceNumber()),
        new BytesValue(new byte[]{0x07, 0x08, 0x09}, KeyValueGenerator.generateRandomOperationType())))
    );

    return Stream.concat(argumentsStream, generateRandomBytesData(count));
  }

  private static Stream<Arguments> generateRandomBytesData(int count) {
    return Stream.generate(() -> Arguments.of(KeyValueGenerator.generateRandomBytesKeyValueEntry())).limit(count);
  }


  public static Stream<Arguments> doubleKeyValueParameters() {
    Stream<Arguments> argumentsStream = Stream.of(
      Arguments.of(new KeyValueEntry(new DoubleKey(0.0,
        KeyValueGenerator.generateRandomSequenceNumber()),
        new DoubleValue(0.0, KeyValueGenerator.generateRandomOperationType()))),
      Arguments.of(new KeyValueEntry(new DoubleKey(-1.0,
        KeyValueGenerator.generateRandomSequenceNumber()),
        new DoubleValue(1.0, KeyValueGenerator.generateRandomOperationType()))),
      Arguments.of(new KeyValueEntry(new DoubleKey(1.0,
        KeyValueGenerator.generateRandomSequenceNumber()),
        new DoubleValue(2.0, KeyValueGenerator.generateRandomOperationType())))
    );

    return Stream.concat(argumentsStream, generateRandomDoubleData(count));
  }

  private static Stream<Arguments> generateRandomDoubleData(int count) {
    return Stream.generate(() -> Arguments.of(KeyValueGenerator.generateRandomDoubleKeyValueEntry())).limit(count);
  }


  public static Stream<Arguments> floatKeyValueParameters() {
    Stream<Arguments> argumentsStream = Stream.of(
      Arguments.of(new KeyValueEntry(new FloatKey(0.0f,
        KeyValueGenerator.generateRandomSequenceNumber()),
        new FloatValue(0.0f, KeyValueGenerator.generateRandomOperationType()))),
      Arguments.of(new KeyValueEntry(new FloatKey(-1.0f,
        KeyValueGenerator.generateRandomSequenceNumber()),
        new FloatValue(1.0f, KeyValueGenerator.generateRandomOperationType()))),
      Arguments.of(new KeyValueEntry(new FloatKey(1.0f,
        KeyValueGenerator.generateRandomSequenceNumber()),
        new FloatValue(2.0f, KeyValueGenerator.generateRandomOperationType())))
    );

    return Stream.concat(argumentsStream, generateRandomFloatData(count));
  }

  private static Stream<Arguments> generateRandomFloatData(int count) {
    return Stream.generate(() -> Arguments.of(KeyValueGenerator.generateRandomFloatKeyValueEntry())).limit(count);
  }

  public static Stream<Arguments> longKeyValueParameters() {
    Stream<Arguments> argumentsStream = Stream.of(
      Arguments.of(new KeyValueEntry(new LongKey(Long.MIN_VALUE,
        KeyValueGenerator.generateRandomSequenceNumber()),
        new LongValue(0L, KeyValueGenerator.generateRandomOperationType()))),
      Arguments.of(new KeyValueEntry(new LongKey(Long.MAX_VALUE,
        KeyValueGenerator.generateRandomSequenceNumber()),
        new LongValue(1L, KeyValueGenerator.generateRandomOperationType())))
    );

    return Stream.concat(argumentsStream, generateRandomLongData(count));
  }

  private static Stream<Arguments> generateRandomLongData(int count) {
    return Stream.generate(() -> Arguments.of(KeyValueGenerator.generateRandomLongKeyValueEntry())).limit(count);
  }

  public static Stream<Arguments> intKeyValueParameters() {
    Stream<Arguments> argumentsStream = Stream.of(
      Arguments.of(new KeyValueEntry(new IntKey(Integer.MIN_VALUE,
        KeyValueGenerator.generateRandomSequenceNumber()),
        new IntValue(0, KeyValueGenerator.generateRandomOperationType()))),
      Arguments.of(new KeyValueEntry(new IntKey(Integer.MAX_VALUE, KeyValueGenerator.generateRandomSequenceNumber()),
        new IntValue(1, KeyValueGenerator.generateRandomOperationType()))),
      Arguments.of(new KeyValueEntry(new IntKey(Integer.MIN_VALUE, KeyValueGenerator.generateRandomSequenceNumber()),
        new IntValue(2, KeyValueGenerator.generateRandomOperationType()))),
      Arguments.of(new KeyValueEntry(new IntKey(Integer.MAX_VALUE,
        KeyValueGenerator.generateRandomSequenceNumber()),
        new IntValue(3, KeyValueGenerator.generateRandomOperationType())))
    );

    return Stream.concat(argumentsStream, generateRandomIntData(count));
  }

  private static Stream<Arguments> generateRandomIntData(int count) {
    return Stream.generate(() -> Arguments.of(KeyValueGenerator.generateRandomIntKeyValueEntry())).limit(count);
  }


}


