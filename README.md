# OpenDB

Based on LSM tree-structured database with up to 100,000 data writes per second.

## Create DB

```

    openDB = OpenDBImp.open(new Options(),"src/test/resources/data/");
    ColumnFamilyDescriptor columnFamilyDescriptor= new ColumnFamilyDescriptor();
    columnFamilyDescriptor.setName("luoluoyuyu");
    columnFamilyDescriptor.setKeyType(KeyType.intKey);
    columnFamilyDescriptor.setValueType(ValueType.intValue);
    columnFamilyDescriptor.setBlockSize(1<<12);
    openDB.createColumnFamily(columnFamilyDescriptor);
    Key key = new IntKey(1);
    Value value = new IntValue(1);

    openDB.put(key, value, columnFamilyHandle);
    columnFamilyHandle=openDB.getColumnFamilyHandle("luoluoyuyu").date;
```