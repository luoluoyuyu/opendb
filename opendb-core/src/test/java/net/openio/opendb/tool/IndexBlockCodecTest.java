package net.openio.opendb.tool;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.openio.opendb.mem.KeyValueGenerator;
import net.openio.opendb.storage.sstable.SSTable;
import net.openio.opendb.model.key.BytesKey;
import net.openio.opendb.model.key.Key;
import net.openio.opendb.model.key.KeyType;
import net.openio.opendb.storage.sstable.IndexBlock;
import net.openio.opendb.storage.sstable.IndexData;
import net.openio.opendb.tool.codec.sstable.IndexBlockProtoCodec;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Stream;

public class IndexBlockCodecTest {
  private static int i = 0;

  private static IndexData indexData;

  private static final IndexBlock indexBlock;

  private static final int count = 1 << 10;

  private static final SSTable ssTable;

  private final static int pageSize = 1 << 18;


  static {
    ssTable = new SSTable();
    ssTable.setKeyType(KeyType.bytesKey);
    indexBlock = new IndexBlock(12312, ssTable, 3232);
    indexData = new IndexData();
    indexData.setSsTable(ssTable);
    indexData.setDataId(0);
    indexData.setDataBlockSize(1234);
    indexData.setOffset(123212);
  }

  private static int getPageNum(int s) {
    return s % pageSize != 0 ? s / pageSize + 1 : s / pageSize;
  }


  @AfterEach
  public void endTest() {
    ByteBuf buf = Unpooled.buffer(getPageNum(IndexBlockProtoCodec
      .getByteSize(indexBlock)) * pageSize);
    IndexBlockProtoCodec.encode(buf, indexBlock);
    Assertions.assertEquals(indexBlock.getSize(), IndexBlockProtoCodec.getByteSize(indexBlock));
    buf.writerIndex(buf.capacity());
    IndexBlock data = IndexBlockProtoCodec.decode(buf, ssTable);
    Assertions.assertEquals(data.getSize(), indexBlock.getSize());
    Assertions.assertEquals(data.getId(), indexBlock.getId());
    Assertions.assertEquals(data.getNum(), indexBlock.getNum());
    Assertions.assertEquals(data.getOffset(), indexBlock.getOffset());
    for (int i = 0; i < data.getNum(); i++) {
      Assertions.assertEquals(data.get(i).getDataId(), indexBlock.get(i).getDataId());
      Assertions.assertEquals(data.get(i).getSize(), indexBlock.get(i).getSize());
    }
  }


  @ParameterizedTest
  @MethodSource("bytesKeyParameters")
  public void testBytesKey(Key key) {
    indexData.add(key);
    i++;
    if (i >= count / 8) {
      i = 0;
      IndexData data = indexData.getMaxIndexData(1 << 8);
      indexBlock.add(data);
      if (indexData.getNum() > 0) {
        indexBlock.add(indexData);
      }
      indexData = new IndexData(ThreadLocalRandom.current().nextInt(1, 100),
        ssTable,
        ThreadLocalRandom.current().nextInt(1, 100));
    }
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
