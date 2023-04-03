package net.openio.jrocksDb.compression;

import lombok.AllArgsConstructor;
import net.openio.jrocksDb.db.ColumnFamilyHandle;
import net.openio.jrocksDb.db.FileList;

@AllArgsConstructor
public class CompressionTask {
    FileList fileList;

    ColumnFamilyHandle columnFamilyHandle;

}
