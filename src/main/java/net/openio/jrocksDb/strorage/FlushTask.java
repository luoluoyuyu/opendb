package net.openio.jrocksDb.strorage;

import lombok.AllArgsConstructor;
import net.openio.jrocksDb.db.ColumnFamilyHandle;
import net.openio.jrocksDb.mem.MemTable;
@AllArgsConstructor
public class FlushTask {
    ColumnFamilyHandle columnFamilyHandle;

    MemTable memTable;
}
