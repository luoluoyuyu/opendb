package net.openio.jrocksDb.compression;

import lombok.AllArgsConstructor;
import net.openio.jrocksDb.config.Config;
import net.openio.jrocksDb.db.ColumnFamilyHandle;
import net.openio.jrocksDb.db.FileList;
import net.openio.jrocksDb.db.Key;
import net.openio.jrocksDb.mem.*;
import net.openio.jrocksDb.strorage.Block;
import net.openio.jrocksDb.strorage.IndexList;
import net.openio.jrocksDb.strorage.IndexOffset;
import net.openio.jrocksDb.strorage.SSTable;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

@AllArgsConstructor
public class CompressionWorker implements Runnable {

    private volatile boolean end;

    private ConcurrentLinkedQueue<CompressionTask> queue;

    private Block block;

    public static final int sleepTime=1000;


    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {

            }
            while (queue.size() > 0) {
                CompressionTask ct = queue.poll();
                SireCompression(ct);
                ct.columnFamilyHandle.setCompression(false);
            }

            if(end){
                break;
            }
        }

    }

    private void SireCompression(CompressionTask compressionTask) {
        List<SSTable> list = compressionTask.fileList.getSSTable(0);
        List<IndexList> lists = new ArrayList<>();
        int size = list.size();
        for (int i =0 ; i < size; i++) {
            lists.add(i, block.getListAll(list.get(i)));
        }
        Set<Key> set = new HashSet<>();
        for (int i = size - 1; i >= 0; i--) {
            IndexList indexList = lists.get(i);
            IndexList iList = new IndexList();
            for (IndexOffset offset : indexList.getList()) {
                if (!set.contains(offset.getKey())) {
                    set.add(offset.getKey());
                    iList.add(offset);
                }
            }
            lists.add(i, iList);
        }

        List<KeyValueEntry> keyValueEntries = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            keyValueEntries.addAll(block.getKeyEntryByIndexOff(list.get(i).getFileName(), lists.get(i)));
        }

        quickSort(keyValueEntries);
        compressionTask.fileList.setList(LevelCompression(keyValueEntries, compressionTask.fileList, 1, compressionTask.columnFamilyHandle),1);
        compressionTask.fileList.deleteSSTable(0,size,0);

        if(compressionTask.fileList.getLeve1Size()>Config.leve1Num){
            keyValueEntries=null;
            keyValueEntries=new ArrayList<>();
            List<SSTable> ssTables=compressionTask.fileList.getSSTable(1);
            for(int i=ssTables.size()/2;i<ssTables.size();i++) {
                keyValueEntries.addAll(block.getAllKeyValue(ssTables.get(i).getFileName()));
            }
            compressionTask.fileList.setList(LevelCompression(keyValueEntries, compressionTask.fileList, 2, compressionTask.columnFamilyHandle),2);
            compressionTask.fileList.deleteSSTable(ssTables.size()/2,ssTables.size(),1);
        }else {
            return;
        }

        if(compressionTask.fileList.getLeve2Size()>Config.leve2Num){
            keyValueEntries=null;
            keyValueEntries=new ArrayList<>();
            List<SSTable> ssTables=compressionTask.fileList.getSSTable(2);
            for(int i=ssTables.size()/2;i<ssTables.size();i++) {
                keyValueEntries.addAll(block.getAllKeyValue(ssTables.get(i).getFileName()));
            }
            compressionTask.fileList.setList(LevelCompression(keyValueEntries, compressionTask.fileList, 3, compressionTask.columnFamilyHandle),3);
            compressionTask.fileList.deleteSSTable(ssTables.size()/2,ssTables.size(),2);
        }else {
            return;
        }

        if(compressionTask.fileList.getLeve3Size()>Config.leve3Num){
            keyValueEntries=null;
            keyValueEntries=new ArrayList<>();
            List<SSTable> ssTables=compressionTask.fileList.getSSTable(3);
            for(int i=ssTables.size()/2;i<ssTables.size();i++) {
                keyValueEntries.addAll(block.getAllKeyValue(ssTables.get(i).getFileName()));
            }
            compressionTask.fileList.setList(Level4Compression(keyValueEntries, compressionTask.fileList,  compressionTask.columnFamilyHandle),4);
            compressionTask.fileList.deleteSSTable(ssTables.size()/2,ssTables.size(),3);
        }

    }

    private List<SSTable> LevelCompression(List<KeyValueEntry> keyValueEntries, FileList fileList, int level, ColumnFamilyHandle columnFamilyHandle) {
        List<SSTable> ssTables = fileList.getSSTable(level);
        int size = ssTables.size();
        int index = 0;
        List<KeyValueEntry> memTableRep = new ArrayList<>();
        BloomFilter bloomFilter = new BloomFilter();
        List<SSTable> ssTableList = new ArrayList<>();
        int SerSize=0;
        for (int i = 0; i < size; i++) {
            SSTable ssTable = ssTables.get(i);
            if (index < keyValueEntries.size() || ssTable.getMaxKey().compareTo(keyValueEntries.get(i).getKey()) >= 0) {
                List<KeyValueEntry> list = block.getAllKeyValue(ssTable.getFileName());
                while (list.size() > 0) {
                    if (index < keyValueEntries.size()) {
                        KeyValueEntry bKey = list.get(0);
                        KeyValueEntry keyValue = keyValueEntries.get(index);
                        int result = keyValue.getKey().compareTo(bKey);
                        if (result > 0) {
                            KeyValueEntry keyValueEntry;
                            memTableRep.add(keyValueEntry = keyValueEntries.get(index++));
                            bloomFilter.add(keyValueEntry.getKey());
                            SerSize+=keyValueEntry.size();
                        } else if (result < 0) {
                            KeyValueEntry keyValueEntry;
                            memTableRep.add(keyValueEntry = list.remove(0));
                            bloomFilter.add(keyValueEntry.getKey());
                            SerSize+=keyValueEntry.size();
                        } else {
                            KeyValueEntry keyValueEntry;
                            memTableRep.add(keyValueEntry = keyValueEntries.get(index++));
                            list.remove(0);
                            bloomFilter.add(keyValueEntry.getKey());
                            SerSize+=keyValueEntry.size();
                        }

                    } else {
                        KeyValueEntry keyValueEntry;
                        memTableRep.add(keyValueEntry = list.remove(0));
                        bloomFilter.add(keyValueEntry.getKey());
                        SerSize+=keyValueEntry.size();
                    }

                    if (SerSize >= Config.serializerSize) {
                        String fileName = columnFamilyHandle.getFileName()+"llllll";
                        block.createFile(fileName, columnFamilyHandle.getColumnFamilyId());
                        ssTableList.add(block.flush(memTableRep,bloomFilter, fileName));
                        SerSize=0;
                        bloomFilter = new BloomFilter();
                    }
                }
            } else {
                ssTableList.add(ssTable);
            }
        }
        while (index < keyValueEntries.size()) {
            KeyValueEntry keyValueEntry;
            memTableRep.add(keyValueEntry = keyValueEntries.get(index++));
            bloomFilter.add(keyValueEntry.getKey());
            SerSize+=keyValueEntry.size();
            if (SerSize >= Config.serializerSize) {

                String fileName = columnFamilyHandle.getFileName()+"lllll";
                block.createFile(fileName, columnFamilyHandle.getColumnFamilyId());
                ssTableList.add(block.flush(memTableRep,bloomFilter, fileName));
                memTableRep = new ArrayList<>();
                bloomFilter = new BloomFilter();
                SerSize=0;
            }
        }

        if (SerSize > 0) {
            String fileName = columnFamilyHandle.getFileName()+"lllll";
            block.createFile(fileName, columnFamilyHandle.getColumnFamilyId());
            ssTableList.add(block.flush(memTableRep,bloomFilter, fileName));

        }
        return ssTableList;

    }


    private List<SSTable> Level4Compression(List<KeyValueEntry> keyValueEntries, FileList fileList, ColumnFamilyHandle columnFamilyHandle) {
        int level=4;
        List<SSTable> ssTables = fileList.getSSTable(level);
        int size = ssTables.size();
        int index = 0;
        List<KeyValueEntry> memTableRep = new ArrayList<>();
        BloomFilter bloomFilter = new BloomFilter();
        List<SSTable> ssTableList = new ArrayList<>();
        int SerSize=0;
        for (int i = 0; i < size; i++) {
            SSTable ssTable = ssTables.get(i);
            if (index < keyValueEntries.size() || ssTable.getMaxKey().compareTo(keyValueEntries.get(i).getKey()) >= 0) {
                List<KeyValueEntry> list = block.getAllKeyValue(ssTable.getFileName());
                while (list.size() > 0) {
                    if (index < keyValueEntries.size()) {
                        KeyValueEntry bKey = list.get(0);
                        KeyValueEntry keyValue = keyValueEntries.get(index);
                        int result = keyValue.getKey().compareTo(bKey);
                        if (result > 0) {
                            KeyValueEntry keyValueEntry;
                            keyValueEntry = keyValueEntries.get(index++);
                            if(keyValueEntry.getType()!= KeyValueEntry.Type.delete){
                                memTableRep.add(keyValueEntry);
                                bloomFilter.add(keyValueEntry.getKey());
                                SerSize+=keyValueEntry.size();
                            }else {
                                continue;
                            }
                        } else if (result < 0) {
                            KeyValueEntry keyValueEntry;
                            keyValueEntry = list.remove(0);
                            if(keyValueEntry.getType()!= KeyValueEntry.Type.delete){
                                memTableRep.add(keyValueEntry);
                                bloomFilter.add(keyValueEntry.getKey());
                                SerSize+=keyValueEntry.size();
                            }else {
                                continue;
                            }
                        } else {
                            KeyValueEntry keyValueEntry;
                            keyValueEntry = keyValueEntries.get(index++);
                            list.remove(0);
                            if(keyValueEntry.getType()!= KeyValueEntry.Type.delete){
                                memTableRep.add(keyValueEntry);
                                bloomFilter.add(keyValueEntry.getKey());
                                SerSize+=keyValueEntry.size();
                            }else {
                                continue;
                            }

                        }

                    } else {
                        KeyValueEntry keyValueEntry;
                        keyValueEntry = list.remove(0);
                        if(keyValueEntry.getType()!= KeyValueEntry.Type.delete){
                            memTableRep.add(keyValueEntry);
                            bloomFilter.add(keyValueEntry.getKey());
                            SerSize+=keyValueEntry.size();
                        }else {
                            continue;
                        }
                    }

                    if (SerSize >= Config.serializerSize) {
                        String fileName = columnFamilyHandle.getFileName();
                        block.createFile(fileName, columnFamilyHandle.getColumnFamilyId());
                        ssTableList.add(block.flush(memTableRep,bloomFilter, fileName));
                        SerSize=0;
                        bloomFilter = new BloomFilter();
                    }
                }
            } else {
                ssTableList.add(ssTable);
            }
        }
        while (index < keyValueEntries.size()) {
            KeyValueEntry keyValueEntry;
            keyValueEntry = keyValueEntries.get(index++);
            if(keyValueEntry.getType()!= KeyValueEntry.Type.delete){
                memTableRep.add(keyValueEntry);
                bloomFilter.add(keyValueEntry.getKey());
                SerSize+=keyValueEntry.size();
            }else {
                continue;
            }
            if (SerSize >= Config.serializerSize) {

                String fileName = columnFamilyHandle.getFileName();
                block.createFile(fileName, columnFamilyHandle.getColumnFamilyId());
                ssTableList.add(block.flush(memTableRep,bloomFilter, fileName));
                memTableRep = new ArrayList<>();
                bloomFilter = new BloomFilter();
                SerSize=0;
            }
        }

        if (SerSize > 0) {
            String fileName = columnFamilyHandle.getFileName();
            block.createFile(fileName, columnFamilyHandle.getColumnFamilyId());
            ssTableList.add(block.flush(memTableRep,bloomFilter, fileName));

        }
        return ssTableList;

    }


    private void quickSort(List<KeyValueEntry> list) {
        if (list == null || list.size() <= 1) {
            return;
        }
        int pivotIndex = list.size() / 2;
        KeyValueEntry pivotValue = list.get(pivotIndex);
        ArrayList<KeyValueEntry> left = new ArrayList<KeyValueEntry>();
        ArrayList<KeyValueEntry> right = new ArrayList<KeyValueEntry>();
        for (int i = 0; i < list.size(); i++) {
            if (i == pivotIndex) {
                continue;
            }
            KeyValueEntry value = list.get(i);
            if (value.getKey().compareTo(pivotValue.getKey()) < 0) {
                left.add(value);
            } else {
                right.add(value);
            }
        }
        quickSort(left);
        quickSort(right);
        list.clear();
        list.addAll(left);
        list.add(pivotValue);
        list.addAll(right);
    }

    public CompressionWorker(ConcurrentLinkedQueue<CompressionTask> queue){
        this.queue=queue;
        block = new Block();
    }
}