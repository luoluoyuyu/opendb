package net.openio.jrocksDb.transaction;

import net.openio.jrocksDb.db.*;

public interface Transaction {
    Status SetSnapshot();


    Status ClearSnapshot();

//    Status Prepare();

    Status Commit();

    Status Rollback();

    Status<Value> Get(Key key, String ColumnFamilyName);

    Status<Value> GetForUpdate(Key key,Value value,String ColumnFamilyName);

    Status<Value> Put(Key key, Value value,String ColumnFamilyName);

    Status<Value> delete(Key key,String ColumnFamilyName);

}
