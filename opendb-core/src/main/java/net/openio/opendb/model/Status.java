package net.openio.opendb.model;


import net.openio.opendb.model.value.Value;

public class Status<K> {
  public boolean success;

  public String message;

  public K date;

  public boolean hasValue;

  public Status(K value) {
    success = true;
    if (value == null) {
      hasValue = false;

    } else {
      hasValue = true;
      date = value;
    }
  }

  public Status() {

  }

  public static Status fail() {
    Status status = new Status();
    status.success = false;
    status.hasValue = false;
    return status;
  }

}
