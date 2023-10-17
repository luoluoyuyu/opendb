package net.openio.opendb.transaction.lock;


import net.openio.opendb.model.key.Key;

import java.util.HashMap;


public class LockMap {

  HashMap<Key, LockMapStripes> lockMap;


  public LockMap() {
    lockMap = new HashMap<>();
  }

}
