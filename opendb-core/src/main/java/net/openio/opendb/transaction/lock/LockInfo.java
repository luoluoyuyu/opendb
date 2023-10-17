package net.openio.opendb.transaction.lock;


public class LockInfo {

  long transactionId;

  boolean exclusive = false;

  long expirationTime;

  public LockInfo(long transactionId, boolean exclusive, long expirationTime) {
    this.transactionId = transactionId;
    this.exclusive = exclusive;
    this.expirationTime = expirationTime;
  }
}
