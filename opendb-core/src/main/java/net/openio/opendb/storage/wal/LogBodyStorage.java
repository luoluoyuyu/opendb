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
