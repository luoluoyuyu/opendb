package net.openio.opendb.storage.sstable;

import io.netty.buffer.ByteBuf;
import net.openio.opendb.mem.BloomFilter;
import net.openio.opendb.memarena.MemArena;
import net.openio.opendb.tool.codec.sstable.BloomFilterProtoCodec;

import java.io.IOException;
import java.nio.channels.FileChannel;


public class BloomBlockStorage {

  private final int pageSize;

  private final MemArena memArena;


  public BloomFilter getBloomFilter(FileChannel fileChannel, int seek, int size) throws IOException {
    fileChannel.position(seek);
    ByteBuf cache = memArena.allocator(size);
    cache.writerIndex(size);
    fileChannel.read(cache.nioBuffer());
    BloomFilter bloomFilter = BloomFilterProtoCodec.decode(cache);
    cache.release();
    return bloomFilter;
  }

  public int flush(FileChannel fileChannel, int seek, BloomFilter bloomFilter) throws IOException {

    int pageNum = getPageNum(BloomFilterProtoCodec.getByteSize(bloomFilter));
    int size = pageNum * pageSize;
    ByteBuf cache = memArena.allocator(size);

    fileChannel.position(seek);
    BloomFilterProtoCodec.encode(cache, bloomFilter);
    cache.writerIndex(size);
    fileChannel.write(cache.nioBuffer());
    cache.release();
    return size + seek;
  }

  public BloomBlockStorage(MemArena memArena, int pageSize) {
    this.pageSize = pageSize;
    this.memArena = memArena;
  }

  private int getPageNum(int s) {
    return s % pageSize != 0 ? s / pageSize + 1 : s / pageSize;
  }
}
