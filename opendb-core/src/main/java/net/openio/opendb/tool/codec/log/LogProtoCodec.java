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
package net.openio.opendb.tool.codec.log;

import io.netty.buffer.ByteBuf;
import net.openio.opendb.log.Log;

import java.util.List;

public interface LogProtoCodec {

  List<Log> decode(ByteBuf buf, int length);

  void encode(ByteBuf buf, Log log);

  void encode(ByteBuf buf, List<Log> logs);

  int getByteSize(Log log);

  int getByteSize(List<Log> log);
}
