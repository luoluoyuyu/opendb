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
package net.openio.opendb.storage.metadata;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.openio.opendb.tool.codec.meta.MetaDataProtoCodec;


import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;

public class MetaMetaStorage {

  private final String fileName = "metaData";

  private final String temFileName = "temMetaData";

  public synchronized DataMeta getMetaData(String dir) {
    try {
      return getMataData(dir, fileName);
    } catch (Exception e) {
      e.printStackTrace();
    }

    try {
      return getMataData(dir, temFileName);
    } catch (IOException ex) {
      return null;
    }
  }

  public synchronized void flush(String dir, DataMeta metaData) {
    try {
      flush(dir, fileName, metaData);
    } catch (Exception e) {
      e.printStackTrace();
    }

    try {
      flush(dir, temFileName, metaData);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public DataMeta getMataData(String dir, String fileName) throws IOException {
    RandomAccessFile randomAccessFile = null;
    FileChannel fileChannel = null;
    DataMeta meta = null;
    try {
      File file = new File(dir + fileName);
      if (!file.exists()) {
        return null;
      }
      randomAccessFile = new RandomAccessFile(file, "rw");
      fileChannel = randomAccessFile.getChannel();
      ByteBuf buf = Unpooled.buffer(4);
      fileChannel.position(0);
      buf.writerIndex(4);
      fileChannel.read(buf.nioBuffer());
      int length = buf.readInt();
      buf.release();
      buf = Unpooled.buffer(length);
      buf.writerIndex(length);
      fileChannel.read(buf.nioBuffer());
      meta = MetaDataProtoCodec.decode(buf, length);
      buf.release();
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      close(randomAccessFile, fileChannel);
    }
    return meta;
  }

  public void flush(String dir, String fileName, DataMeta metaData) throws IOException {
    RandomAccessFile randomAccessFile = null;
    FileChannel fileChannel = null;
    try {
      File file = new File(dir + fileName);
      if (!file.exists()) {
        file.createNewFile();
      }
      randomAccessFile = new RandomAccessFile(file, "rw");
      fileChannel = randomAccessFile.getChannel();
      ByteBuf buf = Unpooled.buffer(4);
      fileChannel.position(0);
      int length = MetaDataProtoCodec.getByteSize(metaData);
      buf.writeInt(length);
      buf.writerIndex(4);
      fileChannel.write(buf.nioBuffer());
      buf.release();
      buf = Unpooled.buffer(length);
      MetaDataProtoCodec.encode(buf, metaData);
      buf.writerIndex(length);
      fileChannel.write(buf.nioBuffer());
      fileChannel.force(true);
      buf.release();
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      close(randomAccessFile, fileChannel);
    }

  }

  private void close(RandomAccessFile randomAccessFile, FileChannel fileChannel) throws IOException {
    if (randomAccessFile != null) {
      randomAccessFile.close();
    }
    if (fileChannel != null) {
      fileChannel.close();
    }

  }
}
