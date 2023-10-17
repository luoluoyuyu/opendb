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
