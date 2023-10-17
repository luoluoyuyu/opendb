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
import net.openio.opendb.mem.KeyValueGenerator;
import net.openio.opendb.model.ColumnFamilyDescriptor;
import net.openio.opendb.model.Options;
import net.openio.opendb.model.Status;
import net.openio.opendb.model.key.Key;
import net.openio.opendb.model.key.KeyType;
import net.openio.opendb.model.value.Value;
import net.openio.opendb.model.value.ValueType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class OpenDBTest {
//
//  static OpenDB openDB;
//  static ColumnFamilyHandle columnFamilyHandle;
//  static List<Key> list =new LinkedList<>();
//
//  static final int THREAD_POOL_SIZE = 1;
//  static final int TASK_COUNT = 1;
//  static final  ExecutorService executorService = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
//
//  static {
//    openDB = OpenDBImp.open(new Options(),"src/test/resources/data/");
//    ColumnFamilyDescriptor columnFamilyDescriptor= new ColumnFamilyDescriptor();
//    columnFamilyDescriptor.setName("luoluoyuyu");
//    columnFamilyDescriptor.setKeyType(KeyType.intKey);
//    columnFamilyDescriptor.setValueType(ValueType.intValue);
//    columnFamilyDescriptor.setBlockSize(1<<12);
//    openDB.createColumnFamily(columnFamilyDescriptor);
//
//
//    columnFamilyHandle=openDB.getColumnFamilyHandle("luoluoyuyu").date;
//
//  }
//
//  @Test
//  public void test(){
//
//
//    for (int i = 0; i < TASK_COUNT; i++) {
//      executorService.execute(new OpenDBTestTask());
//    }
//
//    executorService.shutdown();
//    while (!executorService.isTerminated()) {
//
//    }
//
//
//    openDB.close();
//
//  }
//
//  class OpenDBTestTask implements Runnable {
//    @Override
//    public void run() {
//
//
//      for (int i = 0; i < 100000; i++) {
//        Key key = KeyValueGenerator.generateRandomIntKey();
//        Value value = KeyValueGenerator.generateRandomIntValue();
//        list.add(key);
//        openDB.put(key, value, columnFamilyHandle);
//
//      }
//
//      for (Key key: list){
//        Status status=openDB.get(key,columnFamilyHandle);
//        System.out.println(status.date);
//        Assertions.assertTrue(status.date!=null);
//      }
//    }
//  }
}
