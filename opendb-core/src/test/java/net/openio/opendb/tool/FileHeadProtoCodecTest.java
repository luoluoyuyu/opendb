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
