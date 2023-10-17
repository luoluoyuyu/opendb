package net.openio.opendb.model.value;


import net.openio.opendb.model.OperationType;


public class FloatValue implements Value {

  private Float value;

  private OperationType type;

  public FloatValue(Float value, OperationType type) {
    this.value = value;
    this.type = type;
  }

  @Override
  public Object getValue() {
    return value;
  }

  @Override
  public void setValue(Object value) {
    this.value = (Float) value;
  }

  @Override
  public Value copy() {
    return new FloatValue(value, type);
  }

  @Override
  public OperationType getType() {
    return type;
  }

  @Override
  public void setType(OperationType type) {
    this.type = type;
  }


  public FloatValue(){


  }
}
