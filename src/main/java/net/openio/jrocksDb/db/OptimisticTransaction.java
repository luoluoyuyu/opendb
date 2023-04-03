package net.openio.jrocksDb.db;

import lombok.AllArgsConstructor;
import net.openio.jrocksDb.mem.KeyValueEntry;
import net.openio.jrocksDb.mem.MemTableList;
import net.openio.jrocksDb.transaction.SequenceNumber;
import net.openio.jrocksDb.transaction.Snapshot;
import net.openio.jrocksDb.transaction.Transaction;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public  class OptimisticTransaction implements Transaction {

    long transactionId;

    OptimisticTransactionDB optimisticTransactionDB;

    Snapshot snapshot;

    List<KeyValueEntry> writeBatch=new ArrayList<>();


    @Override
    public Status SetSnapshot() {
         snapshot=new Snapshot();
         return Status.success();
    }


    @Override
    public Status ClearSnapshot() {
        return null;
    }

    @Override
    public Status Commit() {
        return null;
    }

    @Override
    public Status Rollback() {
        return null;
    }

    @Override
    public Status<Value> Get(Key key, ColumnFamilyHandle columnFamilyHandle) {
        return null;
    }

    @Override
    public Status<Value> GetForUpdate(Key key, Value value, ColumnFamilyHandle columnFamilyHandle) {
        return null;
    }

    @Override
    public Status<Value> Put(Key key, Value value, ColumnFamilyHandle columnFamilyHandle) {
        return null;
    }

    @Override
    public Status<Value> delete(Key key, ColumnFamilyHandle columnFamilyHandle) {
        return null;
    }

    public OptimisticTransaction(OptimisticTransactionDB db,long tId){
        this.transactionId=tId;
        this.optimisticTransactionDB=db;
    }

    private SequenceNumber tryLock(long tId,Key key,ColumnFamilyId columnFamilyId){
        return null;
    }

    private void unLock(long transactionId,Key key,ColumnFamilyId columnFamilyId){

    }
}
