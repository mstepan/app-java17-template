package com.max.app17.leetcode.medium;

import com.max.app17.Main;

public class KthSmallestElementInBst {

    public static void main(String[] args) throws Exception {

        TreeNode one = new TreeNode(1);
        TreeNode two = new TreeNode(2);
        two.left = one;

        TreeNode four = new TreeNode(4);

        TreeNode three = new TreeNode(3);
        three.left = two;
        three.right = four;

        TreeNode six = new TreeNode(6);

        TreeNode five = new TreeNode(5);

        five.left = three;
        five.right = six;

        for(int i = 0; i < 10; ++i){
            System.out.println(new KthSmallestElementInBst().kthSmallest(five, i));
        }

        System.out.println("KthSmallestElementInBst done...");
    }

    /**
     * Do in-order traversal for BS-tree (left-right-parent) and count nodes.
     *
     * Time: O(K) ~ O(N)
     * Space: O(N)
     */
    public int kthSmallest(TreeNode root, int k) {
        PartialResult result = kthSmallestRec(root, 1, k);

        if( result.solution == null ){
            return -1;
        }

        return result.solution.val;
    }

    public PartialResult kthSmallestRec(TreeNode cur, int order, int k) {

        if( cur == null){
            return  new PartialResult(null, order);
        }

        PartialResult left = kthSmallestRec(cur.left, order, k);

        if( left.solution != null ){
            return left;
        }
        int curOrder = left.nextOrder;

        if( curOrder == k ){
            return  new PartialResult(cur, curOrder);
        }

        return kthSmallestRec(cur.right, curOrder+1, k);
    }


    record PartialResult(TreeNode solution, int nextOrder){

    }


    // ==== below code should not be copied ====
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

}
