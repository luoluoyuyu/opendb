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
import net.openio.opendb.storage.sstable.FileHeadBlock;
import net.openio.opendb.tool.codec.sstable.FileHeadProtoCodec;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class FileHeadProtoCodecTest {

  @Test
  public void test() {
    FileHeadBlock fileHeadBlock = new FileHeadBlock();
    fileHeadBlock.setDataOfferSeek(12);
    fileHeadBlock.setDataOfferSize(13);
    fileHeadBlock.setIndexOfferSeek(14);
    fileHeadBlock.setIndexOfferSize(15);
    fileHeadBlock.setMetaOfferSeek(16);
    fileHeadBlock.setMetaOfferSize(17);
    fileHeadBlock.setBloomOfferSize(18);
    fileHeadBlock.setBloomOfferSeek(19);
    ByteBuf buf = Unpooled.buffer(1 << 8);
    FileHeadProtoCodec.encode(buf, fileHeadBlock);
    buf.writerIndex(1 << 8);
    FileHeadBlock fileHead = FileHeadProtoCodec.decode(buf);


    Assertions.assertEquals(FileHeadProtoCodec.getByteSize(fileHeadBlock), FileHeadProtoCodec.getByteSize(fileHead));
    Assertions.assertEquals(fileHead.getDataOfferSeek(), fileHeadBlock.getDataOfferSeek());
    Assertions.assertEquals(fileHead.getDataOfferSize(), fileHeadBlock.getDataOfferSize());
    Assertions.assertEquals(fileHead.getIndexOfferSeek(), fileHeadBlock.getIndexOfferSeek());
    Assertions.assertEquals(fileHead.getIndexOfferSize(), fileHeadBlock.getIndexOfferSize());
    Assertions.assertEquals(fileHead.getMetaOfferSeek(), fileHeadBlock.getMetaOfferSeek());
    Assertions.assertEquals(fileHead.getMetaOfferSize(), fileHeadBlock.getMetaOfferSize());
    Assertions.assertEquals(fileHead.getBloomOfferSize(), fileHeadBlock.getBloomOfferSize());
    Assertions.assertEquals(fileHead.getBloomOfferSeek(), fileHeadBlock.getBloomOfferSeek());
  }
}
