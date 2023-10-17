package net.openio.opendb.compaction;

import net.openio.opendb.compression.CompactionIterator;
import net.openio.opendb.db.KeyValueEntry;
import net.openio.opendb.mem.BloomFilter;
import net.openio.opendb.mem.BufferCache;
import net.openio.opendb.mem.KeyValueGenerator;
import net.openio.opendb.mem.LRUBufferCache;
import net.openio.opendb.mem.MemTable;
import net.openio.opendb.mem.SkipListRep;
import net.openio.opendb.model.SequenceNumber;
import net.openio.opendb.model.key.Key;
import net.openio.opendb.model.key.KeyType;
import net.openio.opendb.model.value.ValueType;
import net.openio.opendb.storage.sstable.SSTable;
import net.openio.opendb.storage.sstable.SSTableStorage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class CompactionTest {
//  private final static MemTable memTable = new MemTable(new SkipListRep<>(), new BloomFilter());
//  private final static SSTableStorage storage = new SSTableStorage(1 << 15, 1 << 17, "src/test/resources/");
//  private final static int ssTableCount = 10;
//  private static List<MemTable> mem = new LinkedList<>();
//  private final static List<SSTable> list = new LinkedList<>();
//  private final static List<KeyValueEntry> k = new LinkedList<>();
//
//  private final static int count = 1 << 10;
//
//  static {
//    for (int j = 0; j < ssTableCount; j++) {
//      mem.add(new MemTable(new SkipListRep<>(), new BloomFilter()));
//    }
//    for (int i = 0; i < count; i++) {
//      for (int j = 0; j < ssTableCount; j++) {
//        KeyValueEntry kv = null;
//        mem.get(j).put(kv = KeyValueGenerator.generateRandomIntKeyValueEntry());
//        k.add(kv);
//      }
//    }
//    for (int j = 0; j < ssTableCount; j++) {
//      try {
//        list.add(storage.flush(mem.get(j), KeyType.intKey, ValueType.intValue));
//      } catch (IOException e) {
//        e.printStackTrace();
//      }
//    }
//    mem = null;
//
//    try {
//      Thread.sleep(20000);
//    } catch (InterruptedException e) {
//      e.printStackTrace();
//    }
//
//
//  }
//
//  @Test
//  public void test() throws IOException {
//    k.sort((a, b) -> {
//        Key k = ((KeyValueEntry) a).getKey();
//        Key key = ((KeyValueEntry) b).getKey();
//        int d = k.compareTo(key);
//        if (d == 0) {
//          return k.getSequenceNumber().compareTo(key.getSequenceNumber());
//        }
//        return d;
//      }
//    );
//
//    CompactionIterator compactionIterator = new CompactionIterator(list, storage, new BufferCache(
//      new LRUBufferCache<>(1000, 60, 60000, 1 << 19),
//      new LRUBufferCache<>(1000, 60, 60000, 1 << 19),
//      new LRUBufferCache<>(1000, 60, 60000, 1 << 19)
//    ),
//      new SequenceNumber(0L));
//    List<SSTable> ssTables = storage.compaction(compactionIterator, KeyType.intKey, ValueType.intValue, 1 << 29);
//    CompactionIterator c = new CompactionIterator(ssTables, storage, new BufferCache(
//      new LRUBufferCache<>(1000, 60, 60000, 1 << 19),
//      new LRUBufferCache<>(1000, 60, 60000, 1 << 19),
//      new LRUBufferCache<>(1000, 60, 60000, 1 << 19)
//    ), new SequenceNumber(0L));
//
//    Iterator<KeyValueEntry> iterator = k.iterator();
//    while (iterator.hasNext()) {
//      Assertions.assertEquals(iterator.next().getKey().getKey(), c.next().getKey().getKey());
//    }
//
//    Assertions.assertFalse(c.hasNext());
//
//
//  }
}
