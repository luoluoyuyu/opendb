package net.openio.jrocksDb.transaction.lock;


import lombok.AllArgsConstructor;
import lombok.Data;
import net.openio.jrocksDb.transaction.SequenceNumber;

@Data
@AllArgsConstructor
public class TrackedKeyInfo {

    volatile SequenceNumber sequenceNumber;

    volatile int writeNum;

    volatile int readNum;

    volatile boolean exclusive;

}
