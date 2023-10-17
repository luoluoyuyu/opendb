package net.openio.opendb.model.value;



import net.openio.opendb.model.OperationType;


public class IntValue implements Value {

  private Integer value;

  private OperationType type;

  @Override
  public Object getValue() {
    return value;
  }

  @Override
  public void setValue(Object value) {
    this.value = (Integer) value;
  }

  @Override
  public Value copy() {
    return new IntValue(value, type);
  }

  @Override
  public OperationType getType() {
    return type;
  }

  @Override
  public void setType(OperationType type) {
    this.type = type;
  }

  public IntValue(Integer value, OperationType type) {
    this.value = value;
    this.type = type;
  }

  public IntValue(){


  }
}

