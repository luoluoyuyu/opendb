package net.openio.opendb.storage.wal;

import io.netty.buffer.ByteBuf;
import net.openio.opendb.memarena.MemArena;
import net.openio.opendb.tool.codec.log.FileHeadProtoCodec;

import java.io.IOException;
import java.nio.channels.FileChannel;

public class LogHeadStorage {

  private int pageSize;

  private MemArena memArena;


  public int flush(FileChannel fileChannel, int seek, LogFileHead logFileHead) throws IOException {
    fileChannel.position(seek);
    int size = FileHeadProtoCodec.getByteSize(logFileHead);
    if (size > pageSize) {
      return -1;
    }
    ByteBuf cache = memArena.allocator(pageSize);
    fileChannel.position(seek);
    FileHeadProtoCodec.encode(cache, logFileHead);
    cache.writerIndex(pageSize);
    fileChannel.write(cache.nioBuffer());
    cache.release();
    return size;
  }

  public LogFileHead getFileHead(FileChannel fileChannel, int seek) throws IOException {
    fileChannel.position(seek);
    ByteBuf cache = memArena.allocator(pageSize);
    cache.writerIndex(pageSize);
    fileChannel.read(cache.nioBuffer());
    LogFileHead fileHead = FileHeadProtoCodec.decode(cache, pageSize);
    cache.release();
    return fileHead;
  }

  public LogHeadStorage(MemArena memArena, int pageSize) {
    this.pageSize = pageSize;
    this.memArena = memArena;
  }

}
