package net.openio.opendb.model.value;


import net.openio.opendb.model.OperationType;


public class LongValue implements Value {

  Long value;

  private OperationType type;

  @Override
  public Object getValue() {
    return value;
  }

  @Override
  public void setValue(Object value) {
    this.value = (Long) value;
  }

  @Override
  public Value copy() {
    return new LongValue(value, type);
  }

  @Override
  public OperationType getType() {
    return type;
  }


  @Override
  public void setType(OperationType type) {
    this.type = type;
  }

  public LongValue(Long value, OperationType type) {
    this.value = value;
    this.type = type;
  }

  public LongValue() {


  }

}
