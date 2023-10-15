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
package net.openio.opendb.db;

import net.openio.opendb.log.Log;
import net.openio.opendb.storage.wal.LogStorage;

import java.io.IOException;
import java.util.List;

public class SynchronousWriteBatch extends WriteBatch {

  private final List<String> file;

  private final LogStorage walStorage;

  private final int size;

  public SynchronousWriteBatch(List<String> file, LogStorage walStorage, int size) {
    this.file = file;
    this.walStorage = walStorage;
    this.size = size;
  }

  @Override
  void step(List<Log> list) {
    try {
      walStorage.addLogs(list);
    } catch (IOException e) {
      e.printStackTrace();
      throw new RuntimeException("can not write logs");
    }

    this.notifyAll();
    if (walStorage.logFileSize() > size) {
      file.add(walStorage.createNewFile());
    }
  }

  @Override
  void waitForNextNode(Node snapshot) {
    try {
      this.wait();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    snapshot.pre.next = null;
  }


}
