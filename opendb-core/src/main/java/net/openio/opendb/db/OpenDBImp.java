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

import net.openio.opendb.model.Options;


import java.util.concurrent.ExecutorService;

public class OpenDBImp {

  private final String dir;

  private SnapshotManager snapshotManager;

  private ExecutorService compactionExecutor;

  private WriteBatch writeBatch;

  private ColumnFamilyManager columnFamilyManager;


  public OpenDBImp(Options option, String dir) {
    this.dir = dir;
  }
}
