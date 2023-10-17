package net.openio.opendb.model.value;


import net.openio.opendb.model.OperationType;

public interface Value {

  Object getValue();

  void setValue(Object value);

  Value copy();

  OperationType getType();

  void setType(OperationType type);

}
