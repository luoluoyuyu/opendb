package net.openio.opendb.memarena;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;

public class MemArena {

  private PooledByteBufAllocator allocator;


  public ByteBuf allocator(int size) {
    return allocator.directBuffer(size);

  }


  public MemArena(int memArenaSize, int pageSize) {
    int memArenaSize1 = 2 * 1024 * 1024; // 32MB
    int pageSize1 = 16 * 1024; // 16KB
//    allocator = new PooledByteBufAllocator(true, 0, memArenaSize1, pageSize1, 0);
    allocator = new PooledByteBufAllocator(true);
  }


}
