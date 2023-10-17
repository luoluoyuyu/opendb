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

import net.openio.opendb.storage.sstable.Block;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;


public class LRUBufferCache<X extends Block> {


  private final long expireTime;

  private final int maxCapacity;

  private final int maxNewCapacity;

  private final int maxOldCapacity;

  private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

  private final ScheduledExecutorService scheduledExecutorService;

  private final long oldBlocksTime;

  private volatile int newListSize = 0;

  private volatile int oldListSize = 0;

  private final Node newListHead = new Node();

  private final Node oldListHead = new Node();


  private class Node {
    String ssTable;

    int size;

    private long readTime;

    X b;

    Node next;

    Node pre;
  }

  public X getData(String ssTable) {
    Node node = getNode(ssTable, newListHead);
    if (node != null) {
      updateList(node, true);
      return node.b;
    }

    node = getNode(ssTable, oldListHead);
    if (node != null) {
      updateList(node, false);
      return node.b;
    }
    return null;
  }

  private Node getNode(String ssTable, Node head) {
    readWriteLock.readLock().lock();
    Node h = newListHead.next;
    while (h != null) {
      if (h.ssTable.equals(ssTable)) {
        break;
      }
      h = h.next;
    }
    readWriteLock.readLock().unlock();
    return h;
  }

  private Node getNode(String ssTable, long id, Node head) {
    readWriteLock.readLock().lock();
    Node h = newListHead.next;
    while (h != null) {
      if (h.ssTable.equals(ssTable) && h.b.getId() == id) {
        break;
      }
      h = h.next;
    }
    readWriteLock.readLock().unlock();
    return h;
  }


  private void updateList(Node node, boolean isNew) {
    readWriteLock.writeLock().lock();
    if (node.pre != null && node.pre.next == node) {
      node.pre.next = node.next;
      if (node.next != null) {
        node.next.pre = node.pre;
      }
      if (isNew) {
        newListSize = newListSize - node.size;
      } else {
        oldListSize = oldListSize - node.size;
      }
    }
    node.pre = null;
    node.next = null;

    long time = System.currentTimeMillis();

    if (time - node.readTime > oldBlocksTime && !isNew) {
      node.next = oldListHead.next;
      oldListHead.next = node;
      node.next.pre = node;
      node.pre = oldListHead;
      oldListSize += node.size;
    } else {
      node.next = newListHead.next;
      newListHead.next = node;
      node.next.pre = node;
      node.pre = newListHead;
      newListSize += node.size;
    }
    node.readTime = time;

    if (newListSize > maxNewCapacity) {
      clearOldList();
    }

    if (oldListSize > maxOldCapacity) {
      clearOldList();
    }
    readWriteLock.writeLock().lock();

  }


  public X getData(String ssTable, long id) {

    Node node = getNode(ssTable, id, newListHead);
    if (node != null) {
      updateList(node, true);
      return node.b;
    }

    node = getNode(ssTable, id, oldListHead);
    if (node != null) {
      updateList(node, false);
      return node.b;
    }
    return null;
  }

  public X getDataNotUpdate(String ssTable) {
    Node node = getNode(ssTable, newListHead);
    if (node != null) {
      return node.b;
    }
    node = getNode(ssTable, oldListHead);
    if (node != null) {
      return node.b;
    }
    return null;
  }


  public X getDataNotUpdate(String ssTable, long id) {
    Node node = getNode(ssTable, id, newListHead);
    if (node != null) {
      return node.b;
    }
    node = getNode(ssTable, id, oldListHead);
    if (node != null) {
      return node.b;
    }
    return null;
  }

  public void add(X b, int size, String ssTable) {
    Node node = new Node();
    node.b = b;
    node.size = size;
    node.ssTable = ssTable;

    readWriteLock.writeLock().lock();
    node.next = oldListHead.next;
    node.pre = oldListHead;
    oldListHead.next = node;
    oldListSize = oldListSize + node.size;
    if (oldListSize > (maxOldCapacity)) {
      clearOldList();
    }
    readWriteLock.writeLock().unlock();

  }

  private void clearNewList() {
    int size = 0;
    Node h = newListHead.next;
    while (h != null) {
      if (h.size + size > maxNewCapacity) {
        h.pre.next = null;
        int o = newListSize - size;
        Node tail = getTail(h);

        oldListHead.next.pre = tail;
        tail.next = oldListHead.next;

        oldListHead.next = h;
        h.pre = oldListHead;

        oldListSize = oldListSize + o;
        newListSize = size;
        return;
      }
      h = h.next;
    }

  }

  public int getCurrentSize() {

    return oldListSize + newListSize;

  }

  public Node getTail(Node node) {
    while (node.next != null) {
      node = node.next;
    }
    return node;
  }

  public void clearOldList() {
    int size = 0;
    Node h = oldListHead.next;
    while (h != null) {
      if (h.size + size > maxOldCapacity) {
        h.pre.next = null;
        oldListSize = size;
        return;
      }
      h = h.next;
    }
  }

  public LRUBufferCache(long expireTime, long scanTime, long oldBlocksTime, int maxCapacity) {
    this.expireTime = expireTime;
    this.maxCapacity = maxCapacity;
    this.maxOldCapacity = maxCapacity / 5;
    maxNewCapacity = maxCapacity - this.maxOldCapacity;
    this.oldBlocksTime = oldBlocksTime;
    if (scanTime > 0) {
      scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
      scheduledExecutorService.scheduleAtFixedRate(new ScanListTask(), scanTime, scanTime, TimeUnit.SECONDS);
    } else {
      scheduledExecutorService = null;
    }
  }


  private class ScanListTask implements Runnable {

    @Override
    public void run() {
      readWriteLock.writeLock().lock();
      Node h = newListHead.next;
      long time = System.currentTimeMillis();
      int size = 0;
      while (h != null) {
        if (h.readTime + expireTime < time) {
          h.pre.next = null;
          h.pre = null;
          break;
        }
        size += h.size;
        h = h.next;
      }
      newListSize = size;
      size = 0;
      h = oldListHead.next;
      while (h != null) {
        if (h.readTime + expireTime < time) {
          h.pre.next = null;
          h.pre = null;
          break;
        }
        size += h.size;
        h = h.next;
      }
      oldListSize = size;
      readWriteLock.writeLock().unlock();
    }
  }

  public void close(){
    scheduledExecutorService.shutdown();
  }
}