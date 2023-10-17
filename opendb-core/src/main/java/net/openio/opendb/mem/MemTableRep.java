package net.openio.opendb.mem;

import net.openio.opendb.db.KeyValueEntry;
import net.openio.opendb.model.key.Key;
import net.openio.opendb.model.value.Value;

import java.util.Comparator;
import java.util.List;

public interface MemTableRep extends Iterable<KeyValueEntry> {

  void addKeyValue(KeyValueEntry keyValue);

  void addKeyValue(List<KeyValueEntry> keyValue);

  Value getValue(Key key, Comparator<Key> comparator);


  int getValueNum();

}
