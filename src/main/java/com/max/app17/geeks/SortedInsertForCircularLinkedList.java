package com.max.app17.geeks;

/**
 * Sorted insert for circular linked list
 *
 * <p>https://www.geeksforgeeks.org/problems/sorted-insert-for-circular-linked-list/1?utm_source=geeksforgeeks&utm_medium=newui_home&utm_campaign=potd
 */
public class SortedInsertForCircularLinkedList {

    public static void main(String[] args) {
        Node tail = new Node(8);
        Node head = new Node(1, new Node(5, tail));
        tail.next = head;

        Node newHead = new SortedInsertForCircularLinkedList().sortedInsert(head, 4);
        System.out.println(newHead.toListStr());

        System.out.println("SortedInsertForCircularLinkedList done...");
    }

    public Node sortedInsert(Node head, int value) {
        if (head == null) {
            Node newHead = new Node(value);
            newHead.next = newHead;
            return newHead;
        }

        Node smallerNode = findSmaller(head, value);

        if (smallerNode == null) {
            return insertBeforeHead(head, value);
        }

        insertAfter(smallerNode, value);

        return head;
    }

    private static Node findSmaller(Node head, int value) {
        assert head != null;
        if (head.data >= value) {
            return null;
        }

        Node prev = head;
        Node cur = head.next;

        while (cur != head) {
            if (cur.data >= value) {
                break;
            }
            prev = cur;
            cur = cur.next;
        }

        return prev;
    }

    private static Node insertBeforeHead(Node head, int value) {
        assert head != null;

        Node last = findLast(head);

        Node newHead = new Node(value);
        newHead.next = head;

        last.next = newHead;

        return newHead;
    }

    private static void insertAfter(Node cur, int value) {
        assert cur != null;

        Node newNode = new Node(value);
        newNode.next = cur.next;
        cur.next = newNode;
    }

    private static Node findLast(Node head) {
        assert head != null;

        Node prev = head;
        Node cur = head.next;

        while (cur != head) {
            prev = cur;
            cur = cur.next;
        }

        return prev;
    }

    // ==== DO BOT copy below code ====

    static class Node {
        int data;
        Node next;

        public Node(int data, Node next) {
            this.data = data;
            this.next = next;
        }

        public Node(int data) {
            this(data, null);
        }

        @Override
        public String toString() {
            return String.valueOf(data);
        }

        private String toListStr() {
            StringBuilder buf = new StringBuilder();
            buf.append(this.data);

            Node cur = this.next;

            while (cur != this) {
                buf.append(", ").append(cur.data);
                cur = cur.next;
            }

            return buf.toString();
        }
    }
}
