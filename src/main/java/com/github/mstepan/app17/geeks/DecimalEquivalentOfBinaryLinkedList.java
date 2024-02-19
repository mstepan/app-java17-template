package com.github.mstepan.app17.geeks;

/*
 * Decimal Equivalent of Binary Linked List
 * https://www.geeksforgeeks.org/problems/decimal-equivalent-of-binary-linked-list/1?utm_source=geeksforgeeks&utm_medium=newui_home&utm_campaign=potd
 *
 */
public class DecimalEquivalentOfBinaryLinkedList {

    public static void main(String[] args) throws Exception {

        LinkedList root = new LinkedList();

        // {1, 1, 1, 0}
        root.head = new Node(1, new Node(1, new Node(1, new Node(0))));

        long res = DecimalValue(root.head);

        System.out.printf("res = %d%n", res);

        System.out.println("DecimalEquivalentOfBinaryLinkedList done...");
    }

    private static final int MOD = 1_000_000_007;

    static long DecimalValue(Node head) {
        Node cur = head;

        int res = 0;

        while (cur != null) {
            int digit = cur.data;
            res = (res * 2 + digit) % MOD;
            cur = cur.next;
        }

        return res;
    }

    // === DO NOT COPY below code ====

    static class Node {
        int data;
        Node next;

        Node(int data, Node next) {
            this.data = data;
            this.next = next;
        }

        Node(int data) {
            this(data, null);
        }
    }

    static class LinkedList {
        Node head;
    }
}
