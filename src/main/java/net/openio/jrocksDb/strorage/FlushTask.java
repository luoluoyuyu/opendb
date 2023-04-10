package net.openio.jrocksDb.strorage;

import lombok.AllArgsConstructor;
import lombok.Data;
import net.openio.jrocksDb.db.ColumnFamilyHandle;
import net.openio.jrocksDb.mem.MemTable;
@AllArgsConstructor
@Data
public class FlushTask {
    ColumnFamilyHandle columnFamilyHandle;

    MemTable memTable;
}
