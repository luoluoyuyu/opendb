package net.openio.opendb.tool;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.openio.opendb.mem.KeyValueGenerator;
import net.openio.opendb.storage.sstable.SSTable;
import net.openio.opendb.model.value.BytesValue;
import net.openio.opendb.model.value.Value;
import net.openio.opendb.model.value.ValueType;
import net.openio.opendb.storage.sstable.DataBlock;
import net.openio.opendb.tool.codec.sstable.DataBlockProtoCodec;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

public class DataBlockCodecTest {

  private final static DataBlock dataBlock;

  private final static SSTable ssTable;
  private final static int count = 1 << 10;

  private final static int pageSize = 1 << 16;

  static {
    ssTable = new SSTable();
    ssTable.setValueType(ValueType.bytesValue);
    dataBlock = new DataBlock();
    dataBlock.setId(12);
    dataBlock.setSsTable(ssTable);

  }


  private int getPageNum(int s) {
    return s % pageSize != 0 ? s / pageSize + 1 : s / pageSize;
  }

  @AfterEach
  public void endTest() {
    ByteBuf buf = Unpooled.buffer(
      getPageNum(DataBlockProtoCodec
        .getByteSize(dataBlock)) * pageSize);
    DataBlockProtoCodec.encode(buf, dataBlock);
    buf.writerIndex(buf.capacity());
    DataBlock data = DataBlockProtoCodec.decode(buf, ssTable, 0);
    Assertions.assertEquals(DataBlockProtoCodec.getByteSize(dataBlock), DataBlockProtoCodec.getByteSize(data));
    Assertions.assertEquals(data.getSize(), DataBlockProtoCodec.getByteSize(data));
    Assertions.assertEquals(data.getId(), dataBlock.getId());
    Assertions.assertEquals(data.getValueNum(), dataBlock.getValueNum());

  }

  @ParameterizedTest
  @MethodSource("bytesValueParameters")
  public void test(Value key) {
    dataBlock.add(key);
  }

  public static Stream<Arguments> bytesValueParameters() {
    Stream<Arguments> argumentsStream = Stream.of(
      Arguments.of(

        new BytesValue(new byte[]{0x01, 0x02, 0x03}, KeyValueGenerator.generateRandomOperationType())),
      Arguments.of(
        new BytesValue(new byte[]{0x04, 0x05, 0x06}, KeyValueGenerator.generateRandomOperationType())),
      Arguments.of(
        new BytesValue(new byte[]{0x07, 0x08, 0x09}, KeyValueGenerator.generateRandomOperationType()))
    );

    return Stream.concat(argumentsStream, generateRandomBytesData(count));
  }

  private static Stream<Arguments> generateRandomBytesData(int count) {
    return Stream.generate(() -> Arguments.of(KeyValueGenerator.generateRandomBytesValue())).limit(count);
  }
}
