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
