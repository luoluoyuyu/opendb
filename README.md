# OpenDB

Based on LSM tree-structured database with up to 100,000 data writes per second.

## introduce
1. Developed using the LSM architecture
2. Serialization and deserialization of data in the form of ProtoBuf
3. Use direct memory to reduce one copy of the IO process
4. Memory structure support Bloom filters, jump tables 
5. Multi-threaded read/write support
6. Use of buffer pools to increase read efficiency
7. Supports Size-tire compression and Level compression
8. Support repeatable reads, read committed isolation level.
9. Optimize SSTable, Key and Value separation.
10. Write metadata twice to prevent complete data loss
11. Support for version snapshots
12. Optimistic locking and pessimistic locking transactions. 

## example

```

    openDB = OpenDBImp.open(new Options(),"src/test/resources/data/");
    ColumnFamilyDescriptor columnFamilyDescriptor= new ColumnFamilyDescriptor();
    columnFamilyDescriptor.setName("luoluoyuyu");
    columnFamilyDescriptor.setKeyType(KeyType.intKey);
    columnFamilyDescriptor.setValueType(ValueType.intValue);
    columnFamilyDescriptor.setBlockSize(1<<12);
    openDB.createColumnFamily(columnFamilyDescriptor);
    columnFamilyHandle=openDB.getColumnFamilyHandle("luoluoyuyu").date;

    Key key = new IntKey(1);
    Value value = new IntValue(1);

    openDB.put(key, value, columnFamilyHandle);

```

## benchmark
On an average PC, writes can be up to 130,000 per second and reads up to 510,000 per second.
```
Benchmark                     Mode  Cnt       Score   Error  Units
OpenDBBenchmark.addKeyValue  thrpt       134061.544          ops/s
OpenDBBenchmark.getKeyValue  thrpt       512352.366          ops/s

```
