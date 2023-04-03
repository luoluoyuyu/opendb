package net.openio.jrocksDb.transaction;

import net.openio.jrocksDb.db.*;

public interface Transaction {
    Status SetSnapshot();


    Status ClearSnapshot();

//    Status Prepare();

    Status Commit();

    Status Rollback();

    Status<Value> Get(Key key, ColumnFamilyHandle columnFamilyHandle);

    Status<Value> GetForUpdate(Key key,Value value,ColumnFamilyHandle columnFamilyHandle);

    Status<Value> Put(Key key, Value value,ColumnFamilyHandle columnFamilyHandle);

    Status<Value> delete(Key key,ColumnFamilyHandle columnFamilyHandle);

}
