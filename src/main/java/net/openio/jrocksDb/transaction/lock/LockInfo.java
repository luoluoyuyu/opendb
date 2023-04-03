package net.openio.jrocksDb.transaction.lock;

import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class LockInfo {

    long transactionId;

    boolean exclusive = false;

    long expirationTime;

}
