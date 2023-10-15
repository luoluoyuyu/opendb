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

import net.openio.opendb.model.ColumnFamilyDescriptor;
import net.openio.opendb.model.Status;
import net.openio.opendb.model.key.Key;
import net.openio.opendb.model.value.ColumnFamilyHandle;
import net.openio.opendb.model.value.Value;
import net.openio.opendb.transaction.Transaction;

import java.util.List;

public interface OpenDB {

  Status<Value> get(Key key, ColumnFamilyHandle columnFamilyHandle);

  Status<Value> put(Key key, Value value, ColumnFamilyHandle columnFamilyHandle);

  Status<Value> update(Key key, Value value, ColumnFamilyHandle columnFamilyHandle);

  Status<Value> delete(Key key, ColumnFamilyHandle columnFamilyHandle);

  Status<ColumnFamilyHandle> getColumnFamilyHandle(String name);

  Status<ColumnFamilyDescriptor> getColumnFamily(String name);

  Status<ColumnFamilyDescriptor> createColumnFamily(ColumnFamilyDescriptor columnFamilyDescriptor);

  Status<List<ColumnFamilyDescriptor>> getAllColumnFamily();

  Transaction createTransaction(Snapshot snapshot);

  Snapshot getSnapshot();

}
