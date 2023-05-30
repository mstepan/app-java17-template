package ds;

import java.util.ArrayList;
import java.util.List;

public class SkipListSet {

    private final SkipNode head;

    private final SkipNode tail;

    public SkipListSet() {
        head = new SkipNode(NodeType.HEAD);
        tail = new SkipNode(NodeType.TAIL);

        head.setNext(0, tail);
        tail.setPrev(head);
    }

    public boolean add(int value) {

        SkipNode foundNode = findNode(value);

        // not found, so insert new 'value'
        if (foundNode == head || foundNode.value != value) {
            insertAfter(0, foundNode, value);
            return true;
        }

        return false;
    }

    private void insertAfter(int level, SkipNode parentNode, int value) {

        SkipNode cur = new SkipNode(NodeType.NORMAL, value);

        if (level == 0) {
            // insert into double-linked list

            SkipNode nextNode = parentNode.getNext(level);

            parentNode.setNext(0, cur);
            cur.setPrev(parentNode);

            cur.setNext(0, nextNode);
            nextNode.prev = cur;
        }
        else {
            // insert into single-linked list
        }
    }


    SkipNode findNode(int value) {

        int curLevel = head.nextNodes.size() - 1;
        SkipNode curNode = head;

        while (curLevel > 0) {
            SkipNode nextNode = curNode.getNext(curLevel);

            if (nextNode == tail || nextNode.value > value) {
                --curLevel;
            }

            curNode = curNode.getNext(curLevel);
        }

        // linear search for level '0'
        while (true) {
            SkipNode nextNode = curNode.getNext(0);

            if (nextNode == tail || nextNode.value > value) {
                break;
            }

            curNode = nextNode;
        }

        return curNode;
    }

    public boolean contains(int value) {
        return false;
    }

    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder();
        buf.append("[");

        SkipNode cur = head.getNext(0);

        if( cur != tail){
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

    enum NodeType {
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
