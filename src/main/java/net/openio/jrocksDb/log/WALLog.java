package net.openio.jrocksDb.log;

import lombok.Data;
import net.openio.jrocksDb.config.Config;
import net.openio.jrocksDb.db.ColumnFamilyHandle;
import net.openio.jrocksDb.db.ColumnFamilyId;
import net.openio.jrocksDb.mem.*;

import java.util.LinkedList;
import java.util.concurrent.ConcurrentLinkedQueue;

@Data
public class WALLog {

    ConcurrentLinkedQueue<WalTask> queue;

    WalStorage walStorage;

    public boolean write(KeyValueEntry key,String walFile){

        return queue.add(new WalTask(walFile,key,walStorage));

    }



    public void flush(String fileName){
        walStorage.write(fileName,new LinkedList<>(),true);
    }


    public String createWalFile( ColumnFamilyHandle columnFamilyHandle){
        String fileName=null;
        walStorage.createFile(fileName=columnFamilyHandle.getFileName(),columnFamilyHandle.getColumnFamilyId());
        return fileName;
    }


    public MemTable readAll(String fileName){
        BloomFilter bloomFilter=new BloomFilter();
        SkipListRep skipListRep=new SkipListRep(Config.type== Config.TransactionType.readCommit);
        for(KeyValueEntry keyValueEntry:walStorage.getAllMemTable(fileName)){
            skipListRep.addKeyValue(keyValueEntry);
            bloomFilter.add(keyValueEntry.getKey());
        }

        return new MemTable(fileName,skipListRep,false,this,bloomFilter);
    }



    public boolean isFlush(String fileName){
        return walStorage.isFlush(fileName);
    }



    public WALLog(ConcurrentLinkedQueue<WalTask> queue,WalStorage walStorage){
        this.queue=queue;
        this.walStorage=walStorage;
    }

}
