package com.max.app17.leetcode.medium;


/**
 * 61. Rotate List
 *
 * <p>https://leetcode.com/problems/rotate-list/
 */
public class RotateList {

    public static void main(String[] args) throws Exception {

        for (int rotationPoint = 0; rotationPoint < 10; ++rotationPoint) {

            ListNode fifth = new ListNode(5, null);
            ListNode fourth = new ListNode(4, fifth);
            ListNode third = new ListNode(3, fourth);
            ListNode second = new ListNode(2, third);
            ListNode first = new ListNode(1, second);

            ListNode head = first;

            ListNode rotatedHead = new RotateList().rotateRight(head, rotationPoint);

            System.out.printf("rotation: %d, list: %s%n", rotationPoint, listToString(rotatedHead));
        }

        System.out.println("RotateList done...");
    }

    /**
     * time: O(N), space: O(1)
     */
    public ListNode rotateRight(ListNode head, int k) {
        checkArgument(k >= 0, "k < 0");

        if (k == 0) {
            return head;
        }

        int length = calculateLength(head);

        if (length == 0) {
            return head;
        }

        int offset = k % length;

        if (offset == 0) {
            return head;
        }

        int rightNodesCnt = length - offset;

        ListNode cur = head;
        int curIdx = 1;

        while (curIdx < rightNodesCnt) {
            cur = cur.next;
            ++curIdx;
        }

        ListNode newHead = cur.next;
        cur.next = null;

        ListNode last = findLast(newHead);
        last.next = head;

        return newHead;
    }

    private ListNode findLast(ListNode startNode) {
        assert startNode != null;

        ListNode cur = startNode;

        while (cur.next != null) {
            cur = cur.next;
        }

        return cur;
    }

    private int calculateLength(ListNode head) {

        int length = 0;
        for (ListNode cur = head; cur != null; cur = cur.next) {
            ++length;
        }

        return length;
    }

    private void checkArgument(boolean testPredicate, String errorMsg) {
        if (!testPredicate) {
            throw new IllegalArgumentException(errorMsg);
        }
    }

    // ======= BELOW code should not be submitted =============

    public static class ListNode {

        final int val;
        ListNode next;

        ListNode(int val) {
            this.val = val;
        }

        ListNode(int val, ListNode next) {
            this.val = val;
            this.next = next;
        }
    }

    private static String listToString(ListNode head) {

        StringBuilder res = new StringBuilder();

        for (ListNode cur = head; cur != null; cur = cur.next) {
            if (!res.isEmpty()) {
                res.append(", ");
            }
            res.append(cur.val);
        }

        return res.toString();
    }
}
