package net.openio.jrocksDb.db;

import lombok.AllArgsConstructor;
import net.openio.jrocksDb.mem.KeyValueEntry;

import javax.xml.crypto.dsig.keyinfo.KeyValue;

@AllArgsConstructor
public class ColumnFamilyKeyValue {

    String name;

    KeyValueEntry keyValueEntry;
}
