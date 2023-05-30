package ds;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Skip-list implementation.
 */
public class SkipListSet {

    // log2(4_294_967_296) ~ 32
    private static final int MAX_SKIP_LIST_LEVEL = 32;

    private final ThreadLocalRandom rand = ThreadLocalRandom.current();

    private final SkipNode head;

    private final SkipNode tail;

    private int size;

    private int maxLevelValue;

    public SkipListSet() {
        head = new SkipNode(NodeType.HEAD);
        tail = new SkipNode(NodeType.TAIL);

        head.setNext(0, tail);
        tail.setPrev(head);
    }

    public int getMaxLevelValue() {
        return maxLevelValue;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public boolean add(int value) {

        List<SkipNode> searchPath = findNode(value);

        ListIterator<SkipNode> searchPathIt = searchPath.listIterator(searchPath.size());

        assert searchPathIt.hasPrevious() : "'searchPathIt' is empty, doesn't have previous value";

        SkipNode lastNode = searchPathIt.previous();

        // not found, so insert new 'value'
        if (lastNode == head || lastNode.value != value) {

            SkipNode newNode = new SkipNode(NodeType.NORMAL, value);

            // always insert into '0' level
            insertAfter(lastNode, 0, newNode);
            //System.out.printf("inserting '%d' into tier %d\n", value, 0);

            for (int curLevel = 1; curLevel < MAX_SKIP_LIST_LEVEL; ++curLevel) {

                // check if node need to be inserted to higher tiers with probability 1/2
                boolean shouldInsert = rand.nextBoolean();

                if (!shouldInsert) {
                    break;
                }

                SkipNode parentNode = searchPathIt.hasPrevious() ? searchPathIt.previous() : head;

                //System.out.printf("inserting '%d' into tier %d\n", value, curLevel);

                insertAfter(parentNode, curLevel, newNode);
            }

            ++size;
            return true;
        }
        return false;
    }

    private void insertAfter(SkipNode parentNode, int level, SkipNode cur) {

        maxLevelValue = Math.max(maxLevelValue, level);

        if (level == 0) {
            // '0' level, insert into double-linked list
            SkipNode nextNode = parentNode.getNext(level);

            assert nextNode != null : "null value for 'nextNode' detected";

            parentNode.setNext(0, cur);
            cur.setPrev(parentNode);

            cur.setNext(0, nextNode);
            nextNode.prev = cur;
        }
        else {
            // any other, non '0' level, insert into single-linked list ONLY
            if (parentNode == head) {
                // if we are building completely new level, specify the link: head -> tail
                if (parentNode.getNext(level) == null) {
                    parentNode.setNext(level, tail);
                }
            }

            SkipNode nextNode = parentNode.getNext(level);

            parentNode.setNext(level, cur);
            cur.setNext(level, nextNode);
        }
    }

    /**
     * Returns the whole search path for a node.
     * If node exists in a set it will be the last element in search path.
     */
    private List<SkipNode> findNode(int value) {

        int curLevel = head.nextNodes.size() - 1;
        SkipNode curNode = head;

        // we expect that search path has 'log N' length with high probability
        List<SkipNode> searchPath = new ArrayList<>();

        while (curLevel > 0) {
            SkipNode nextNode = curNode.getNext(curLevel);

            assert nextNode != null;

            if (nextNode == tail || nextNode.value > value) {
                --curLevel;
                searchPath.add(curNode);
                continue;
            }

            curNode = curNode.getNext(curLevel);
        }

        // linear search for level '0'
        while (true) {
            SkipNode nextNode = curNode.getNext(0);

            assert nextNode != null;

            if (nextNode == tail || nextNode.value > value) {
                break;
            }

            curNode = nextNode;
        }

        searchPath.add(curNode);

        return searchPath;
    }


    public boolean contains(int value) {

        List<SkipNode> searchPath = findNode(value);

        SkipNode lastNodeInPath = searchPath.get(searchPath.size() - 1);

        if (lastNodeInPath == head) {
            return false;
        }

        return lastNodeInPath.value == value;
    }

    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder();
        buf.append("[");

        SkipNode cur = head.getNext(0);

        if (cur != tail) {
            buf.append(cur.value);
            cur = cur.getNext(0);
        }

        while (cur != tail) {
            buf.append(", ").append(cur.value);
            cur = cur.getNext(0);
        }

        buf.append("]");
        return buf.toString();
    }

    public String toStringReverse() {
        StringBuilder buf = new StringBuilder();
        buf.append("[");

        SkipNode cur = tail.prev;

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
        HEAD, TAIL, NORMAL;
    }

    private static class SkipNode {
        final NodeType type;

        final List<SkipNode> nextNodes;

        SkipNode prev;

        int value;

        public SkipNode(NodeType nodeType) {
            this(nodeType, -1);
        }

        public SkipNode(NodeType nodeType, int initialValue) {
            type = nodeType;
            nextNodes = new ArrayList<>();
            value = initialValue;
        }

        SkipNode getNext(int level) {
            if (level >= nextNodes.size()) {
                return null;
            }

            return nextNodes.get(level);
        }

        void setNext(int level, SkipNode nextNode) {
            if (nextNodes.size() == level) {
                nextNodes.add(level, nextNode);
            }
            else {
                nextNodes.set(level, nextNode);
            }
        }

        public void setPrev(SkipNode prev) {
            this.prev = prev;
        }

        @Override
        public String toString() {
            String stringVal = switch (type) {
                case HEAD -> "HEAD";
                case TAIL -> "TAIL";
                case NORMAL -> String.valueOf(value);
            };

            return "[" + stringVal + "]";
        }
    }

}
