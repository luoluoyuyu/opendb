package net.openio.opendb.model;

public enum OperationType {

  delete(1),

  update(2),

  insert(3),

  ;
  public static OperationType get(int tag) {
    if (tag == 1) {
      return OperationType.delete;
    }
    if (tag == 2) {
      return OperationType.update;
    }
    if (tag == 3) {
      return OperationType.insert;
    }
    return null;
  }
  int num;

  OperationType(int num) {
    this.num = num;
  }

  public int getNum() {
    return this.num;
  }
}
