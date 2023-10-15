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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class AsynchronousWriteBatch extends WriteBatch {

  private final List<String> file;

  private final LogStorage walStorage;

  private final int size;

  ExecutorService executorService = Executors.newSingleThreadExecutor();

  public AsynchronousWriteBatch(List<String> file, LogStorage walStorage, int size) {
    this.file = file;
    this.walStorage = walStorage;
    this.size = size;
  }


  @Override
  void step(List<Log> list) {
    executorService.execute(new WalTask(list));
    if (walStorage.logFileSize() > size) {
      file.add(walStorage.createNewFile());
    }
    this.notifyAll();
  }

  @Override
  void waitForNextNode(WriteBatch.Node snapshot) {
    try {
      this.wait();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    while (snapshot.next == null) {

    }
    snapshot.pre.next = null;
  }

  class WalTask implements Runnable {

    List<Log> list;

    @Override
    public void run() {
      try {
        walStorage.addLogs(list);
      } catch (IOException e) {
        e.printStackTrace();
        throw new RuntimeException("can not write logs");
      }
    }

    WalTask(List<Log> list) {
      this.list = list;
    }
  }
}
