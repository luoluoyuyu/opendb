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
import net.openio.opendb.mem.KeyValueGenerator;
import net.openio.opendb.storage.sstable.SSTable;
import net.openio.opendb.model.key.BytesKey;
import net.openio.opendb.model.key.Key;
import net.openio.opendb.model.key.KeyType;
import net.openio.opendb.storage.sstable.IndexData;
import net.openio.opendb.tool.codec.sstable.IndexDataProtoCodec;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

public class IndexDataCodecTest {
  private final static IndexData indexData;


  private final static int count = 1 << 19;

  private final static SSTable ssTable;

  int pageSize = 1 << 16;

  static {
    ssTable = new SSTable();
    ssTable.setKeyType(KeyType.bytesKey);
    indexData = new IndexData();
    indexData.setSsTable(ssTable);
    indexData.setDataId(111);
    indexData.setDataBlockSize(1234);
    indexData.setOffset(123212);
    indexData.setFirstIndex(2313);
  }

  private int getPageNum(int s) {
    return s % pageSize != 0 ? s / pageSize + 1 : s / pageSize;
  }


  @AfterEach
  public void endTest() {
    ByteBuf buf = Unpooled.buffer(IndexDataProtoCodec
      .getByteSize(indexData));
    IndexDataProtoCodec.encode(buf, indexData);
    IndexData data = IndexDataProtoCodec.decode(buf, ssTable);
    Assertions.assertEquals(indexData.getSize(), buf.writerIndex());
    Assertions.assertEquals(data.getSize(), buf.writerIndex());
    Assertions.assertEquals(data.getDataId(), indexData.getDataId());
    Assertions.assertEquals(data.getDataBlockSize(), indexData.getDataBlockSize());
    Assertions.assertEquals(data.getNum(), indexData.getNum());
    Assertions.assertEquals(data.getOffset(), indexData.getOffset());
    Assertions.assertEquals(data.getKeys(), indexData.getKeys());
    Assertions.assertEquals(data.getFirstIndex(), indexData.getFirstIndex());
  }


  @ParameterizedTest
  @MethodSource("bytesKeyParameters")
  public void testBytesKey(Key key) {
    indexData.add(key);
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
