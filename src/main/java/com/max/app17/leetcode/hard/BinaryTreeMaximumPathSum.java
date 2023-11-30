package com.max.app17.leetcode.hard;

import java.util.Objects;

/*
124. Binary Tree Maximum Path Sum
https://leetcode.com/problems/binary-tree-maximum-path-sum/

Uses post-order (left-right-parent) tree traversal to find max path sum.
time: O(N)
space: O(h) ~ O(N)
 */
public class BinaryTreeMaximumPathSum {

    public static void main(String[] args) throws Exception {

        TreeNode a = new TreeNode(15);
        TreeNode b = new TreeNode(7);

        TreeNode c = new TreeNode(20, a, b);
        TreeNode d = new TreeNode(9);

        TreeNode root = new TreeNode(-10, d, c);

        // TreeNode root = new TreeNode(-3);

        int maxPath = new BinaryTreeMaximumPathSum().maxPathSum(root);

        System.out.printf("maxPath: %d%n", maxPath);

        System.out.println("BinaryTreeMaximumPathSum done...");
    }

    public static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        TreeNode() {}

        TreeNode(int val) {
            this.val = val;
        }

        TreeNode(int val, TreeNode left, TreeNode right) {
            this.val = val;
            this.left = left;
            this.right = right;
        }
    }

    public int maxPathSum(TreeNode root) {
        Objects.requireNonNull(root);
        return maxPathSumRec(root).maxPathInSubtree();
    }

    public PartialRes maxPathSumRec(TreeNode cur) {

        assert cur != null;

        PartialRes leftRes = (cur.left == null) ? PartialRes.ZERO : maxPathSumRec(cur.left);
        PartialRes rightRes = (cur.right == null) ? PartialRes.ZERO : maxPathSumRec(cur.right);

        int maxPathInSub =
                max(
                        leftRes.maxPathInSubtree(),
                        rightRes.maxPathInSubtree(),
                        leftRes.maxCostOrZero() + rightRes.maxCostOrZero() + cur.val);

        return new PartialRes(
                maxPathInSub,
                Math.max(leftRes.maxCostOrZero(), rightRes.maxCostOrZero()) + cur.val);
    }

    private int max(int first, int second, int third) {
        return Math.max(Math.max(first, second), third);
    }

    record PartialRes(int maxPathInSubtree, int maxCost) {

        private static final PartialRes ZERO = new PartialRes(Integer.MIN_VALUE, 0);

        int maxCostOrZero() {
            return Math.max(maxCost, 0);
        }
    }
}
