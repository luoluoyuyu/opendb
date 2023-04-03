package net.openio.jrocksDb.memArena;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import net.openio.jrocksDb.config.Config;

public class MemArena {

    private PooledByteBufAllocator allocator;


    static int memArenaSize= Config.memArenaSize;


    public ByteBuf allocator(int size){
        return allocator.directBuffer(size);

    }


    public MemArena(){

        allocator=new PooledByteBufAllocator(
                true,  // preferDirect
                0,     // nHeapArena
                0,     // nDirectArena
                8192,  // pageSize
                11,    // maxOrder
                40 * 1024 * 1024,  // tinyCacheSize
                40 * 1024 * 1024,  // smallCacheSize
                40 * 1024 * 1024,  // normalCacheSize
                false  // useCacheForAllThreads
        );
    }

    public MemArena(boolean small){
        if(small) {
            allocator = new PooledByteBufAllocator(
                    true,  // preferDirect
                    0,     // nHeapArena
                    0,     // nDirectArena
                    8192,  // pageSize
                    6,     // maxOrder
                    1024 * 1024,  // tinyCacheSize
                    1024 * 1024,  // smallCacheSize
                    1024 * 1024,  // normalCacheSize
                    false  // useCacheForAllThreads
            );
        }else {
            allocator=new PooledByteBufAllocator(
                    true,  // preferDirect
                    0,     // nHeapArena
                    0,     // nDirectArena
                    8192,  // pageSize
                    11,    // maxOrder
                    40 * 1024 * 1024,  // tinyCacheSize
                    40 * 1024 * 1024,  // smallCacheSize
                    40 * 1024 * 1024,  // normalCacheSize
                    false  // useCacheForAllThreads
            );
        }

    }



}
