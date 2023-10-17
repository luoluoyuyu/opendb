package net.openio.opendb.transaction.lock;


import net.openio.opendb.model.key.Key;

import java.util.Objects;


public class CFKey {

  long cfId;

  Key key;

  public CFKey(long cfId, Key key) {
    this.cfId = cfId;
    this.key = key;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CFKey cfKey = (CFKey) o;
    return Objects.equals(cfId, cfKey.cfId) && Objects.equals(key, cfKey.key);
  }

  @Override
  public int hashCode() {
    return Objects.hash(cfId, key);
  }
}
