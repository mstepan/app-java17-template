package com.max.app17.geeks;

/**
 * Subtraction in Linked List
 *
 * <p>https://www.geeksforgeeks.org/problems/subtraction-in-linked-list/1?utm_source=geeksforgeeks&utm_medium=newui_home&utm_campaign=potd
 */
public class SubtractionInLinkedList {

    public static void main(String[] args) throws Exception {

        // expected 647
        //        Node first = new Node(7, new Node(1, new Node(0))); // 710
        //        Node second = new Node(0, new Node(0, new Node(6, new Node(3)))); // 0063

        // expected: 88
        //        Node first = new Node(1, new Node(0, new Node(0))); // 100
        //        Node second = new Node(1, new Node(2)); // 12

        // expected: 88
        Node first = new Node(1, new Node(1)); // 11
        Node second = new Node(1, new Node(0)); // 10

        Node res = subLinkedList(first, second);

        System.out.printf("res: %s%n", res.toString());

        System.out.println("SubtractionInLinkedList done...");
    }

    public static Node subLinkedList(Node head1, Node head2) {

        Node head1Reversed = reverse(removeZeroPrefix(head1));

        Node head2Reversed = reverse(removeZeroPrefix(head2));

        Node bigger = head2Reversed;
        Node smaller = head1Reversed;

        if (isBigger(head1Reversed, head2Reversed)) {
            bigger = head1Reversed;
            smaller = head2Reversed;
        }

        int borrow = 0;

        Node biggerCur = bigger;
        Node smallerCur = smaller;

        Node resHead = null;
        Node resCur = null;

        while (biggerCur != null) {

            int diff = biggerCur.data - borrow - (smallerCur == null ? 0 : smallerCur.data);

            if (diff >= 0) {
                borrow = 0;
            } else {
                borrow = 1;
                diff += 10;
            }

            if (resHead == null) {
                resHead = new Node(diff);
                resCur = resHead;
            } else {
                resCur.next = new Node(diff);
                resCur = resCur.next;
            }

            biggerCur = biggerCur.next;

            if (smallerCur != null) {
                smallerCur = smallerCur.next;
            }
        }

        return removeZeroPrefix(reverse(resHead));
    }

    private static boolean isBigger(Node h1, Node h2) {

        Node cur1 = h1;
        Node cur2 = h2;

        boolean firstBigger = true;

        while (cur1 != null || cur2 != null) {

            int d1 = (cur1 == null) ? 0 : cur1.data;
            int d2 = (cur2 == null) ? 0 : cur2.data;

            if (d1 > d2) {
                firstBigger = true;
            } else if (d1 < d2) {
                firstBigger = false;
            }

            if (cur1 != null) {
                cur1 = cur1.next;
            }

            if (cur2 != null) {
                cur2 = cur2.next;
            }
        }

        return firstBigger;
    }

    private static Node reverse(Node head) {
        Node prev = null;
        Node cur = head;

        while (cur != null) {
            Node temp = cur.next;
            cur.next = prev;
            prev = cur;
            cur = temp;
        }

        return prev;
    }

    private static Node removeZeroPrefix(Node head) {
        Node cur = head;

        while (cur != null && cur.data == 0) {
            cur = cur.next;
        }

        return cur == null ? new Node(0) : cur;
    }

    // ==== DO NOT copy below code ====

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

        @Override
        public String toString() {
            StringBuilder buf = new StringBuilder();

            Node cur = this;

            while (cur != null) {
                buf.append(cur.data).append(", ");
                cur = cur.next;
            }

            return buf.toString();
        }
    }
}
