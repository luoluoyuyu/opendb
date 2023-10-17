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
import net.openio.opendb.memarena.MemArena;
import net.openio.opendb.tool.codec.sstable.DataBlockProtoCodec;

import java.io.IOException;
import java.nio.channels.FileChannel;


public class DataBlockStorage {

  private int pageSize;

  private MemArena memArena;

  public int flush(FileChannel fileChannel, int seek, DataBlock dataBlock) throws IOException {
    fileChannel.position(seek);

    int pageNum = getPageNum(DataBlockProtoCodec.getByteSize(dataBlock));
    int size = pageNum * pageSize;

    ByteBuf cache = memArena.allocator(size);

    fileChannel.position(seek);
    DataBlockProtoCodec.encode(cache, dataBlock);
    cache.writerIndex(size);
    fileChannel.write(cache.nioBuffer());
    cache.release();
    return size;

  }

  public DataBlock getDataBlock(SSTable ssTable, FileChannel fileChannel, int seek, int size) throws IOException {
    fileChannel.position(seek);
    ByteBuf cache = memArena.allocator(size);
    cache.writerIndex(size);
    fileChannel.read(cache.nioBuffer());
    DataBlock dataBlock = DataBlockProtoCodec.decode(cache, ssTable, seek);
    cache.release();
    return dataBlock;
  }

  public DataBlockStorage(MemArena memArena, int pageSize) {
    this.memArena = memArena;
    this.pageSize = pageSize;
  }

  private int getPageNum(int s) {
    return s % pageSize != 0 ? s / pageSize + 1 : s / pageSize;
  }
}
