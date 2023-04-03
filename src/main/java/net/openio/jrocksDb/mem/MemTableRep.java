package net.openio.jrocksDb.mem;

import net.openio.jrocksDb.db.Key;
import net.openio.jrocksDb.db.Value;
import net.openio.jrocksDb.transaction.CommitId;

import java.util.List;

public interface MemTableRep {

    void addKeyValue(KeyValueEntry keyValue);


    KeyValueEntry getValue(KeyValueEntry keyValueEntry);


    int getKeySize();

    List<KeyValueEntry> getKeyValue(BloomFilter bloomFilter);

    CommitId getMaxCommit();

    int getSerializerSize();

}
