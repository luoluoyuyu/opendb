package net.openio.jrocksDb.mem;

import net.openio.jrocksDb.config.Config;
import net.openio.jrocksDb.db.ColumnFamilyHandle;
import net.openio.jrocksDb.log.WALLog;
import net.openio.jrocksDb.strorage.FlushTask;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;

public class MemTableList {


    volatile MemTable Table;

    List<MemTable> immTable;

    ColumnFamilyHandle columnFamilyHandle;

    WALLog walLog;

    ConcurrentLinkedQueue<FlushTask> flushTasks;

    Set<MemTable> memTables;

    public final static int serializerSize =  Config.serializerSize;

    public final static int maxImmMemoryTableNum = 1;

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

        if (immTable.size() > maxImmMemoryTableNum) {

            flush();

        }

    }

    private MemTable getUseMemTable() {
        synchronized (this) {
            MemTable memTable = Table;
            if ( memTable.getSerializerSize() > serializerSize) {
                immTable.add(memTable);
                Table = new MemTable(new SkipListRep(Config.type == Config.TransactionType.readCommit), columnFamilyHandle, walLog);
                columnFamilyHandle.getDb().loadDB();
            }

        }
        return Table;
    }

    public void flush(MemTable memTable){
        memTables.remove(memTable);
        flush();
    }



    private void flush() {

        synchronized (this) {
            for(int i=0;i<immTable.size();i++) {
                if (immTable.get(i).needFlush) {
                    immTable.remove(i);
                }
            }
            columnFamilyHandle.getDb().loadDB();

            for (MemTable memTable : immTable) {
                if (memTable.getMaxCommit().compareTo(columnFamilyHandle.getDb().getMinPrepareId()) < 0&&!memTables.contains(memTable)) {
                    memTables.add(memTable);
                    flushTasks.add(new FlushTask(columnFamilyHandle, memTable));
                }else {
                    break;
                }
            }
        }

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

            MemTable memTable=walLog.readAll(fileName);
            if( memTable.getSerializerSize() > serializerSize){
                immTable.add(memTable);
            }else {
                Table=memTable;
            }
        }

        if(Table==null){
            Table =new MemTable(new SkipListRep(Config.type == Config.TransactionType.readCommit), columnFamilyHandle, walLog);
        }
        this.flushTasks=flushTasks;
        memTables=new HashSet<>();
    }
}
