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
package net.openio.opendb.compression;

import net.openio.opendb.mem.BufferCache;
import net.openio.opendb.model.SequenceNumber;
import net.openio.opendb.storage.metadata.Level;
import net.openio.opendb.storage.metadata.Levels;
import net.openio.opendb.storage.sstable.SSTable;
import net.openio.opendb.storage.sstable.SSTableStorage;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class SizeTieredCompaction extends Compaction {


  public SizeTieredCompaction(BufferCache bufferCache, SSTableStorage storage, int ssTableSize) {
    super(bufferCache, storage, ssTableSize);
  }

  public List<SSTable> compaction(Levels levels, int level, SequenceNumber sequenceNumber, int ssTableSize) throws IOException {
    for (Level list : levels.getLevels()) {
      if (list.getLevel() == level) {
        list.clear();
        return compaction(list.getSsTables(), sequenceNumber);
      }
    }
    return new LinkedList<>();
  }
}
