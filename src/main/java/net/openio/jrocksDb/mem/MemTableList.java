package net.openio.jrocksDb.mem;

import net.openio.jrocksDb.config.TransactionConfig;
import net.openio.jrocksDb.db.ColumnFamilyHandle;
import net.openio.jrocksDb.db.ColumnFamilyId;
import net.openio.jrocksDb.db.Key;
import net.openio.jrocksDb.db.Value;
import net.openio.jrocksDb.log.WALLog;
import net.openio.jrocksDb.strorage.FlushTask;
import net.openio.jrocksDb.transaction.Snapshot;
import net.openio.jrocksDb.transaction.Transaction;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

public class MemTableList {


    volatile MemTable Table;

    List<MemTable> immTable;

    ColumnFamilyHandle columnFamilyHandle;

    WALLog walLog;

    ConcurrentLinkedQueue<FlushTask> flushTasks;

    public final static int keySize = 1024 * 1024 * 2;

    public final static int serializerSize = 8 * keySize;

    public final static int maxImmMemoryTableSize = 10;

    public KeyValueEntry getValue(KeyValueEntry keyValueEntry) {
        KeyValueEntry keyValue = null;
        keyValue = Table.get(keyValueEntry);
        if (keyValue != null) {
            return keyValue;
        }
        int i = immTable.size() - 1;
        for (; i >= 0; i--) {
            keyValue = immTable.get(i).get(keyValueEntry);
            if (keyValue != null) {
                return keyValue;
            }
        }
        return null;
    }

    public void putValue(KeyValueEntry keyValueEntry) {

        getUseMemTable().put(keyValueEntry);

        if (immTable.size() > maxImmMemoryTableSize) {

            flush();

        }

    }

    private MemTable getUseMemTable() {
        synchronized (this) {
            MemTable memTable = Table;
            if (memTable.getKeySize() > keySize || memTable.getSerializerSize() > serializerSize) {
                immTable.add(memTable);
                Table = new MemTable(new SkipListRep(TransactionConfig.type == TransactionConfig.TransactionType.readCommit), columnFamilyHandle, walLog);
                columnFamilyHandle.getDb().loadDB();
            }

        }
        return Table;
    }



    private void flush() {
        List<FlushTask> memTableList=new LinkedList<>();
        synchronized (this) {
            while (immTable.size() > 0) {
                if (immTable.get(0).needFlush) {
                    immTable.remove(0);
                } else {
                    break;
                }
            }
            columnFamilyHandle.getDb().loadDB();

            for (MemTable memTable : immTable) {
                if (memTable.getMaxCommit().compareTo(columnFamilyHandle.getDb().getMinPrepareId()) < 0) {
                    memTableList.add(new FlushTask(columnFamilyHandle, memTable));
                }else {
                    break;
                }
            }
        }

        flushTasks.addAll(memTableList);

    }

    public MemTableList(WALLog walLog, ColumnFamilyHandle columnFamilyHandle, List<String> WalFiles,ConcurrentLinkedQueue<FlushTask> flushTasks) {
        this.columnFamilyHandle=columnFamilyHandle;
        this.walLog=walLog;
        immTable = new LinkedList<>();
        for (int i=0;i<WalFiles.size();i++) {
            String fileName=WalFiles.get(i);
            if(walLog.isFlush(fileName)){
                continue;
            }
            MemTable memTable=new MemTable(fileName,walLog.readAll(fileName),false,walLog);
            if(memTable.getKeySize() > keySize || memTable.getSerializerSize() > serializerSize){
                immTable.add(memTable);
            }else {
                Table=memTable;
            }
        }

        if(Table==null){
            Table =new MemTable(new SkipListRep(TransactionConfig.type == TransactionConfig.TransactionType.readCommit), columnFamilyHandle, walLog);
        }
        this.flushTasks=flushTasks;
    }
}
