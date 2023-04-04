package net.openio.jrocksDb.transaction.lock;

import net.openio.jrocksDb.db.ColumnFamilyHandle;
import net.openio.jrocksDb.db.ColumnFamilyId;
import net.openio.jrocksDb.db.Key;
import net.openio.jrocksDb.db.Status;
import net.openio.jrocksDb.transaction.SequenceNumber;

import java.util.concurrent.ConcurrentHashMap;

public interface LockManager {

    // Whether supports locking a specific key.
    boolean IsPointLockSupported();


    SequenceNumber TryLock(long Lid, ColumnFamilyId cId, Key key,boolean exclusive,boolean readOnly
            , SequenceNumber sequenceNumber,boolean isTracker);


     void UnLock(long Lid, ColumnFamilyId cId, Key key,boolean exclusive,boolean readOnly,boolean isTracker) ;


    LockTracker.PointLockStatus GetPointLockStatus(ColumnFamilyId cId, Key key);


}
