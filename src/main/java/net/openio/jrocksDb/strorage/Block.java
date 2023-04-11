package net.openio.jrocksDb.strorage;

import net.openio.jrocksDb.config.Config;
import net.openio.jrocksDb.db.ColumnFamilyId;
import net.openio.jrocksDb.db.IntKey;
import net.openio.jrocksDb.db.Key;
import net.openio.jrocksDb.mem.BloomFilter;
import net.openio.jrocksDb.mem.KeyValueEntry;
import net.openio.jrocksDb.mem.MemTable;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class Block {

    private final FileHeadBlock fileHeadBlock;

    private final DataBlocks dataBlocks;

    private final IndexBlocks indexBlocks;

    private final BloomBlocks bloomBlocks;

    private final String filePath;

    private final static int dataBlockSize = 512;

    public SSTable flush(List<KeyValueEntry> list, BloomFilter bloomFilter, String fileName) {
        File file = new File(filePath + fileName);
        Key minKey = null;
        Key maxKey = null;
        int seek = 0;
        try {
            RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
            minKey = list.get(0).getKey();
            maxKey = list.get(list.size() - 1).getKey();
            List<IndexList> lists = new ArrayList<>();
            IndexList indexOffsets = new IndexList();
            seek = FileHeadBlock.FileHeadSize;

            //data
            fileHeadBlock.setDataOffset(randomAccessFile.getChannel(), seek);
            int i = 0;
            for (KeyValueEntry keyValueEntry : list) {

                indexOffsets.add(new IndexOffset(keyValueEntry.getKey(), seek));
                seek += dataBlocks.flush(randomAccessFile.getChannel(), seek, keyValueEntry);
                if (i >= dataBlockSize) {
                    lists.add(indexOffsets);
                    indexOffsets = new IndexList();
                    i=0;
                }
                i++;
            }
            if (indexOffsets.getList().size() > 0) {
                lists.add(indexOffsets);
            }
            //bloom
            fileHeadBlock.setBloomFilterOffset(randomAccessFile.getChannel(), seek);
            seek += bloomBlocks.flush(randomAccessFile.getChannel(), seek, bloomFilter.getData());

            //meta Index

            fileHeadBlock.setMetaOffset(randomAccessFile.getChannel(), seek);
            IndexList firstIndexOffer = new IndexList();
            for (IndexList indexList : lists) {
                firstIndexOffer.add(new IndexOffset(indexList.getList().get(0).getKey(), seek));
                seek += indexBlocks.flushFile(randomAccessFile.getChannel(), seek, indexList);
            }

            //firstIndex

            fileHeadBlock.setIndexOffset(randomAccessFile.getChannel(), seek);

            indexBlocks.flushFile(randomAccessFile.getChannel(), seek, firstIndexOffer);

            randomAccessFile.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        return new SSTable(fileName,minKey,maxKey, seek);
    }


    public SSTable flush(MemTable memTable, String fileName) {
        File file = new File(filePath + fileName);
        Key minKey = null;
        Key maxKey = null;
        int seek = 0;
        try {
            RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
            List<KeyValueEntry> list = memTable.getAllKeyValue();
            minKey = list.get(0).getKey();
            maxKey = list.get(list.size() - 1).getKey();
            List<IndexList> lists = new ArrayList<>();
            IndexList indexOffsets = new IndexList();
            seek = FileHeadBlock.FileHeadSize;

            //data
            fileHeadBlock.setDataOffset(randomAccessFile.getChannel(), seek);
            int i = 0;
            for (KeyValueEntry keyValueEntry : list) {

                indexOffsets.add(new IndexOffset(keyValueEntry.getKey(), seek));
                seek += dataBlocks.flush(randomAccessFile.getChannel(), seek, keyValueEntry);
                if (i >= dataBlockSize) {
                    lists.add(indexOffsets);
                    indexOffsets = new IndexList();
                    i=0;
                }
                i++;
            }
            if (indexOffsets.getList().size() > 0) {
                lists.add(indexOffsets);
            }
            //bloom
            fileHeadBlock.setBloomFilterOffset(randomAccessFile.getChannel(), seek);
            seek += bloomBlocks.flush(randomAccessFile.getChannel(), seek, memTable.getBloom());

            //meta Index

            fileHeadBlock.setMetaOffset(randomAccessFile.getChannel(), seek);
            IndexList firstIndexOffer = new IndexList();
            for (IndexList indexList : lists) {
                firstIndexOffer.add(new IndexOffset(indexList.getList().get(0).getKey(), seek));
                seek += indexBlocks.flushFile(randomAccessFile.getChannel(), seek, indexList);
            }

            //firstIndex

            fileHeadBlock.setIndexOffset(randomAccessFile.getChannel(), seek);

            indexBlocks.flushFile(randomAccessFile.getChannel(), seek, firstIndexOffer);

            randomAccessFile.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        memTable.needFlush();
        return new SSTable(fileName,minKey,maxKey, seek);
    }


    public boolean createFile(String fileName, ColumnFamilyId columnFamilyId) {
        File file = new File(filePath + fileName);
        if (file.exists()) {
            return false;
        }
        try {
            RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
            fileHeadBlock.setColumnFamilyId(randomAccessFile.getChannel(), columnFamilyId.getCId());
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }


    public KeyValueEntry getKeyValue(Key key, String fileName) {
        KeyValueEntry keyValueEntries = null;
        File file = new File(filePath + fileName);

        try {
            RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
            int seek = fileHeadBlock.getIndexOffset(randomAccessFile.getChannel());
            IndexList fIndex = indexBlocks.getList(randomAccessFile.getChannel(), seek);
            List<IndexOffset> fList = fIndex.list;
            IndexOffset fIndexOffer = fList.get(binarySearch(fList, key));
            IndexList tIndex = indexBlocks.getList(randomAccessFile.getChannel(), fIndexOffer.getOffset());
            List<IndexOffset> tList = tIndex.list;
            IndexOffset tIndexOffset = tList.get(binarySearch(tList, key));
            if (tIndexOffset.getKey().compareTo(key) == 0) {
                keyValueEntries = dataBlocks.getKeyValue(randomAccessFile.getChannel(), tIndexOffset.getOffset());
            }
            randomAccessFile.close();
        } catch (IOException ioException) {
            ioException.printStackTrace();
            return null;
        }

        return keyValueEntries;
    }


    public List<KeyValueEntry> getAllKeyValue( String fileName) {
        List<KeyValueEntry> keyValueEntries = new ArrayList<>();
        File file = new File(filePath + fileName);

        try {
            RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
            int seek = fileHeadBlock.getDataOffset(randomAccessFile.getChannel());
            int end = fileHeadBlock.getBloomFilterOffset(randomAccessFile.getChannel());
            while (seek < end) {
                seek += dataBlocks.getKeyValue(randomAccessFile.getChannel(), seek, keyValueEntries);
            }
            randomAccessFile.close();
        } catch (IOException ioException) {
            ioException.printStackTrace();
            return null;
        }

        return keyValueEntries;
    }

    public List<KeyValueEntry> getKeyEntryByIndexOff(String fileName,IndexList indexList){
        List<KeyValueEntry> keyValueEntries = new ArrayList<>();
        File file = new File(filePath + fileName);

        try {
            RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");

            for(IndexOffset indexO:indexList.getList()) {
                dataBlocks.getKeyValue(randomAccessFile.getChannel(), indexO.getOffset(), keyValueEntries);
            }
            randomAccessFile.close();
        } catch (IOException ioException) {
            ioException.printStackTrace();
            return null;
        }

        return keyValueEntries;
    }



    public IndexList getListAll(SSTable ssTable){
        File file=new File(filePath+ssTable.getFileName());
        if(!file.exists()){
            throw new RuntimeException("file not exist");
        }
        IndexList indexList=null;
        try {
            RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
            int seek = fileHeadBlock.getMetaOffset(randomAccessFile.getChannel());
            int indexSeek = fileHeadBlock.getIndexOffset(randomAccessFile.getChannel());
            indexList=indexBlocks.getMetaIndexList(randomAccessFile.getChannel(),indexSeek,seek);
            randomAccessFile.close();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

        return indexList;
    }



    private static int binarySearch(List<IndexOffset> arr, Key key) {
        int low = 0;
        int high = arr.size() - 1;
        while (low <= high) {
            int mid = low + (high - low) / 2;
            Key midVal = arr.get(mid).getKey();
            if (midVal.compareTo(key) < 0) {
                low = mid + 1;
            } else if (midVal.compareTo(key) > 0) {
                high = mid - 1;
            } else {
                return mid;
            }
        }
        return low; // 返回-1表示没找到
    }

    public Block() {
        fileHeadBlock = new FileHeadBlock();
        dataBlocks = new DataBlocks();
        indexBlocks = new IndexBlocks();
        bloomBlocks = new BloomBlocks();
        filePath = Config.StoragePath;
    }


}
