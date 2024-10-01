package com.github.mstepan.app17.ds;

import java.util.AbstractSet;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Spliterator;
import java.util.Spliterators;
import org.jetbrains.annotations.NotNull;

/**
 * Ternary search tree data structure implementation for String ONLY elements.
 *
 * <p><a href="https://en.wikipedia.org/wiki/Ternary_search_tree">wiki/Ternary_search_tree</a>
 *
 * <p>Not thread safe.
 */
public class TernarySearchTreeSet extends AbstractSet<String> {

    private TernaryNode root;

    private TernaryNode leafs;

    private int size;
    private long version;

    @Override
    public boolean add(String value) {
        Objects.requireNonNull(value, "Can't save null value. Nulls are prohibited.");

        if (root == null) {
            root = insertPath(value, 0);

            ++version;
            ++size;
            return true;
        }

        TernaryNode cur = root;
        TernaryNode prev = root;

        MoveDirection lastMove = null;

        int idx = 0;

        while (idx < value.length() && cur != null) {
            prev = cur;

            char valueCh = value.charAt(idx);

            if (cur.ch == valueCh) {
                ++idx;
                cur = cur.mid;
                lastMove = MoveDirection.MIDDLE;
            } else if (valueCh > cur.ch) {
                cur = cur.right;
                lastMove = MoveDirection.RIGHT;
            } else {
                cur = cur.left;
                lastMove = MoveDirection.LEFT;
            }
        }

        if (idx < value.length()) {
            switch (lastMove) {
                case MIDDLE -> {
                    TernaryNode child = insertPath(value, idx);
                    child.parent = prev;
                    prev.mid = child;
                }
                case LEFT -> {
                    TernaryNode child = insertPath(value, idx);
                    child.parent = prev;
                    prev.left = child;
                }
                case RIGHT -> {
                    TernaryNode child = insertPath(value, idx);
                    child.parent = prev;
                    prev.right = child;
                }
            }

            ++version;
            ++size;
            return true;
        } else if (prev.isIntermediate()) {
            // trying to insert a node that is a prefix of previously inserted value
            prev.markAsTerminal();

            ++version;
            ++size;
            return true;
        }

        return false;
    }

    private TernaryNode insertPath(String value, int from) {
        assert value != null;
        assert from >= 0;
        assert from < value.length();

        TernaryNode first = TernaryNode.intermediate(value.charAt(from));
        TernaryNode last = first;

        for (int i = from + 1; i < value.length(); ++i) {
            TernaryNode newNode = TernaryNode.intermediate(value.charAt(i));
            newNode.parent = last;
            last.mid = newNode;
            last = newNode;
        }

        last.markAsTerminal();

        // insert last node into leafs double-linked list
        if (leafs == null) {
            last.linkPreAndNextToSelf();
            leafs = last;
        } else {
            TernaryNode head = leafs;
            TernaryNode tail = leafs.prev;

            tail.next = last;
            last.prev = tail;

            head.prev = last;
            last.next = head;
        }

        return first;
    }

    @Override
    public boolean contains(Object val) {
        Objects.requireNonNull(val, "Can't search for null value. Nulls are prohibited.");

        if (val instanceof String value) {

            if (root == null) {
                return false;
            }

            int idx = 0;

            TernaryNode cur = root;
            TernaryNode lastVisited = root;

            while (cur != null && idx < value.length()) {

                lastVisited = cur;

                char valueCh = value.charAt(idx);
                if (cur.ch == valueCh) {
                    cur = cur.mid;
                    ++idx;
                } else if (valueCh > cur.ch) {
                    cur = cur.right;
                } else {
                    cur = cur.left;
                }
            }

            // We found a value only if both conditions are true:
            // 1. we have consumed all characters from a string;
            // 2. the last visited TernaryNode was a TERMINAL one.
            return idx == value.length() && lastVisited.isTerminal();
        } else {
            throw new IllegalArgumentException(
                    "Can't search for a value that is not a 'java.lang.String' type");
        }
    }

