package ds;

import java.util.AbstractSequentialList;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;

/**
 * Unrolled linked list implementation.
 * <a href="https://en.wikipedia.org/wiki/Unrolled_linked_list">Unrolled linked list WIKI</a>
 */
public class UnrolledLinkedList<E> extends AbstractSequentialList<E> implements List<E> {

    private ChunkNode<E> head;
    private ChunkNode<E> tail;

    public UnrolledLinkedList() {
        head = new ChunkNode<>();
        tail = head;
    }

    private int size;

    @Override
    public ListIterator<E> listIterator(int index) {
        return new ListItr(index);
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    public void addFirst(E val) {
        if (head.isFull()) {
            head = splitNode(head);
        }
        head.addFirst(val);
        ++size;
    }

    /**
     * Split 'curNode' into 2 nodes, adding a new node with half of elements to the left.
     */
    ChunkNode<E> splitNode(ChunkNode<E> curNode) {
        ChunkNode<E> leftNode = new ChunkNode<>();

        ChunkNode<E> prevLeft = curNode.prev;

        leftNode.next = curNode;
        curNode.prev = leftNode;

        if (prevLeft != null) {
            prevLeft.next = leftNode;
            leftNode.prev = prevLeft;
        }

        moveHalf(curNode, leftNode);

        return leftNode;
    }

    private void moveHalf(ChunkNode<E> srcNode, ChunkNode<E> destNode) {
        final int moveCount = srcNode.length() >>> 1;

        // copy half of elements from 'src.data' array to 'dest.data' array
        System.arraycopy(srcNode.data, 0, destNode.data, 0, moveCount);
        destNode.last = moveCount;

        // shift 'srd.data' array 'leftCount' positions to the left
        final int leftCount = srcNode.length() - moveCount;
        System.arraycopy(srcNode.data, moveCount, srcNode.data, 0, leftCount);

        // null all unused slots to prevent any memory leaks
        for (int i = leftCount; i < srcNode.length(); ++i) {
            srcNode.data[i] = null;
        }

        srcNode.last = leftCount;
    }

    public E pollFirst() {
        if (isEmpty()) {
            return null;
        }

        E val = head.extractFirst();


        if (head.isEmpty() && head.next != null) {
            ChunkNode<E> tempHead = head.next;

            head.next = null;
            tempHead.prev = null;
            head = tempHead;
        }

        --size;
        return val;
    }

    @Override
    public boolean add(E value) {
        addLast(value);
        return true;
    }

    public void addLast(E value) {
        if (tail.isFull()) {
            ChunkNode<E> leftNode = splitNode(tail);

            if (head == tail) {
                head = leftNode;
            }
        }

        tail.addLast(value);
        ++size;
    }

    public E pollLast() {
        if (isEmpty()) {
            return null;
        }

        if (tail.isEmpty()) {

            ChunkNode<E> prevTail = tail.prev;

            tail.prev = null;

            tail = prevTail;
            tail.next = null;
        }

        E valueToRet = tail.extractLast();

        --size;
        return valueToRet;
    }

    public void push(E value) {
        addLast(value);
    }

    public E pop() {
        return pollLast();
    }

    public void enqueue(E val) {
        addLast(val);
    }

    public E dequee() {
        return pollFirst();
    }

    @Override
    public String toString() {
        if (isEmpty()) {
            return "[]";
        }

        StringBuilder buf = new StringBuilder();

        ChunkNode<E> cur = head;

        while (cur != null) {

            for (int i = 0; i < cur.last; ++i) {
                if (buf.isEmpty()) {
                    buf.append("[").append(cur.data[i]);
                }
                else {
                    buf.append(", ").append(cur.data[i]);
                }
            }

            cur = cur.next;
        }

        buf.append("]");
        return buf.toString();
    }


    private final class ListItr implements ListIterator<E> {

        private ChunkNode<E> curNode;

        private int positionWithinList;

        private int curIndex;

        ListItr(int start) {
            assert start >= 0 && start < UnrolledLinkedList.this.size() :
                "'start' = " + start + ", for ListIterator is out of bounds [0; " + UnrolledLinkedList.this.size() + "]";

            positionWithinList = start;
            curNode = UnrolledLinkedList.this.head;

            int leftElements = start;

            for (int i = 0; i < start && leftElements >= curNode.length(); ++i) {
                leftElements -= curNode.length();
                curNode = curNode.next;
                assert curNode != null : "'curNode' is NULL";
            }

            curIndex = leftElements;
        }

        @Override
        public boolean hasNext() {
            return curNode != null;
        }

        @Override
        public E next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }

            E retValue = curNode.getElement(curIndex);
            moveToNextNode();

            return retValue;
        }

        private void moveToNextNode() {
            ++curIndex;
            ++positionWithinList;

            assert curNode != null : "'curNode' is NULL here";
            if (curIndex >= curNode.length()) {
                curNode = curNode.next;
                curIndex = 0;
            }
        }


        @Override
        public boolean hasPrevious() {
            return curNode != null;
        }

        @Override
        public E previous() {
            if (!hasPrevious()) {
                throw new NoSuchElementException();
            }

            E retValue = curNode.getElement(curIndex);

            moveToPreviousNode();

            return retValue;
        }

        private void moveToPreviousNode() {
            --curIndex;
            --positionWithinList;

            if (curIndex < 0) {
                curNode = curNode.prev;
                if (curNode != null) {
                    curIndex = curNode.length() - 1;
                }
            }
        }

        @Override
        public int nextIndex() {
            if (!hasNext()) {
                return UnrolledLinkedList.this.size;
            }
            return positionWithinList;
        }

        @Override
        public int previousIndex() {
            if (!hasPrevious()) {
                return -1;
            }
            return positionWithinList - 1;
        }

        @Override
        public void remove() {
            //TODO:
        }

        @Override
        public void set(E e) {
            //TODO:
        }

        @Override
        public void add(E e) {
            //TODO:
        }
    }

    private static class ChunkNode<T> {

        private static final int NODE_SIZE = 5;

        @SuppressWarnings("unchecked")
        final T[] data = (T[]) new Object[NODE_SIZE];

        int last;

        ChunkNode<T> prev;
        ChunkNode<T> next;

        boolean isFull() {
            return last == data.length;
        }

        public boolean isEmpty() {
            return last == 0;
        }

        public int length() {
            return last;
        }

        public void addLast(T value) {
            data[last] = value;
            ++last;
        }

        public void addFirst(T val) {
            shiftRight();
            data[0] = val;
        }

        public T extractLast() {
            --last;
            T val = data[last];
            data[last] = null;

            return val;
        }

        public T extractFirst() {
            T val = data[0];

            shiftLeft();

            return val;
        }

        /**
         * Shift all elements to the right by 1 position
         */
        private void shiftRight() {
            System.arraycopy(data, 0, data, 1, last);
            ++last;
            data[0] = null;
        }

        /**
         * Shift all elements to the left by 1 position
         */
        private void shiftLeft() {
            System.arraycopy(data, 1, data, 0, last - 1);
            --last;
            data[last] = null;
        }

        public T getElement(int curIndex) {
            return data[curIndex];
        }

        @Override
        public String toString() {
            StringBuilder buf = new StringBuilder();

            if (prev == null) {
                buf.append("HEAD: ");
            }
            else if (next == null) {
                buf.append("TAIL: ");
            }
            buf.append("[");

            for (int i = 0; i < last; ++i) {
                if (i != 0) {
                    buf.append(", ");
                }
                buf.append(data[i]);
            }

            buf.append("]");

            return buf.toString();
        }


    }
}
