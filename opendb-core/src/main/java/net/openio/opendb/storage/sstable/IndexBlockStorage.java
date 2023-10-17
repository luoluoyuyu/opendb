package net.openio.opendb.storage.sstable;

import io.netty.buffer.ByteBuf;
import net.openio.opendb.memarena.MemArena;
import net.openio.opendb.tool.codec.sstable.IndexBlockProtoCodec;

import java.io.IOException;
import java.nio.channels.FileChannel;


public class IndexBlockStorage {

  private int pageSize;

  private MemArena memArena;

  //return length
  public int flush(FileChannel fileChannel, int seek, IndexBlock indexBlock) throws IOException {
    fileChannel.position(seek);

    int pageNum = getPageNum(indexBlock.getSize());
    int size = pageNum * pageSize;

    ByteBuf cache = memArena.allocator(size);

    fileChannel.position(seek);
    IndexBlockProtoCodec.encode(cache, indexBlock);
    cache.writerIndex(size);
    fileChannel.write(cache.nioBuffer());
    cache.release();
    return size + seek;
  }


  public IndexBlock getIndexBlock(SSTable ssTable, FileChannel fileChannel, int seek, int size) throws IOException {
    fileChannel.position(seek);
    ByteBuf buf = memArena.allocator(size);
    buf.writerIndex(size);
    fileChannel.read(buf.nioBuffer());
    IndexBlock indexBlock = IndexBlockProtoCodec.decode(buf, ssTable);
    buf.release();
    return indexBlock;
  }


  public IndexBlockStorage(MemArena memArena, int pageSize) {
    this.memArena = memArena;
    this.pageSize = pageSize;
  }

  private int getPageNum(int s) {
    return s % pageSize != 0 ? s / pageSize + 1 : s / pageSize;
  }


}
