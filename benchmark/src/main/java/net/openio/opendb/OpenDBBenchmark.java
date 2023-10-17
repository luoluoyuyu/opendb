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
package net.openio.opendb;


import net.openio.opendb.db.ColumnFamilyHandle;
import net.openio.opendb.db.OpenDB;
import net.openio.opendb.db.OpenDBImp;
import net.openio.opendb.model.ColumnFamilyDescriptor;
import net.openio.opendb.model.OperationType;
import net.openio.opendb.model.Options;
import net.openio.opendb.model.key.IntKey;
import net.openio.opendb.model.key.KeyType;
import net.openio.opendb.model.value.IntValue;
import net.openio.opendb.model.value.ValueType;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;
import org.openjdk.jmh.annotations.Timeout;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@State(Scope.Benchmark)
@Warmup(iterations = 0)
@OutputTimeUnit(TimeUnit.SECONDS)
@Measurement(iterations = 1,time = 60, timeUnit = TimeUnit.SECONDS)
@Fork(value = 1)
public class OpenDBBenchmark {

  static OpenDB openDB;
  static ColumnFamilyHandle columnFamilyHandle;
  static int i = 0;

  @Setup
  public void stepUp(){
    openDB = OpenDBImp.open(new Options(), "D:/data/git/jDB/opendb-core/src/test/resources/data/");
    ColumnFamilyDescriptor columnFamilyDescriptor = new ColumnFamilyDescriptor();
    columnFamilyDescriptor.setName("luoluoyuyu");
    columnFamilyDescriptor.setKeyType(KeyType.intKey);
    columnFamilyDescriptor.setValueType(ValueType.intValue);
    columnFamilyDescriptor.setBlockSize(1 << 12);
    openDB.createColumnFamily(columnFamilyDescriptor);
    columnFamilyHandle = openDB.getColumnFamilyHandle("luoluoyuyu").date;
    ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

  }

  @Benchmark
  @Timeout(time = 20, timeUnit = TimeUnit.SECONDS)
  public void addKeyValue(Blackhole bh) throws Exception {
    bh.consume(openDB.put(new IntKey(++i), new IntValue(i, OperationType.insert), columnFamilyHandle));
  }


  @Benchmark
  public void getKeyValue(Blackhole bh) throws Exception {
    bh.consume(openDB.get(new IntKey(--i), columnFamilyHandle));
  }

  @TearDown
  public void closeDatabase() {
    openDB.close();
  }

}
