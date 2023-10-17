package net.openio.opendb.transaction.lock;

import net.openio.opendb.model.SequenceNumber;
import net.openio.opendb.model.key.Key;

interface LockManager {

  // Whether supports locking a specific key.
  boolean isPointLockSupported();


  void tryLock(Long cId, Key key, boolean exclusive);

  void tracker(Long cId, Key key, SequenceNumber sequenceNumber);

  void unLock(Long cId, Key key);


  SequenceNumber tryLock(long tId, Long cId, Key key, boolean exclusive, boolean readOnly);

  void unLock(long tid, Long cId, Key key);


  void tracker(long tid, Long cId, Key key, SequenceNumber sequenceNumber);


  boolean validate(Long cId, Key key, SequenceNumber sequenceNumber);


}
