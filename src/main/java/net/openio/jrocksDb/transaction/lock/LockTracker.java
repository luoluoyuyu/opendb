package net.openio.jrocksDb.transaction.lock;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.openio.jrocksDb.db.ColumnFamilyId;
import net.openio.jrocksDb.db.Key;
import net.openio.jrocksDb.transaction.SequenceNumber;

public interface LockTracker {

    boolean IsPointLockSupported();

    SequenceNumber Track(PointLockRequest pointLockRequest);


    PointLockStatus GetPointLockStatus(ColumnFamilyId id, Key key);

    public UntrackStatus UnTrack(PointLockRequest pointLockRequest);

    @AllArgsConstructor
    class PointLockRequest {

        ColumnFamilyId columnFamilyId;

        Key key;

        SequenceNumber seq;

        boolean read_only = false;

        boolean exclusive = true;
    }


    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    class PointLockStatus {

        boolean locked =false;

        boolean exclusive =true;

        SequenceNumber seq=new SequenceNumber(0l,0);

    }


    enum  UntrackStatus {

        NOT_TRACKED,

        UNTRACKED,

        REMOVED,
    }




}
