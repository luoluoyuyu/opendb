package net.openio.opendb.tool;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.openio.opendb.mem.KeyValueGenerator;
import net.openio.opendb.storage.sstable.SSTable;
import net.openio.opendb.model.key.BytesKey;
import net.openio.opendb.model.key.Key;
import net.openio.opendb.model.key.KeyType;
import net.openio.opendb.storage.sstable.MetaBlock;
import net.openio.opendb.storage.sstable.MetaData;
import net.openio.opendb.tool.codec.sstable.MetaBlockProtoCodec;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

public class MetaBlockCodecTest {

  private static MetaBlock metaBlock;

  private static SSTable ssTable;

  private static int id = 0;

  private static int count = 1 << 10;

  private final static int pageSize = 1 << 20;

  static {
    metaBlock = new MetaBlock();
    ssTable = new SSTable();
    ssTable.setKeyType(KeyType.bytesKey);
    metaBlock.setSsTable(ssTable);
  }

  private int getPageNum(int s) {
    return s % pageSize != 0 ? s / pageSize + 1 : s / pageSize;
  }


  @AfterEach
  public void test() {
    ByteBuf buf = Unpooled.buffer(
      getPageNum(
        MetaBlockProtoCodec.getByteSize(metaBlock)) * pageSize);

    MetaBlockProtoCodec.encode(buf, metaBlock);
    buf.writerIndex(buf.capacity());
    MetaBlock data = MetaBlockProtoCodec.decode(buf, ssTable);
    Assertions.assertEquals(MetaBlockProtoCodec.getByteSize(metaBlock), MetaBlockProtoCodec.getByteSize(data));
    for (int i = 0; i < data.getNum(); i++) {
      Assertions.assertEquals(data.get(i).getSize(), metaBlock.get(i).getSize());
      Assertions.assertEquals(data.get(i).getKey(), metaBlock.get(i).getKey());
      Assertions.assertEquals(data.get(i).getOffset(), metaBlock.get(i).getOffset());
      Assertions.assertEquals(data.get(i).getIndexId(), metaBlock.get(i).getIndexId());
    }
  }

  @ParameterizedTest
  @MethodSource("bytesKeyParameters")
  public void testMeta(Key key) {
    metaBlock.add(new MetaData(++id, key, id, id, ssTable));
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
