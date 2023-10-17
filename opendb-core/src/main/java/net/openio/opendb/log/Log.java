package net.openio.opendb.log;

import net.openio.opendb.model.SequenceNumber;

public interface Log {

  int compare(SequenceNumber sequenceNumber);

  long getTransactionId();
}
