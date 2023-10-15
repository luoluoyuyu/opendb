/**
 * Licensed to the OpenIO.Net under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.openio.opendb.mem;


import net.openio.opendb.db.KeyValueEntry;
import net.openio.opendb.model.key.Key;
import net.openio.opendb.model.value.Value;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;


public class SkipListRep<K extends Key, V extends Value> implements MemTableRep {

  private static final int MAX_LEVEL = 32;

  private volatile Node[] head;

  private static final double P = 0.75;


  static final AtomicReferenceFieldUpdater<Node, Node[]> NEXT =
    AtomicReferenceFieldUpdater.newUpdater(Node.class, Node[].class, "next");

  AtomicInteger num = new AtomicInteger(0);


  private int randomLevel() {

    int level = 1;
    for (int i = 0; i < MAX_LEVEL; i++) {
      if (ThreadLocalRandom.current().nextBoolean()) {
        level++;
      }
    }
    return level;
  }

  @Override
  public void addKeyValue(KeyValueEntry keyValue) {
    Node[] newNode = null;
    int addNodeLevel = 0;
    Node[][] map = new Node[MAX_LEVEL][];
    to:
    for (; ; ) {
      Node[] le = head;
      add(map, head);
      for (int i = MAX_LEVEL - 1; i >= 0; ) {
        if (le[i].next == null) {
          i--;
          continue;
        }
        Key key = ((NodeImp) le[i].next[0]).key;
        int d = com(key, keyValue.getKey());
        if (d > 0) {
          i--;
          continue;
        }
        add(map, le);
        le = le[i].next;
      }
      add(map, le);
      if (newNode == null) {
        newNode = newNodeArray(randomLevel(), keyValue);
      }
      Node[] nodes = newNode;
      int l = nodes.length;
      for (int a = addNodeLevel; a < l; a++, addNodeLevel++) {
        Node[] node = map[a];
        Node[] fNext;
        fNext = nodes[a].next = node[a].next;
        if (fNext != null && com(((NodeImp) nodes[0]).key, ((NodeImp) fNext[0]).key) >= 0) {
          newNode = null;
          l = 0;
          continue to;
        }
        if (!NEXT.compareAndSet(node[a], fNext, nodes)) {
          continue to;
        }
      }
      if (addNodeLevel == l) {
        break;
      }
    }
    num.incrementAndGet();
  }

  @Override
  public void addKeyValue(List<KeyValueEntry> entries) {
    for (KeyValueEntry entry : entries) {
      addKeyValue(entry);
    }
  }


  private void add(Node[][] nodes, Node[] node) {
    for (int i = node.length - 1; i >= 0; i--) {
      nodes[i] = node;
    }
  }

  @Override
  public Value getValue(Key key) {
    Node[] le = head;
    for (int i = MAX_LEVEL - 1; i >= 0; ) {
      if (le[i].next == null) {
        i--;
        continue;
      }
      int d = ((NodeImp) le[i].next[0]).key.compareTo(key);
      if (d > 0) {
        i--;
        continue;
      }
      if (d == 0) {
        return ((NodeImp) le[i].next[0]).get(key).getValue();
      }
      le = le[i].next;
    }
    return null;
  }

  @Override
  public int getValueNum() {
    return this.num.intValue();
  }


  static class Node {
    volatile Node[] next;
  }


  static final class NodeImp extends Node {
    private Key key;
    private KeyValueEntry values;

    private KeyValueEntry get(Key kv) {
      return values;
    }

    private NodeImp(final KeyValueEntry kv) {
      values = kv;
      this.key = kv.getKey();
    }

    private NodeImp() {
    }

  }

  private Node[] newNodeArray(final int level, final KeyValueEntry kv) {
    Node[] nodes = new Node[level];
    for (int i = 1; i < level; i++) {
      nodes[i] = new Node();
    }
    nodes[0] = new NodeImp(kv);
    return nodes;
  }


  public SkipListRep() {
    Node[] nodes = new Node[MAX_LEVEL];
    for (int i = 1; i < MAX_LEVEL; i++) {
      nodes[i] = new Node();
    }

    nodes[0] = new NodeImp();
    head = nodes;
  }


  @Override
  public Iterator<KeyValueEntry> iterator() {
    return new SkipListIterator();
  }

  class SkipListIterator implements Iterator<KeyValueEntry> {
    private NodeImp currentNode;

    public SkipListIterator() {
      currentNode = head[0].next != null ? (NodeImp) head[0].next[0] : null;
    }

    @Override
    public boolean hasNext() {
      return currentNode != null;
    }

    @Override
    public KeyValueEntry next() {
      if (!hasNext()) {
        throw new NoSuchElementException();
      }

      KeyValueEntry entry = currentNode.values;
      currentNode = (currentNode.next != null ? (NodeImp) currentNode.next[0] : null);
      return entry;
    }
  }

  private int com(Key key, Key keyValue) {
    int d = key.compareTo(keyValue);
    if (d == 0) {
      d = -key.getSequenceNumber().compareTo(keyValue.getSequenceNumber());
    }
    return d;
  }
}