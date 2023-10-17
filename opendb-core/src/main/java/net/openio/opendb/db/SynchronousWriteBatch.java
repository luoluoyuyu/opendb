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
import net.openio.opendb.model.SequenceNumber;
import net.openio.opendb.storage.wal.LogStorage;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class SynchronousWriteBatch extends WriteBatch {

  private final List<String> file;

  private final LogStorage walStorage;

  private final int size;

  public SynchronousWriteBatch(List<String> file, LogStorage walStorage, int size, long max) {
    super(max);
    this.file = file;
    this.walStorage = walStorage;
    this.size = size;
  }

  @Override
  public List<String> getFile() {
    return file;
  }

  @Override
  public List<Log> getWal(SequenceNumber sequenceNumber) {
    List<Log> walLogs = new LinkedList<>();
    for (int i = file.size() - 1; i >= 0; i--) {
      try {
        List<Log> l = null;
        walLogs.addAll(l = walStorage.getLogs(sequenceNumber, file.get(i)));
        if (l.size() == 0) {
          return walLogs;
        }
      } catch (IOException e) {
        e.printStackTrace();
        return walLogs;
      }
    }
    return walLogs;
  }

  @Override
  boolean isSyn() {
    return true;
  }

  @Override
  void step(List<Log> list) {
    try {
      if (!list.isEmpty()) {
        walStorage.addLogs(list);
      }
    } catch (IOException e) {
      e.printStackTrace();
      throw new RuntimeException("can not write logs");
    }

    try {
      this.notifyAll();
    } catch (Exception e) {

    }

    if (walStorage.logFileSize() > size) {
      file.add(walStorage.createNewFile());
    }
  }

  @Override
  void waitForNextNode(Node snapshot) {
    do {
      try {
        this.wait();
      } catch (Exception e) {
      }
    } while (this.writeSequenceNumber < snapshot.sequenceNumber.getTimes());
    snapshot.pre.next = null;
  }

  @Override
  void close() {

  }


}
