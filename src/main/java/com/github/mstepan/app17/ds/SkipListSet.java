package com.github.mstepan.app17.ds;

import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Skip-list implementation. For more details check <a
 * href="https://brilliant.org/wiki/skip-lists/">Skip List</a>
 */
public class SkipListSet<E extends Comparable<E>> extends AbstractSet<E> implements Set<E> {

    // log2(4_294_967_296) ~ 32
    private static final int MAX_SKIP_LIST_LEVEL = 32;

    private final ThreadLocalRandom rand = ThreadLocalRandom.current();

    private final SkipNode<E> head;

    private final SkipNode<E> tail;

    private int size;

    private int maxLevelValue;

    public SkipListSet() {
        head = new SkipNode<>(NodeType.HEAD);
        tail = new SkipNode<>(NodeType.TAIL);

        head.setNext(0, tail);
        tail.setPrev(head);
    }

    public int getMaxLevelValue() {
        return maxLevelValue;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean add(E value) {
        Objects.requireNonNull(value, "Can't insert null value.");

        List<SkipNode<E>> searchPath = findNode(value);

        ListIterator<SkipNode<E>> searchPathIt = searchPath.listIterator(searchPath.size());

        assert searchPathIt.hasPrevious() : "'searchPathIt' is empty, doesn't have previous value";

        SkipNode<E> lastNode = searchPathIt.previous();

        // not found, so insert new 'value'
        if (lastNode == head || value.compareTo(lastNode.value) != 0) {

            SkipNode<E> newNode = new SkipNode<>(NodeType.NORMAL, value);

            // always insert into '0' level
            insertAfter(lastNode, 0, newNode);
            // System.out.printf("inserting '%d' into tier %d\n", value, 0);

            for (int curLevel = 1; curLevel < MAX_SKIP_LIST_LEVEL; ++curLevel) {

                // check if node need to be inserted to higher tiers with probability 1/2
                boolean shouldInsert = rand.nextBoolean();

                if (!shouldInsert) {
                    break;
                }

                SkipNode<E> parentNode =
                        searchPathIt.hasPrevious() ? searchPathIt.previous() : head;

                // System.out.printf("inserting '%d' into tier %d\n", value, curLevel);

                insertAfter(parentNode, curLevel, newNode);
            }

            ++size;
            return true;
        }
        return false;
    }

    private void insertAfter(SkipNode<E> parentNode, int level, SkipNode<E> cur) {

        maxLevelValue = Math.max(maxLevelValue, level);

        if (level == 0) {
            // '0' level, insert into double-linked list
            SkipNode<E> nextNode = parentNode.getNext(level);

            assert nextNode != null : "null value for 'nextNode' detected";

            parentNode.setNext(0, cur);
            cur.setPrev(parentNode);

            cur.setNext(0, nextNode);
            nextNode.prev = cur;
        } else {
            // any other, non '0' level, insert into single-linked list ONLY
            if (parentNode == head) {
                // if we are building completely new level, specify the link: head -> tail
                if (parentNode.getNext(level) == null) {
                    parentNode.setNext(level, tail);
                }
            }

            SkipNode<E> nextNode = parentNode.getNext(level);

            parentNode.setNext(level, cur);
            cur.setNext(level, nextNode);
        }
    }

    /**
     * Returns the whole search path for a node. If node exists in a set it will be the last element
     * in search path.
     */
    private List<SkipNode<E>> findNode(E value) {

        int curLevel = head.nextNodes.size() - 1;
        SkipNode<E> curNode = head;

        // we expect that search path has 'log N' length with high probability
        List<SkipNode<E>> searchPath = new ArrayList<>();

        while (curLevel > 0) {
            assert curNode != null : "null 'curNode' detected";

            SkipNode<E> nextNode = curNode.getNext(curLevel);

            assert nextNode != null;

            if (nextNode == tail || nextNode.value.compareTo(value) > 0) {
                --curLevel;
                searchPath.add(curNode);
                continue;
            }

            curNode = curNode.getNext(curLevel);
        }

        // linear search for level '0'
        while (true) {
            SkipNode<E> nextNode = curNode.getNext(0);

            assert nextNode != null;

            if (nextNode == tail || nextNode.value.compareTo(value) > 0) {
                break;
            }

            curNode = nextNode;
        }

        searchPath.add(curNode);

        return searchPath;
    }

    @Override
    public boolean contains(Object initialValue) {
        Objects.requireNonNull(
                initialValue,
                "Can't find value inside SkipListSet b/c null values are not allowed.");

        @SuppressWarnings("unchecked")
        final E value = (E) initialValue;

        List<SkipNode<E>> searchPath = findNode(value);

        SkipNode<E> lastNodeInPath = searchPath.get(searchPath.size() - 1);

        if (lastNodeInPath == head) {
            return false;
        }

        return value.compareTo(lastNodeInPath.value) == 0;
    }

    @Override
    public Iterator<E> iterator() {
        return new SkipListIterator();
    }

    private final class SkipListIterator implements Iterator<E> {
        SkipNode<E> cur;
        SkipNode<E> end;

        SkipListIterator() {
            cur = SkipListSet.this.head.getNext(0);
            end = SkipListSet.this.tail;
        }

        @Override
        public boolean hasNext() {
            return cur != end;
        }

        @Override
        public E next() {
            if (!hasNext()) {
                throw new NoSuchElementException("No elements left in SkipListSet iterator.");
            }

            E retValue = cur.value;

            cur = cur.getNext(0);

            return retValue;
        }
    }

    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder();
        buf.append("[");

        // we can use iterator() here, but we can use internal representation to iterate
        // the code will be faster, but will add some maintenance cost
        SkipNode<E> cur = head.getNext(0);

        if (cur != tail) {
            assert cur != null : "null cur value detected";
            buf.append(cur.value);
            cur = cur.getNext(0);
        }

        while (cur != tail) {
            assert cur != null : "null value detected as 'cur'";
            buf.append(", ").append(cur.value);
            cur = cur.getNext(0);
        }

        buf.append("]");
        return buf.toString();
    }

    public String toStringReverse() {
        StringBuilder buf = new StringBuilder();
        buf.append("[");

        SkipNode<E> cur = tail.prev;

        if (cur != head) {
            buf.append(cur.value);
            cur = cur.prev;
        }

        while (cur != head) {
            buf.append(", ").append(cur.value);
            cur = cur.prev;
        }

        buf.append("]");
        return buf.toString();
    }

    private enum NodeType {
        HEAD,
        TAIL,
        NORMAL;
    }

    private static class SkipNode<T> {
        final NodeType type;

        final List<SkipNode<T>> nextNodes;

        SkipNode<T> prev;

        T value;

        public SkipNode(NodeType nodeType) {
            this(nodeType, null);
        }

        public SkipNode(NodeType nodeType, T initialValue) {
            type = nodeType;
            nextNodes = new ArrayList<>();
            value = initialValue;
        }

        SkipNode<T> getNext(int level) {
            if (level >= nextNodes.size()) {
                return null;
            }

            return nextNodes.get(level);
        }

        void setNext(int level, SkipNode<T> nextNode) {
            if (nextNodes.size() == level) {
                nextNodes.add(level, nextNode);
            } else {
                nextNodes.set(level, nextNode);
            }
        }

        public void setPrev(SkipNode<T> prev) {
            this.prev = prev;
        }

        @Override
        public String toString() {
            String stringVal =
                    switch (type) {
                        case HEAD -> "HEAD";
                        case TAIL -> "TAIL";
                        case NORMAL -> String.valueOf(value);
                    };

            return "[" + stringVal + "]";
        }
    }
}
