package net.openio.opendb.model.key;


import net.openio.opendb.model.SequenceNumber;

public interface Key extends Comparable<Key> {

  Object getKey();

  SequenceNumber getSequenceNumber();

  void setSequenceNumber(SequenceNumber sequenceNumber);

  void setKey(Object o);

  void setKey(Object value, SequenceNumber sequenceNumber);

  Key copy();


}
