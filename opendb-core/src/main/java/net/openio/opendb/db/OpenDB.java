package net.openio.opendb.db;

import net.openio.opendb.model.ColumnFamilyDescriptor;
import net.openio.opendb.model.Status;
import net.openio.opendb.model.key.Key;
import net.openio.opendb.model.value.Value;
import net.openio.opendb.transaction.Transaction;

import java.util.List;

public interface OpenDB {

  Status<Value> get(Key key, ColumnFamilyHandle columnFamilyHandle);

  Status<Value> put(Key key, Value value, ColumnFamilyHandle columnFamilyHandle);

  Status<Value> update(Key key, Value value, ColumnFamilyHandle columnFamilyHandle);

  Status<Value> delete(Key key, ColumnFamilyHandle columnFamilyHandle);

  Status<ColumnFamilyHandle> getColumnFamilyHandle(String name);

  Status deleteColumnFamilyHandle(ColumnFamilyHandle columnFamilyHandle);

  Status<ColumnFamilyDescriptor> getColumnFamily(String name);

  Status<ColumnFamilyDescriptor> createColumnFamily(ColumnFamilyDescriptor columnFamilyDescriptor);

  Status<List<ColumnFamilyDescriptor>> getAllColumnFamily();

  Transaction createTransaction(Snapshot snapshot);

  Snapshot getSnapshot();

  void close();

}
