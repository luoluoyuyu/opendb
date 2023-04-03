package net.openio.jrocksDb.db;

import net.openio.jrocksDb.mem.KeyValueEntry;
import net.openio.jrocksDb.transaction.CommitId;
import net.openio.jrocksDb.transaction.SequenceNumber;


import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

import java.util.concurrent.locks.ReentrantReadWriteLock;

public interface WriteBatchBase {


    CommitId getCommitId();

    SequenceNumber getSequenceNumber();


    Iterator<KeyValueEntry> getAll();


    final class SkipListRep {

        private static final int MAX_LEVEL = 64; // 最大层数

        private volatile Node[] head; // 头结点

        private static final double P = 0.25; // 每一层加入节点的概率

        int keyNum;


        // 生成随机层数
        private int randomLevel() {
            int level = 1;
            while (ThreadLocalRandom.current().nextDouble() < P && level < MAX_LEVEL) {
                level++;
            }
            return level;
        }

        public void addKeyValue(KeyValueEntry keyValue) {
            if (keyValue == null || keyValue.getKey() == null) {
                throw new RuntimeException("keyValue is null or ke is null ");
            }
            Map<Integer, Node[]> map = new HashMap();
            Node[] le = head;
            add(map, this.head);
            for (int i = MAX_LEVEL - 1; i >= 0; ) {
                if (le[i].next == null) {
                    i--;
                    continue;
                }
                int d = ((NodeImp) le[i].next[0]).getKey().compareTo(keyValue.getKey());
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
            Node[] nodes = newNodeArray(randomLevel(), keyValue);
            int l = nodes.length;
            for (int a = 0; a < l; a++) {
                Node[] node = map.get(a);
                nodes[a].next = node[a].next;
                node[a].next = nodes[a].next;

            }

            keyNum++;


        }


        private void add(Map<Integer, Node[]> map, Node[] node) {
            for (int i = node.length - 1; i >= 0; i--) {
                map.put(i, node);
            }
        }


        public KeyValueEntry getValue(Key key) {
            Node[] le = head;
            for (int i = MAX_LEVEL - 1; i >= 0; ) {
                if (le[i].next == null) {
                    i--;
                    continue;
                }
                int d = ((NodeImp) le[i].next[0]).getKey().compareTo(key);
                if (d > 0) {
                    i--;
                    continue;
                }
                if (d == 0) {
                    return ((NodeImp) le[i].next[0]).get();
                }
                le = le[i].next;
            }
            return null;
        }


        public int getKeySize() {
            return keyNum;
        }


        class Node {
            volatile Node[] next;

        }

        final class NodeImp extends Node {

            KeyValueEntry value;


            private void add(final KeyValueEntry kv) {
                value = kv;
            }

            private KeyValueEntry get() {
                return value;
            }

            private Key getKey() {
                return value.getKey();
            }


            private NodeImp(final KeyValueEntry kv) {
                value = kv;

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


        public SkipListRep() {
            Node[] nodes = new Node[MAX_LEVEL];
            for (int i = 1; i < MAX_LEVEL; i++) {
                nodes[i] = new Node();
            }
            nodes[0] = new NodeImp();
            head = nodes;

        }

    }
}
