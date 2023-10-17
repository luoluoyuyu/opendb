package net.openio.opendb.model.value;


import net.openio.opendb.model.OperationType;

import java.util.Arrays;


public class BytesValue implements Value {

  private byte[] value;

  private OperationType type;

  @Override
  public Object getValue() {
    return value;
  }

  @Override
  public void setValue(Object value) {
    this.value = (byte[]) value;
  }

  @Override
  public Value copy() {
    return new BytesValue(Arrays.copyOf(value, value.length), type);
  }

  @Override
  public OperationType getType() {
    return type;
  }

  @Override
  public void setType(OperationType type) {
    this.type = type;
  }

  public BytesValue(byte[] value, OperationType type) {
    this.value = value;
    this.type = type;
  }

  public BytesValue(){


  }
}

