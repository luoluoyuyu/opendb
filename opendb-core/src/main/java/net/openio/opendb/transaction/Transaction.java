package net.openio.opendb.transaction;

import net.openio.opendb.db.ColumnFamilyHandle;
import net.openio.opendb.model.ColumnFamilyDescriptor;
import net.openio.opendb.model.Status;
import net.openio.opendb.model.key.Key;
import net.openio.opendb.model.value.Value;

import java.util.List;

public interface Transaction {

  Status prepare();

  Status commit();

  Status rollback();

  Status<Value> get(Key key, ColumnFamilyHandle columnFamilyHandle);

  Status<Value> update(Key key, Value value, ColumnFamilyHandle columnFamilyHandle);

  Status<Value> put(Key key, Value value, ColumnFamilyHandle columnFamilyHandle);

  Status<Value> delete(Key key, ColumnFamilyHandle columnFamilyHandle);

  Status<ColumnFamilyHandle> getColumnFamilyHandle(String name);

  Status<List<ColumnFamilyDescriptor>> getAllColumnFamily();

}