    @Override
    public boolean remove(Object obj) {
        Objects.requireNonNull(obj, "Can't delete null value. Nulls are prohibited.");

        if (root == null) {
            return false;
        }

        if (obj instanceof String value) {
            TernaryNode cur = root;
            TernaryNode prev = cur;

            TernaryNode lastWithOneOrZeroLinksParent = null;
            TernaryNode lastWithOneOrZeroLinks = null;

            int idx = 0;

            while (idx < value.length() && cur != null) {

                if (cur.linksCount() > 1) {
                    lastWithOneOrZeroLinks = null;
                } else if (lastWithOneOrZeroLinks == null) {
                    lastWithOneOrZeroLinks = cur;
                    lastWithOneOrZeroLinksParent = prev;
                }

                prev = cur;

                char valueCh = value.charAt(idx);

                if (valueCh == cur.ch) {
                    cur = cur.mid;
                    ++idx;
                } else if (valueCh > cur.ch) {
                    cur = cur.right;
                } else {
                    cur = cur.left;
                }
            }

            // value not found
            if (idx < value.length()) {
                return false;
            }

            // check if last processed node has type TERMINAL, otherwise value not found
            if (prev.isTerminal()) {

                // remove node from leafs linked-list
                prev.removeFromLeafs();

                // last node has more than just 1 link, so we should mark 'prev' as INTERMEDIATE
                if (prev.linksCount() > 1) {
                    prev.markAsIntermediate();
                } else {
                    // last element left in a tree, just nullify 'root' reference
                    if (lastWithOneOrZeroLinksParent == lastWithOneOrZeroLinks) {
                        root = null;
                    } else {
                        lastWithOneOrZeroLinksParent.unlink(lastWithOneOrZeroLinks);
                    }
                }

                ++version;
                --size;
                return true;
            }

            return false;
        } else {
            throw new IllegalArgumentException(
                    "Can't delete value that is not a 'java.lang.String' type but '%s'"
                            .formatted(obj.getClass().getCanonicalName()));
        }
    }

    @Override
    public void clear() {
        root = null;
        leafs = null;
        size = 0;
    }

    @Override
    public @NotNull Iterator<String> iterator() {
        return new InserionOrderIterator(version, leafs, size);
    }

    @Override
    public Spliterator<String> spliterator() {
        // TODO: implement more efficient spliterator here
        return Spliterators.spliterator(this, 0);
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return root == null;
    }

    private enum MoveDirection {
        LEFT,
        MIDDLE,
        RIGHT
    }

    private static final class TernaryNode {

        TernaryNode parent;

        final char ch;
        NodeType type;

        TernaryNode left;
        TernaryNode mid;
        TernaryNode right;

        TernaryNode prev;
        TernaryNode next;

        TernaryNode(char ch, NodeType type) {
            this.ch = ch;
            this.type = type;
        }

        private static TernaryNode intermediate(char ch) {
            return new TernaryNode(ch, NodeType.INTERMEDIATE);
        }

        @Override
        public String toString() {
            return ch + ", " + type;
        }

        public void markAsTerminal() {
            this.type = NodeType.TERMINAL;
        }

        public boolean isTerminal() {
            return type == NodeType.TERMINAL;
        }

        public boolean isIntermediate() {
            return type == NodeType.INTERMEDIATE;
        }

        public void markAsIntermediate() {
            this.type = NodeType.INTERMEDIATE;
        }

        public int linksCount() {
            return (left == null ? 0 : 1) + (right == null ? 0 : 1) + (mid == null ? 0 : 1);
        }

        public void unlink(TernaryNode child) {
            if (left == child) {
                left = null;
            } else if (right == child) {
                right = null;
            } else if (mid == child) {
                mid = null;
            } else {
                throw new IllegalStateException(
                        "Can't unlink child from parent b/c incorrect child passed");
            }
        }

        public void linkPreAndNextToSelf() {
            prev = this;
            next = this;
        }

        /** Build String key traversing all nodes upward. */
        public String buildKey() {
            StringBuilder key = new StringBuilder();

            TernaryNode cur = this;
            key.append(cur.ch);

            TernaryNode prev = this;

            while (cur != null) {

                if (cur.mid == prev) {
                    key.append(cur.ch);
                }

                prev = cur;
                cur = cur.parent;
            }

            return key.reverse().toString();
        }

        public void removeFromLeafs() {
            TernaryNode prevTemp = prev;
            TernaryNode nextTemp = next;

            next = null;
            prev = null;

            if (prevTemp != null) {
                prevTemp.next = nextTemp;
            }

            if (nextTemp != null) {
                nextTemp.prev = prevTemp;
            }
        }
    }

    private enum NodeType {
        TERMINAL,
        INTERMEDIATE
    }

    private final class InserionOrderIterator implements Iterator<String> {

        private final long versionSnapshot;

        private final int leafsCount;

        private TernaryNode cur;
        private int curIdx;

        public InserionOrderIterator(
                long versionSnapshot, TernaryNode leafsStartNode, int leafsCount) {
            this.versionSnapshot = versionSnapshot;
            this.cur = leafsStartNode;
            this.leafsCount = leafsCount;
            this.curIdx = 0;
        }

        @Override
        public boolean hasNext() {
            return curIdx < leafsCount;
        }

        @Override
        public String next() {
            if (!hasNext()) {
                throw new NoSuchElementException("No more element left to iterate over");
            }

            if (versionSnapshot != TernarySearchTreeSet.this.version) {
                throw new ConcurrentModificationException();
            }

            String key = cur.buildKey();
            ++curIdx;

            cur = cur.next;

            return key;
        }
    }
}
