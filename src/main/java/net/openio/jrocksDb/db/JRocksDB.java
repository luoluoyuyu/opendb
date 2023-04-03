package net.openio.jrocksDb.db;

import net.openio.jrocksDb.transaction.Transaction;

import javax.xml.crypto.dsig.keyinfo.KeyValue;
import java.util.List;

public interface JRocksDB {

    Status<Value> get(Key key,String columnFamilyName);

    Status<Value> put(Key key,Value value ,String columnFamilyName);

    Status<Value> getForUpdate(Key key,Value value ,String columnFamilyName);

    Status<Value> delete(Key key ,String columnFamilyName);

    Status<ColumnFamily> getColumnFamily(String Name);

    Status<ColumnFamily> createColumnFamily(String name, Key.KeyType keyType, Value.ValueType valueType);

    Status<List<ColumnFamily>> getAllColumnFamily();

    Status<Transaction> createTransaction();

}
