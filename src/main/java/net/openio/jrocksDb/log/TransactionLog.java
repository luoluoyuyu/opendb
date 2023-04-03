package net.openio.jrocksDb.log;

import net.openio.jrocksDb.db.WriteBatchBase;
import net.openio.jrocksDb.mem.KeyValueEntry;
import net.openio.jrocksDb.transaction.SequenceNumber;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;

public class TransactionLog {

    Map<SequenceNumber,List<KeyValueEntry>> log;

    ThreadPoolExecutor threadPoolExecutor;

    public boolean prepare(WriteBatchBase writeBatchBase){
        return false;
    }

    private boolean write(KeyValueEntry key){
        return false;
    }

    List<KeyValueEntry> readAll(SequenceNumber sequenceNumber){

        return null;
    }

    Map<SequenceNumber,List<KeyValueEntry>> readAll(){

        return null;
    }

    Map<SequenceNumber,List<KeyValueEntry>> getCommitDate(){
        return null;
    }



    boolean commit(SequenceNumber sequenceNumber){
        return false;
    }
}
