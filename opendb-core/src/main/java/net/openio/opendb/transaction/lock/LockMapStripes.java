package net.openio.opendb.transaction.lock;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class LockMapStripes {

  ReentrantReadWriteLock reentrantReadWriteLock;

  volatile List<LockInfo> infos;

  volatile int num = 0;

  public LockMapStripes() {
    reentrantReadWriteLock = new ReentrantReadWriteLock();
    infos = new LinkedList<>();
  }

  public void add(LockInfo lockInfo) {
    synchronized (this) {
      infos.add(lockInfo);
      num++;
    }
  }

  public boolean delete(long tId) {
    if (num <= 0) {
      throw new RuntimeException("num is zero");
    }
    synchronized (this) {
      for (int i = 0; i < infos.size(); i++) {
        if (infos.get(i).transactionId == tId) {
          infos.remove(i);
          num--;
          return true;
        }
      }
    }

    return false;
  }

  public LockInfo get(long tId) {
    if (num <= 0) {
      throw new RuntimeException("num is zero");
    }
    LockInfo lockInfo = null;
    synchronized (this) {
      for (LockInfo info : infos) {
        if (info.transactionId == tId) {
          return info;
        }
      }
    }

    return null;
  }

}
