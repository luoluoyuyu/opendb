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
package net.openio.opendb.model.key;

import io.netty.buffer.ByteBuf;
import net.openio.opendb.tool.codec.key.BytesKeyProtoCodec;
import net.openio.opendb.tool.codec.key.DoubleKeyProtoCodec;
import net.openio.opendb.tool.codec.key.FloatKeyProtoCodec;
import net.openio.opendb.tool.codec.key.IntKeyProtoCodec;
import net.openio.opendb.tool.codec.key.KeyProtoCodec;
import net.openio.opendb.tool.codec.key.LongKeyProtoCodec;
import net.openio.opendb.tool.codec.key.StringKeyProtoCodec;


public enum KeyType {
  intKey(1, new IntKeyProtoCodec()),

  longKey(2, new LongKeyProtoCodec()),

  bytesKey(3, new BytesKeyProtoCodec()),

  doubleKey(4, new DoubleKeyProtoCodec()),

  floatKey(5, new FloatKeyProtoCodec()),

  stringKey(6, new StringKeyProtoCodec());

  public static KeyType get(int tag) {
    if (tag == 1) {
      return KeyType.intKey;
    }
    if (tag == 2) {
      return KeyType.longKey;
    }
    if (tag == 3) {
      return KeyType.bytesKey;
    }
    if (tag == 4) {
      return KeyType.doubleKey;
    }
    if (tag == 5) {
      return KeyType.floatKey;
    }
    if (tag == 6) {
      return KeyType.stringKey;
    }
    return null;
  }

  private final int num;

  private final KeyProtoCodec keyProtoCodec;

  KeyType(int num, KeyProtoCodec keyProtoCodec) {
    this.num = num;
    this.keyProtoCodec = keyProtoCodec;
  }

  public int getNum() {
    return this.num;
  }

  public Key decode(ByteBuf buf, int length){
    return keyProtoCodec.decode(buf, length);
  }

  public void encode(ByteBuf buf, Key key){
    keyProtoCodec.encode(buf, key);
  }

  public int getByteSize(Key key){
    return keyProtoCodec.getByteSize(key);
  }

  public boolean isType(Key key){
    switch (num) {
      case 1:
        return key instanceof IntKey;
      case 2:
        return key instanceof LongKey;
      case 3:
        return key instanceof BytesKey;
      case 4:
        return key instanceof DoubleKey;
      case 5:
        return key instanceof FloatKey;
      case 6:
        return key instanceof StringKey;
      default:
        return false;
    }
  }
}
