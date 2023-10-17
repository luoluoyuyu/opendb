package net.openio.opendb.storage.wal;

import io.netty.buffer.ByteBuf;
import net.openio.opendb.log.Log;
import net.openio.opendb.memarena.MemArena;
import net.openio.opendb.tool.codec.log.LogProtoCodec;

import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.List;

public class LogBodyStorage {

  private final MemArena memArena;

  private final LogProtoCodec protoCodec;

  public int flush(FileChannel fileChannel, int seek, List<Log> logs) throws IOException {
    fileChannel.position(seek);
    int size = protoCodec.getByteSize(logs);

    ByteBuf cache = memArena.allocator(size);
    protoCodec.encode(cache, logs);
    cache.writerIndex(size);
    fileChannel.write(cache.nioBuffer());
    cache.release();
    return size;
  }

  public List<Log> getLogs(FileChannel fileChannel, int seek, int size) throws IOException {
    fileChannel.position(seek);
    ByteBuf cache = memArena.allocator(size);
    cache.writerIndex(size);
    fileChannel.read(cache.nioBuffer());
    List<Log> walLogs = protoCodec.decode(cache, size);
    cache.release();
    return walLogs;
  }

  public LogBodyStorage(MemArena memArena, LogProtoCodec logProtoCodec) {
    this.memArena = memArena;
    this.protoCodec = logProtoCodec;
  }
}
