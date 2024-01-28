package com.max.app17.leetcode.medium;


/**
 * 96. Unique Binary Search Trees
 * https://leetcode.com/problems/unique-binary-search-trees/description/
 */
public class UniqueBinarySearchTrees {

    public static void main(String[] args) throws Exception {

        System.out.println(new UniqueBinarySearchTrees().numTrees(19));

        System.out.println("UniqueBinarySearchTrees done...");
    }

    private static final long[] COMPUTED_RESULTS = new long[20];

    static {
        COMPUTED_RESULTS[0] = 1L;
        COMPUTED_RESULTS[1] = 1L;

        for (int i = 2; i < COMPUTED_RESULTS.length; ++i) {
            COMPUTED_RESULTS[i] = (2L * (2L * i - 1L) * COMPUTED_RESULTS[i - 1]) / (i + 1L);
        }
    }

    /**
     * Number of unique binary trees is equal to Catalan's number
     * https://en.wikipedia.org/wiki/Catalan_number time: O(1) space: O(1)
     */
    public int numTrees(int n) {
        if (n < 0 || n > COMPUTED_RESULTS.length) {
            throw new IllegalArgumentException(
                    "'n' is not in range n = %d but should be in range [0; %d]"
                            .formatted(n, COMPUTED_RESULTS.length - 1));
        }
        return (int) COMPUTED_RESULTS[n];
    }
}
