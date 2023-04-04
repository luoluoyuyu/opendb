package net.openio.jrocksDb.mem;

import lombok.Data;
import net.openio.jrocksDb.db.Key;
import net.openio.jrocksDb.transaction.CommitId;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Data
public class SkipListRep implements MemTableRep {

    private static final int MAX_LEVEL = 64; // 最大层数

    private volatile Node[] head; // 头结点

    private static final double P = 0.75; // 每一层加入节点的概率

    private boolean commitRead;

    private volatile CommitId maxCommitId;


     final AtomicInteger serializerSize = new AtomicInteger();

    static final AtomicReferenceFieldUpdater<SkipListRep, CommitId> updateCommit =
            AtomicReferenceFieldUpdater.newUpdater(SkipListRep.class, CommitId.class, "maxCommitId");

    static final AtomicReferenceFieldUpdater<Node, Node[]> updater =
            AtomicReferenceFieldUpdater.newUpdater(Node.class, Node[].class, "next");

    AtomicInteger keyNum = new AtomicInteger(0); //key的数量

    // 生成随机层数
    private int randomLevel() {

        int level = 1;
        for(int i=0;i<MAX_LEVEL;i++) {
            if (ThreadLocalRandom.current().nextBoolean()) {
                level++;
            }
        }
        return level;
    }

    @Override
    public void addKeyValue(KeyValueEntry keyValue) {

        while (true) {

            CommitId commitId=this.maxCommitId;
            if (keyValue.commitId.compareTo(maxCommitId) > 0) {
                if(updateCommit.compareAndSet(this,commitId,keyValue.commitId))
                    break;
            }else {
                break;
            }

        }

        int size=keyValue.getSize();
        serializerSize.getAndAdd(size);
        Node[] newNode = null;
        int addNodeLevel = 0;
        Map<Integer, Node[]> map = new HashMap<>();

        to:
        for (; ; ) {

            Node[] le = head;
            map.clear();
            add(map, head);

            for (int i = MAX_LEVEL - 1; i >= 0; ) {

                if (le[i].next == null) {
                    i--;
                    continue;
                }
                int d = ((NodeImp) le[i].next[0]).key.compareTo(keyValue.key);
                if (d > 0) {
                    i--;
                    continue;
                }
                if (d == 0) {
                    ((NodeImp) le[i].next[0]).add(keyValue);
                    return;
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

                Node[] node = map.get(a);
                Node[] fnext;
                fnext = nodes[a].next = node[a].next;

                if (fnext != null && ((NodeImp) nodes[0]).key.compareTo(((NodeImp) fnext[0]).key) >= 0) {

                    newNode = null;
                    l = 0;
                    continue to;
                }

                if (!updater.compareAndSet(node[a], fnext, nodes)) {

                    continue to;
                }
            }

            if (addNodeLevel == l) {
                break;
            }

            keyNum.incrementAndGet();


        }


    }


    private void add(Map<Integer, Node[]> map, Node[] node) {
        for (int i = node.length - 1; i >= 0; i--) {
            map.put(i, node);
        }
    }

    @Override
    public KeyValueEntry getValue(KeyValueEntry keyValueEntry) {
        Node[] le = head;
        for (int i = MAX_LEVEL - 1; i >= 0; ) {
            if (le[i].next == null) {
                i--;
                continue;
            }
            int d = ((NodeImp) le[i].next[0]).key.compareTo(keyValueEntry.key);
            if (d > 0) {
                i--;
                continue;
            }
            if (d == 0) {
                return ((NodeImp) le[i].next[0]).get(keyValueEntry);
            }
            le = le[i].next;
        }
        return null;
    }

    @Override
    public int getKeySize() {
        return keyNum.get();
    }


    public int getSerializerSize() {
        return serializerSize.get();
    }

    @Override
    public List<KeyValueEntry> getKeyValue(BloomFilter bloomFilter) {
        List<KeyValueEntry> list = new ArrayList<>(keyNum.get());
        Node[] node = head;
        while (head[0].next != null) {
            NodeImp nodeImp = (NodeImp) node[0];
            list.add((nodeImp.getMax()));
            int size = nodeImp.values.size() - 1;
            if (size != 0) bloomFilter.delete(nodeImp.key, size);
            node = node[0].next;
        }

        return list;
    }

    @Override
    public CommitId getMaxCommit() {
        return maxCommitId;
    }


    static class Node {
        volatile Node[] next;

    }

    final class NodeImp extends Node {
        Key key;
        final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
        final List<KeyValueEntry> values = new LinkedList<>();


        private void add(final KeyValueEntry kv) {
            lock.writeLock().lock();
            if (commitRead) {
                values.add(0, kv);
            } else {
                values.add(kv);
            }
            lock.writeLock().unlock();

        }

        private KeyValueEntry get(KeyValueEntry kv) {
            KeyValueEntry kev = null;
            lock.readLock().lock();
            if (commitRead) return values.get(0);
            for (KeyValueEntry keyValueEntry : values) {
                if (keyValueEntry.commitId.compareTo(kv.prepareId) < 0) {
                    if (kev == null) {
                        kev = keyValueEntry;
                    } else if (kev.commitId.compareTo(keyValueEntry.commitId) < 0) {
                        kev = keyValueEntry;
                    }
                }
            }
            lock.readLock().unlock();
            return kev;
        }

        private KeyValueEntry getMax() {
            CommitId commitId = new CommitId(0L, 0);
            KeyValueEntry keyValueEntry = null;
            for (KeyValueEntry keyValue : values) {
                if (keyValue.commitId.compareTo(commitId) > 0) {
                    keyValueEntry = keyValue;
                }
            }
            return keyValueEntry;
        }


        private NodeImp(final KeyValueEntry kv) {
            values.add(kv);
            this.key = kv.key;
        }

        private NodeImp() {

        }

    }

    public Node[] newNodeArray(final int level, final KeyValueEntry kv) {
        Node[] nodes = new Node[level];
        for (int i = 1; i < level; i++) {
            nodes[i] = new Node();
        }
        nodes[0] = new NodeImp(kv);
        return nodes;
    }


    public SkipListRep(boolean commitRead) {

        Node[] nodes = new Node[MAX_LEVEL];
        for (int i = 1; i < MAX_LEVEL; i++) {
            nodes[i] = new Node();
        }

        nodes[0] = new NodeImp();
        head = nodes;
        this.commitRead = commitRead;
        maxCommitId = new CommitId(0L, 0);
    }


}