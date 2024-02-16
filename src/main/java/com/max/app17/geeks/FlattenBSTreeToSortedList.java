package com.max.app17.geeks;

/**
 * Flatten BST to sorted list
 *
 * <p>https://www.geeksforgeeks.org/problems/flatten-bst-to-sorted-list--111950/1?utm_source=geeksforgeeks&utm_medium=newui_home&utm_campaign=potd.
 */
public class FlattenBSTreeToSortedList {

    public static void main(String[] args) throws Exception {

        Node three = new Node(3);
        Node seven = new Node(7);

        Node five = new Node(5);
        five.left = three;
        five.right = seven;

        Node twelve = new Node(12);

        Node eighteen = new Node(18);
        eighteen.left = twelve;

        Node ten = new Node(10);
        ten.left = five;
        ten.right = eighteen;

        Node flatTree = flattenBST(ten);

        System.out.printf("flat tree: %s%n", toListString(flatTree));

        System.out.println("FlattenBSTreeToSortedList done...");
    }

    /**
     * Time: O(N)
     *
     * <p>Space: O(h), so can be in range [lgN....N]
     */
    public static Node flattenBST(Node root) {
        PartialSolution solution = toListRec(root);
        return solution.first;
    }

    private static PartialSolution toListRec(Node cur) {
        if (cur == null) {
            return null;
        }

        Node first = cur;
        Node last = cur;

        if (cur.left != null) {

            PartialSolution leftSide = toListRec(cur.left);
            first = leftSide.first;

            cur.left = null;

            if (leftSide.last != null) {
                leftSide.last.right = cur;
            }
        }

        if (cur.right != null) {

            PartialSolution rightSide = toListRec(cur.right);
            last = rightSide.last;

            cur.right = null;

            cur.right = rightSide.first;
        }

        return new PartialSolution(first, last);
    }

    static class PartialSolution {
        Node first;
        Node last;

        public PartialSolution(Node first, Node last) {
            this.first = first;
            this.last = last;
        }
    }

    // ===== DO NOT copy below code ====

    private static String toListString(Node flatTreeRoot) {
        StringBuilder treeStr = new StringBuilder();

        Node cur = flatTreeRoot;

        if (cur != null) {
            treeStr.append(cur.data);
        }

        cur = cur.right;

        while (cur != null) {
            treeStr.append(" -> ").append(cur.data);
            cur = cur.right;
        }

        return treeStr.toString();
    }

    private static class Node {
        int data;
        Node left;
        Node right;

        public Node(int data) {
            this(data, null, null);
        }

        public Node(int data, Node left, Node right) {
            this.data = data;
            this.left = left;
            this.right = right;
        }

        @Override
        public String toString() {
            return String.valueOf(data);
        }
    }
}
