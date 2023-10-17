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
