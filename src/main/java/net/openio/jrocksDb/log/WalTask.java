package net.openio.jrocksDb.log;

import lombok.AllArgsConstructor;
import net.openio.jrocksDb.db.ColumnFamilyId;
import net.openio.jrocksDb.mem.KeyValueEntry;

import java.util.List;
@AllArgsConstructor
public class WalTask {

    String fileName;

    KeyValueEntry keyValueEntry;

    boolean isFlush;

    WalStorage walStorage;

}
