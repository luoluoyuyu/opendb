package net.openio.jrocksDb.transaction.lock;

import net.openio.jrocksDb.db.ColumnFamilyId;
import net.openio.jrocksDb.db.Key;

import java.util.List;

public class TrackedTrxInfo {

    List<CFKey> keys;

    CFKey waitingKey;

    boolean exclusive;


}
