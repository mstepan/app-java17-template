package leetcode.hard;

/**
 * 25. Reverse Nodes in k-Group.
 * https://leetcode.com/problems/reverse-nodes-in-k-group/
 */
public class ReverseNodesInKGroup {

    public static void main(String[] args) throws Exception {

        ListNode head = ListNode.fromArray(new int[]{1, 2, 3, 4, 5});

        System.out.println(head);

        ListNode reversedInGroup = reverseKGroup(head, 1);

        System.out.println(reversedInGroup);

        System.out.println("Main done...");
    }

    public static ListNode reverseKGroup(ListNode head, int k) {
        if (k == 1) {
            return head;
        }

        ListNode first = head;
        ListNode last = move(head, k - 1);

        if (last == null) {
            return head;
        }

        ListNode newHead = null;
        ListNode tail = null;

        while (last != null) {
            ListNode rev = reverse(first, last);
            if (newHead == null) {
                newHead = rev;
            }
            else {
                tail.next = rev;
            }

            tail = first;
            first = first.next;
            last = move(first, k - 1);
        }

        return newHead;
    }

    private static ListNode reverse(ListNode first, ListNode last) {

        ListNode newHead = first;

        while (true) {
            ListNode cur = first.next;

            first.next = cur.next;
            cur.next = newHead;
            newHead = cur;

            if (cur == last) {
                break;
            }
        }

        return newHead;
    }

    private static ListNode move(ListNode start, int hops) {
        ListNode cur = start;

        for (int it = 0; it < hops && cur != null; ++it) {
            cur = cur.next;
        }

        return cur;
    }

    public static class ListNode {
        int val;
        ListNode next;

        static ListNode fromArray(int[] arr) {
            ListNode root = new ListNode(arr[0]);
            ListNode last = root;

            for (int i = 1; i < arr.length; ++i) {
                ListNode newNode = new ListNode(arr[i]);
                last.next = newNode;
                last = newNode;
            }

            return root;
        }

        ListNode() {
        }

        ListNode(int val) {
            this.val = val;
        }

        ListNode(int val, ListNode next) {
            this.val = val;
            this.next = next;
        }

        @Override
        public String toString() {

            StringBuilder res = new StringBuilder();

            ListNode cur = this;

            while (cur != null) {
                res.append(cur.val).append(", ");
                cur = cur.next;
            }

            if (!res.isEmpty()) {
                res.setLength(res.length() - 2);
            }

            return res.toString();
        }
    }

}



