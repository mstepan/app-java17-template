package com.max.app17.leetcode.medium;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 508. Most Frequent Subtree Sum
 *
 * <p>https://leetcode.com/problems/most-frequent-subtree-sum/
 */
public class MostFrequentSubtreeSum {

    public static void main(String[] args) throws Exception {

        TreeNode five = new TreeNode(5);

        TreeNode two = new TreeNode(2);
        TreeNode minusFive = new TreeNode(-5);

        five.left = two;
        five.right = minusFive;

        int[] mostFreq = new MostFrequentSubtreeSum().findFrequentTreeSum(five);

        System.out.println(Arrays.toString(mostFreq));

        System.out.println("MostFrequentSubtreeSum done...");
    }

    /**
     * We will do recursive post-order tree traversal (left-right-parent) and gather all subtree
     * sums in HashMap. time: O(N) space: O(N)
     */
    public int[] findFrequentTreeSum(TreeNode root) {
        Objects.requireNonNull(root);

        List<Integer> mostFreqSubtreeSum = findMostFreqSubtreeSum(root);

        return toIntArray(mostFreqSubtreeSum);
    }

    private List<Integer> findMostFreqSubtreeSum(TreeNode root) {
        assert root != null;

        Map<Integer, Integer> sumAndFreq = new HashMap<>();

        gatherAllSubtreeSumsRec(root, sumAndFreq);

        return findMostFrequentKeys(sumAndFreq);
    }

    private int gatherAllSubtreeSumsRec(TreeNode cur, Map<Integer, Integer> sumAndFreq) {
        if (cur == null) {
            return 0;
        }

        int leftSum = gatherAllSubtreeSumsRec(cur.left, sumAndFreq);
        int rightSum = gatherAllSubtreeSumsRec(cur.right, sumAndFreq);

        // overflow/underflow is not possible here
        // because according to constraints we have 10^4 nodes and each node has a
        // maximum of 10^5 as a value, so 10^4 * 10^5 = 10^9
        int curSubtreeSum = cur.val + leftSum + rightSum;

        sumAndFreq.compute(curSubtreeSum, (notUsedKey, freq) -> freq == null ? 1 : freq + 1);

        return curSubtreeSum;
    }

    private List<Integer> findMostFrequentKeys(Map<Integer, Integer> sumAndFreq) {
        assert sumAndFreq != null : "null 'sumAndFreq' detected";
        List<Integer> mostFreqKeys = new ArrayList<>();
        int maxFreq = 0;

        for (Map.Entry<Integer, Integer> entry : sumAndFreq.entrySet()) {
            if (entry.getValue() > maxFreq) {
                maxFreq = entry.getValue();
                mostFreqKeys.clear();
                mostFreqKeys.add(entry.getKey());
            } else if (entry.getValue() == maxFreq) {
                mostFreqKeys.add(entry.getKey());
            }
        }

        return mostFreqKeys;
    }

    private int[] toIntArray(List<Integer> list) {
        assert list != null;
        int[] res = new int[list.size()];

        int index = 0;
        for (int val : list) {
            res[index] = val;
            ++index;
        }

        return res;
    }

    // ===== Do not copy below code =======
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
