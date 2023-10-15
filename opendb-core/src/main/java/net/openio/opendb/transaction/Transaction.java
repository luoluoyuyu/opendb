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
package net.openio.opendb.transaction;

import net.openio.opendb.model.Status;
import net.openio.opendb.model.key.Key;
import net.openio.opendb.model.value.Value;

public interface Transaction {
  Status setSnapshot();


  Status clearSnapshot();


  Status commit();

  Status rollback();

  Status<Value> get(Key key, String columnFamilyName);

  Status<Value> getForUpdate(Key key, Value value, String columnFamilyName);

  Status<Value> put(Key key, Value value, String columnFamilyName);

  Status<Value> delete(Key key, String columnFamilyName);

}
