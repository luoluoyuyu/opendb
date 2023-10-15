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
package net.openio.opendb.storage.sstable;


import net.openio.opendb.compression.CompactionIterator;
import net.openio.opendb.db.KeyValueEntry;
import net.openio.opendb.mem.BloomFilter;
import net.openio.opendb.mem.MemTable;
import net.openio.opendb.mem.MemTableRep;
import net.openio.opendb.memarena.MemArena;
import net.openio.opendb.model.key.Key;
import net.openio.opendb.model.key.KeyType;
import net.openio.opendb.model.value.ValueType;
import net.openio.opendb.tool.IDGenerator;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class SSTableStorage {


  private int pageSize = 1 << 16;

  private MemArena memArena;

  private final FileHeadBlockStorage fileHeadBlockStorage;

  private final DataBlockStorage blockStorage;

  private final IndexBlockStorage indexBlockStorage;

  private final BloomBlockStorage bloomBlockStorage;

  private final MetaBlockStorage metaBlockStorage;

  private final String filePath;

  private final int fileHeadSize = 1 << 9;


  public SSTable flush(MemTable memTable, KeyType keyType, ValueType valueType) throws IOException {
    String fileName = IDGenerator.generateUniqueFileName();
    if (!createFile(fileName)) {
      throw new RuntimeException("can not create file");
    }
    return flush(memTable, keyType, valueType, fileName);
  }

  public List<SSTable> compaction(CompactionIterator iterator, KeyType keyType, ValueType valueType, int size) throws IOException {
    List<SSTable> ssTables = new ArrayList<>();
    while (iterator.hasNext()) {
      String fileName = IDGenerator.generateUniqueFileName();
      if (!createFile(fileName)) {
        throw new RuntimeException("can not create file");
      }
      SSTable ssTable = compaction(iterator, keyType, valueType, fileName, size);
      ssTables.add(ssTable);
    }
    return ssTables;
  }

  public SSTable flush(MemTable memTable, KeyType keyType, ValueType valueType, String fileName) throws IOException {
    SSTable ssTable = new SSTable();
    ssTable.setFileName(fileName);
    ssTable.setKeyType(keyType);
    ssTable.setValueType(valueType);
    ssTable.setCreateTime(new Date().getTime());

    List<IndexData> indexBlocks = new LinkedList<>();
    FileHeadBlock fileHeadBlock = new FileHeadBlock();
    int seek = 0;
    RandomAccessFile randomAccessFile = null;
    FileChannel fileChannel = null;
    try {
      File file = new File(filePath + fileName);
      randomAccessFile = new RandomAccessFile(file, "rw");
      fileChannel = randomAccessFile.getChannel();
      fileHeadBlock.setDataOfferSeek(seek);
      seek = flushDataBlock(fileChannel, ssTable, memTable.getMemTableRep(), indexBlocks, seek);
      fileHeadBlock.setDataOfferSize(seek - fileHeadBlock.getDataOfferSeek());


      MetaBlock metaBlock = new MetaBlock(ssTable);
      fileHeadBlock.setIndexOfferSeek(seek);
      seek = flushIndexBlock(fileChannel, ssTable, metaBlock, indexBlocks, seek);
      fileHeadBlock.setIndexOfferSize(seek - fileHeadBlock.getIndexOfferSeek());

      fileHeadBlock.setMetaOfferSeek(seek);
      seek = flushMetaBlock(fileChannel, metaBlock, seek);
      fileHeadBlock.setMetaOfferSize(seek - fileHeadBlock.getMetaOfferSeek());

      fileHeadBlock.setBloomOfferSeek(seek);
      seek = flushBloomBlock(fileChannel, memTable.getBloomFilter(), seek);
      fileHeadBlock.setBloomOfferSize(seek - fileHeadBlock.getBloomOfferSeek());

      ssTable.setFileHeadSeek(seek);
      seek = flushFileHead(fileChannel, ssTable, fileHeadBlock, seek);
      ssTable.setFileHeadSize(seek - ssTable.getFileHeadSeek());
      ssTable.setSize(seek);
    } catch (IOException e) {
      e.printStackTrace();
      throw new RuntimeException("can not create SSTable");
    } finally {
      close(randomAccessFile, fileChannel);
    }

    return ssTable;
  }

  public SSTable compaction(Iterator<KeyValueEntry> iterator,
                            KeyType keyType, ValueType valueType, String fileName, int size) throws IOException {
    SSTable ssTable = new SSTable();
    ssTable.setFileName(fileName);
    ssTable.setKeyType(keyType);
    ssTable.setValueType(valueType);
    ssTable.setCreateTime(new Date().getTime());

    List<IndexData> indexBlocks = new LinkedList<>();
    FileHeadBlock fileHeadBlock = new FileHeadBlock();
    int seek = 0;
    RandomAccessFile randomAccessFile = null;
    FileChannel fileChannel = null;
    BloomFilter bloomFilter = new BloomFilter();
    try {
      File file = new File(filePath + fileName);
      randomAccessFile = new RandomAccessFile(file, "rw");
      fileChannel = randomAccessFile.getChannel();
      fileHeadBlock.setDataOfferSeek(seek);

      seek = flushDataBlock(fileChannel, ssTable, iterator, bloomFilter, indexBlocks, seek, size);
      fileHeadBlock.setDataOfferSize(seek - fileHeadBlock.getDataOfferSeek());


      MetaBlock metaBlock = new MetaBlock(ssTable);
      fileHeadBlock.setIndexOfferSeek(seek);
      seek = flushIndexBlock(fileChannel, ssTable, metaBlock, indexBlocks, seek);
      fileHeadBlock.setIndexOfferSize(seek - fileHeadBlock.getIndexOfferSeek());

      fileHeadBlock.setMetaOfferSeek(seek);
      seek = flushMetaBlock(fileChannel, metaBlock, seek);
      fileHeadBlock.setMetaOfferSize(seek - fileHeadBlock.getMetaOfferSeek());

      fileHeadBlock.setBloomOfferSeek(seek);
      seek = flushBloomBlock(fileChannel, bloomFilter, seek);
      fileHeadBlock.setBloomOfferSize(seek - fileHeadBlock.getBloomOfferSeek());

      ssTable.setFileHeadSeek(seek);
      seek = flushFileHead(fileChannel, ssTable, fileHeadBlock, seek);
      ssTable.setFileHeadSize(seek - ssTable.getFileHeadSeek());
      ssTable.setSize(seek);
    } catch (IOException e) {
      e.printStackTrace();
      throw new RuntimeException("can not create SSTable");
    } finally {
      close(randomAccessFile, fileChannel);
    }

    return ssTable;
  }


  private int flushDataBlock(FileChannel fileChannel, SSTable ssTable, MemTableRep memTableRep,
                             List<IndexData> indexBlocks, int seek) throws IOException {
    fileChannel.position(seek);
    Iterator<KeyValueEntry> iterator = memTableRep.iterator();
    KeyValueEntry keyValueEntry = iterator.next();
    int dataId = 1;
    DataBlock dataBlock = new DataBlock(dataId, ssTable, seek);
    dataBlock.add(keyValueEntry.getValue());
    IndexData indexData = new IndexData(dataId, ssTable, seek);
    indexData.add(keyValueEntry.getKey());
    while (iterator.hasNext()) {

      keyValueEntry = iterator.next();
      if (!dataBlock.addValue(keyValueEntry.getValue(), pageSize)) {
        seek += blockStorage.flush(fileChannel, seek, dataBlock);
        indexData.setDataBlockSize(seek - indexData.getOffset());
        dataId++;
        indexBlocks.add(indexData);
        indexData = new IndexData(dataId, ssTable, seek);
        dataBlock = new DataBlock(dataId, ssTable, seek);
        indexData.add(keyValueEntry.getKey());
        dataBlock.add(keyValueEntry.getValue());
      } else {
        indexData.add(keyValueEntry.getKey());
      }

    }

    if (dataBlock.getValueNum() > 0) {
      seek += blockStorage.flush(fileChannel, seek, dataBlock);
      indexData.setDataBlockSize(seek - indexData.getOffset());
      indexBlocks.add(indexData);
    }
    return seek;
  }

  private int flushDataBlock(FileChannel fileChannel, SSTable ssTable, Iterator<KeyValueEntry> iterator, BloomFilter bloomFilter,
                             List<IndexData> indexBlocks, int seek, int size) throws IOException {
    fileChannel.position(seek);
    KeyValueEntry key = iterator.next();
    bloomFilter.add(key.getKey());
    int dataId = 1;
    int s = 0;
    DataBlock dataBlock = new DataBlock(dataId, ssTable, seek);
    dataBlock.add(key.getValue());
    IndexData indexData = new IndexData(dataId, ssTable, seek);
    indexData.add(key.getKey());
    while (iterator.hasNext() && s < size) {

      key = iterator.next();
      bloomFilter.add(key.getKey());
      if (!dataBlock.addValue(key.getValue(), pageSize)) {
        seek += blockStorage.flush(fileChannel, seek, dataBlock);
        indexData.setDataBlockSize(seek - indexData.getOffset());
        dataId++;
        indexBlocks.add(indexData);
        s += indexData.getSize() + indexBlocks.size();
        indexData = new IndexData(dataId, ssTable, seek);
        dataBlock = new DataBlock(dataId, ssTable, seek);
        indexData.add(key.getKey());
        dataBlock.add(key.getValue());
      } else {
        indexData.add(key.getKey());
      }

    }

    if (dataBlock.getValueNum() > 0) {
      if (indexData.getNum() == 0) {
        System.out.println(indexData);
      }
      seek += blockStorage.flush(fileChannel, seek, dataBlock);
      indexData.setDataBlockSize(seek - indexData.getOffset());
      indexBlocks.add(indexData);
    }
    return seek;
  }

  private int flushIndexBlock(FileChannel fileChannel, SSTable ssTable, MetaBlock metaBlock,
                              List<IndexData> indexDataList, int seek) throws IOException {
    fileChannel.position(seek);
    long id = 1;
    IndexBlock indexBlock = new IndexBlock(id, ssTable, seek);
    Iterator<IndexData> iterator = indexDataList.iterator();
    IndexData indexD = iterator.next();
    indexBlock.add(indexD);
    Key minKey = indexD.get(0);
    ssTable.setMinKey(minKey);
    IndexData lastIndexData = indexBlock.getDataList().get(indexBlock.getNum() - 1);
    ssTable.setMaxValue(lastIndexData.get(lastIndexData.getNum() - 1));
    while (iterator.hasNext()) {
      indexD = iterator.next();

      if (!indexBlock.addKey(indexD, pageSize)) {
        int size = indexBlock.getSize();
        if (size < pageSize) {
          IndexData indexData1 = indexD.getMaxIndexData(size);
//          if(indexBlock.get(indexBlock.getNum()-1).getNum()==0){
//
//          }
          if (indexData1.getNum() > 0) {
            indexBlock.add(indexData1);
          }
        }
        MetaData metaData = new MetaData(id, minKey, seek, 0, ssTable);
        seek = indexBlockStorage.flush(fileChannel, seek, indexBlock);
        metaData.setSize(seek - metaData.getOffset());
        metaBlock.add(metaData);
        indexBlock = new IndexBlock(++id, ssTable, seek);
      }
      indexBlock.add(indexD);

    }
    if (indexBlock.getNum() > 0) {
      MetaData metaData = new MetaData(id, minKey, seek, 0, ssTable);
      seek = indexBlockStorage.flush(fileChannel, seek, indexBlock);
      metaData.setSize(seek - metaData.getOffset());
      metaBlock.add(metaData);
    }
    return seek;
  }

  private int flushMetaBlock(FileChannel fileChannel, MetaBlock metaBlock, int seek) throws IOException {
    return metaBlockStorage.flush(fileChannel, seek, metaBlock);
  }

  private int flushBloomBlock(FileChannel fileChannel, BloomFilter bloomFilter, int seek) throws IOException {
    return bloomBlockStorage.flush(fileChannel, seek, bloomFilter);
  }

  private int flushFileHead(FileChannel fileChannel, SSTable ssTable, FileHeadBlock fileHeadBlock, int seek) throws IOException {
    return fileHeadBlockStorage.flush(fileChannel, ssTable, fileHeadBlock, seek);
  }


  public DataBlock getDataBlock(SSTable ssTable, int seek, int size) throws IOException {
    RandomAccessFile randomAccessFile = null;
    FileChannel fileChannel = null;
    try {
      File file = new File(filePath + ssTable.getFileName());
      randomAccessFile = new RandomAccessFile(file, "rw");
      return blockStorage.getDataBlock(ssTable, fileChannel = randomAccessFile.getChannel(), seek, size);
    } catch (IOException e) {
      throw new RuntimeException("can not readDataBlock");
    } finally {
      close(randomAccessFile, fileChannel);
    }
  }

  public IndexBlock getIndexBlock(SSTable ssTable, int seek, int size) throws IOException {
    RandomAccessFile randomAccessFile = null;
    FileChannel fileChannel = null;

    try {
      File file = new File(filePath + ssTable.getFileName());
      randomAccessFile = new RandomAccessFile(file, "rw");
      return indexBlockStorage.getIndexBlock(ssTable, fileChannel = randomAccessFile.getChannel(), seek, size);
    } catch (IOException e) {
      throw new RuntimeException("can not read IndexBlock");
    } finally {
      close(randomAccessFile, fileChannel);
    }
  }

  public MetaBlock getMetaBlock(SSTable ssTable, int seek, int size) throws IOException {
    RandomAccessFile randomAccessFile = null;
    FileChannel fileChannel = null;
    try {
      File file = new File(filePath + ssTable.getFileName());
      randomAccessFile = new RandomAccessFile(file, "rw");
      return metaBlockStorage.getMetaBlock(ssTable, fileChannel = randomAccessFile.getChannel(), seek, size);
    } catch (IOException e) {
      throw new RuntimeException("can not read MetaBlock");
    } finally {
      close(randomAccessFile, fileChannel);
    }
  }

  public BloomFilter getBloomFilter(SSTable ssTable, int seek, int size) throws IOException {
    RandomAccessFile randomAccessFile = null;
    FileChannel fileChannel = null;
    try {
      File file = new File(filePath + ssTable.getFileName());
      randomAccessFile = new RandomAccessFile(file, "rw");
      return bloomBlockStorage.getBloomFilter(fileChannel = randomAccessFile.getChannel(), seek, size);
    } catch (IOException e) {
      throw new RuntimeException("can not read BloomBlock");
    } finally {
      close(randomAccessFile, fileChannel);
    }
  }

  public FileHeadBlock getFileHead(SSTable ssTable) throws IOException {
    RandomAccessFile randomAccessFile = null;
    FileChannel fileChannel = null;
    try {
      File file = new File(filePath + ssTable.getFileName());
      randomAccessFile = new RandomAccessFile(file, "rw");
      return fileHeadBlockStorage.getFileHeadBlock(fileChannel = randomAccessFile.getChannel(), ssTable.getFileHeadSeek(),
        ssTable.getFileHeadSize());
    } catch (IOException e) {
      throw new RuntimeException("can not read MetaBlock");
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


  private boolean createFile(String fileName) {
    File file = new File(filePath + fileName);

    if (file.exists()) {
      return false;
    }

    try {
      return file.createNewFile();
    } catch (IOException e) {
      e.printStackTrace();
      return false;
    }
  }


  private static int binarySearch(List<Key> arr, Key key) {
    int low = 0;
    int high = arr.size() - 1;
    while (low <= high) {
      int mid = low + (high - low) / 2;
      Key midVal = arr.get(mid);
      if (midVal.compareTo(key) < 0) {
        low = mid + 1;
      } else if (midVal.compareTo(key) > 0) {
        high = mid - 1;
      } else {
        return mid;
      }
    }
    return low;
  }


  public SSTableStorage(int pageSize, int memCacheSize, String fileDir) {
    this.pageSize = pageSize;
    memArena = new MemArena(memCacheSize, pageSize);
    fileHeadBlockStorage = new FileHeadBlockStorage(memArena, pageSize);
    metaBlockStorage = new MetaBlockStorage(memArena, pageSize);
    blockStorage = new DataBlockStorage(memArena, pageSize);
    indexBlockStorage = new IndexBlockStorage(memArena, pageSize);
    bloomBlockStorage = new BloomBlockStorage(memArena, pageSize);
    filePath = fileDir;
  }

}
