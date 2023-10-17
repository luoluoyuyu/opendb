package net.openio.opendb.model.value;

import net.openio.opendb.model.OperationType;


public class StringValue implements Value {

  private String value;

  private OperationType type;

  public StringValue(String value, OperationType type) {
    this.value = value;
    this.type = type;
  }

  @Override
  public Object getValue() {
    return value;
  }

  @Override
  public void setValue(Object value) {
    this.value = (String) value;
  }

  @Override
  public Value copy() {
    return new StringValue(value, type);
  }

  @Override
  public OperationType getType() {
    return type;
  }

  @Override
  public void setType(OperationType type) {
    this.type = type;
  }

  public StringValue() {


  }


}