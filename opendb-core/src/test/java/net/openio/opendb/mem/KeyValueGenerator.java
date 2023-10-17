package net.openio.opendb.mem;

import net.openio.opendb.db.KeyValueEntry;
import net.openio.opendb.model.OperationType;
import net.openio.opendb.model.SequenceNumber;
import net.openio.opendb.model.key.Key;
import net.openio.opendb.model.value.Value;
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

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicLong;

public class KeyValueGenerator {

  public static KeyValueEntry generateRandomIntKeyValueEntry() {
    Key randomKey = generateRandomIntKey();
    Value randomValue = generateRandomIntValue();
    return new KeyValueEntry(randomKey, randomValue);
  }

  public static KeyValueEntry generateRandomLongKeyValueEntry() {
    Key randomKey = generateRandomLongKey();
    Value randomValue = generateRandomLongValue();
    return new KeyValueEntry(randomKey, randomValue);
  }

  public static KeyValueEntry generateRandomFloatKeyValueEntry() {
    Key randomKey = generateRandomFloatKey();
    Value randomValue = generateRandomFloatValue();
    return new KeyValueEntry(randomKey, randomValue);
  }

  public static KeyValueEntry generateRandomDoubleKeyValueEntry() {
    Key randomKey = generateRandomDoubleKey();
    Value randomValue = generateRandomDoubleValue();
    return new KeyValueEntry(randomKey, randomValue);
  }

  public static KeyValueEntry generateRandomBytesKeyValueEntry() {
    Key randomKey = generateRandomBytesKey();
    Value randomValue = generateRandomBytesValue();
    return new KeyValueEntry(randomKey, randomValue);
  }

  public static KeyValueEntry generateRandomStringKeyValueEntry() {
    Key randomKey = generateRandomStringKey();
    Value randomValue = generateRandomStringValue();
    return new KeyValueEntry(randomKey, randomValue);
  }

  public static Key generateRandomIntKey() {
    return new IntKey(ThreadLocalRandom.current().nextInt(Integer.MIN_VALUE, Integer.MAX_VALUE),
      generateRandomSequenceNumber());
  }

  public static Key generateRandomLongKey() {
    return new LongKey(ThreadLocalRandom.current().nextLong(Long.MIN_VALUE, Long.MAX_VALUE),
      generateRandomSequenceNumber());
  }

  public static Key generateRandomFloatKey() {
    return new FloatKey(ThreadLocalRandom.current().nextFloat(), generateRandomSequenceNumber());
  }

  public static Key generateRandomDoubleKey() {
    return new DoubleKey(ThreadLocalRandom.current().nextDouble(), generateRandomSequenceNumber());
  }

  public static Key generateRandomBytesKey() {
    byte[] randomBytes = new byte[ThreadLocalRandom.current().nextInt(1, 100)];
    ThreadLocalRandom.current().nextBytes(randomBytes);
    return new BytesKey(randomBytes, generateRandomSequenceNumber());
  }

  public static Key generateRandomStringKey() {
    String randomString = generateRandomString(10);
    return new StringKey(randomString, generateRandomSequenceNumber());
  }

  public static Value generateRandomIntValue() {
    return new IntValue(ThreadLocalRandom.current().nextInt(Integer.MIN_VALUE,
      Integer.MAX_VALUE), generateRandomOperationType());
  }

  public static Value generateRandomLongValue() {
    return new LongValue(ThreadLocalRandom.current().nextLong(Long.MIN_VALUE, Long.MAX_VALUE),
      generateRandomOperationType());
  }

  public static Value generateRandomFloatValue() {
    return new FloatValue(ThreadLocalRandom.current().nextFloat(),
      generateRandomOperationType());
  }

  public static Value generateRandomDoubleValue() {
    return new DoubleValue(ThreadLocalRandom.current().nextDouble(),
      generateRandomOperationType());
  }

  public static Value generateRandomBytesValue() {
    byte[] randomBytes = new byte[ThreadLocalRandom.current().nextInt(1, 100)];
    ThreadLocalRandom.current().nextBytes(randomBytes);
    return new BytesValue(randomBytes, generateRandomOperationType());
  }

  public static Value generateRandomStringValue() {
    String randomString = generateRandomString(10);
    return new StringValue(randomString, generateRandomOperationType());
  }

  public static String generateRandomString(int length) {
    String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    StringBuilder randomString = new StringBuilder(length);
    for (int i = 0; i < length; i++) {
      int randomIndex = ThreadLocalRandom.current().nextInt(characters.length());
      randomString.append(characters.charAt(randomIndex));
    }
    return randomString.toString();
  }


  private static AtomicLong counter = new AtomicLong(0);

  public static SequenceNumber generateRandomSequenceNumber() {
    long timestamp = System.currentTimeMillis();
    long uniqueValue = counter.getAndIncrement();
    long sequenceNumber = (timestamp << 32) | uniqueValue;
    return new SequenceNumber(sequenceNumber);
  }

  public static OperationType generateRandomOperationType() {
    OperationType[] operationTypes = OperationType.values();
    int randomIndex = ThreadLocalRandom.current().nextInt(operationTypes.length);
    return operationTypes[randomIndex];
  }

}
