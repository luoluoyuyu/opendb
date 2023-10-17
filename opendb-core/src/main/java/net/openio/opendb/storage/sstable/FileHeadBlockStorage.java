package net.openio.opendb.storage.sstable;

import io.netty.buffer.ByteBuf;
import net.openio.opendb.memarena.MemArena;
import net.openio.opendb.tool.codec.sstable.FileHeadProtoCodec;

import java.io.IOException;
import java.nio.channels.FileChannel;

public class FileHeadBlockStorage {

  private final int pageSize;

  private MemArena memArena;

  public int flush(FileChannel fileChannel, SSTable ssTable, FileHeadBlock fileHeadBlock, int seek) throws IOException {
    fileChannel.position(seek);
    int pageNum = getPageNum(FileHeadProtoCodec.getByteSize(fileHeadBlock));
    int size = pageNum * pageSize;
    ByteBuf cache = memArena.allocator(size);
    FileHeadProtoCodec.encode(cache, fileHeadBlock);
    cache.writerIndex(size);
    fileChannel.write(cache.nioBuffer());
    cache.release();
    return seek + size;

  }

  public FileHeadBlock getFileHeadBlock(FileChannel fileChannel, int seek, int size) throws IOException {
    fileChannel.position(seek);
    ByteBuf cache = memArena.allocator(size);
    cache.writerIndex(size);
    fileChannel.read(cache.nioBuffer());
    FileHeadBlock dataBlock = FileHeadProtoCodec.decode(cache);
    cache.release();
    return dataBlock;
  }

  public FileHeadBlockStorage(MemArena memArena, int pageSize) {
    this.memArena = memArena;
    this.pageSize = pageSize;
  }

  private int getPageNum(int s) {
    return s % pageSize != 0 ? s / pageSize + 1 : s / pageSize;
  }
}
