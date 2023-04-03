package net.openio.jrocksDb.transaction.lock;


import net.openio.jrocksDb.db.Key;

import java.util.HashMap;


public class LockMap {

    HashMap<Key, LockMapStripes> LockMap;


    public LockMap(){
        LockMap=new HashMap<>();
    }

}
