package net.openio.opendb.model.value;


import net.openio.opendb.model.OperationType;


public class DoubleValue implements Value {

  private Double value;

  private OperationType type;

  public DoubleValue(Double value, OperationType type) {
    this.value = value;
    this.type = type;
  }

  @Override
  public Object getValue() {
    return value;
  }

  @Override
  public void setValue(Object value) {
    this.value = (Double) value;
  }

  @Override
  public Value copy() {
    return new DoubleValue(value, type);
  }

  @Override
  public OperationType getType() {
    return type;
  }

  public void setValue(Double value) {
    this.value = value;
  }

  @Override
  public void setType(OperationType type) {
    this.type = type;
  }

  public DoubleValue(){

  }
}
