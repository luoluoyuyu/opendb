package net.openio.jrocksDb.log;

import lombok.Data;
import net.openio.jrocksDb.config.TransactionConfig;
import net.openio.jrocksDb.db.ColumnFamilyHandle;
import net.openio.jrocksDb.db.ColumnFamilyId;
import net.openio.jrocksDb.mem.KeyValueEntry;
import net.openio.jrocksDb.mem.MemTableRep;
import net.openio.jrocksDb.mem.SkipListRep;

import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ThreadPoolExecutor;
@Data
public class WALLog {

    ConcurrentLinkedQueue<WalTask> queue;

    WalStorage walStorage;

    public boolean write(KeyValueEntry key,String walFile){

        return queue.add(new WalTask(walFile,key,false,walStorage));

    }

    public boolean flush(){
        return queue.add(new WalTask(null,null,true,walStorage));
    }


    public String createWalFile( ColumnFamilyHandle columnFamilyHandle){
        String fileName=null;
        walStorage.createFile(fileName=columnFamilyHandle.getFileName(),columnFamilyHandle.getColumnFamilyId());
        return fileName;
    }


    public MemTableRep readAll(String fileName){
        SkipListRep skipListRep=new SkipListRep(TransactionConfig.type==TransactionConfig.TransactionType.readCommit);
        for(KeyValueEntry keyValueEntry:walStorage.getAllMemTable(fileName)){
            skipListRep.addKeyValue(keyValueEntry);
        }
        return skipListRep;
    }

    public boolean isFlush(String fileName){
        return walStorage.isFlush(fileName);
    }

    public boolean createFile(String fileName, ColumnFamilyId columnFamilyId){
        return walStorage.createFile(fileName,columnFamilyId);
    }


    public WALLog(ConcurrentLinkedQueue<WalTask> queue,WalStorage walStorage){
        this.queue=queue;
        this.walStorage=walStorage;
    }

}
