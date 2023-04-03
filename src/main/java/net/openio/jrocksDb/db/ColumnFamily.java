package net.openio.jrocksDb.db;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class ColumnFamily {

    ColumnFamilyId columnFamilyId;

    String name;

    Key.KeyType keyType;

    Value.ValueType valueType;

}
