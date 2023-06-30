package com.max.app17.leetcode.hard;

/**
 * <a href="https://leetcode.com/problems/distinct-subsequences/">115. Distinct Subsequences</a>
 */
public class DistinctSubsequences {

    public static void main(String[] args) throws Exception {

        String str = "BABGBAG";
        String t = "BAG";

        System.out.println(numDistinct(str, t));

        System.out.println("DistinctSubsequences done...");
    }

    /**
     * N = s.length() M = t.length() time: O(N*M) space: O(N*M), can be optimized to O(2N) ~ O(N)
     */
    public static int numDistinct(String s, String t) {
        if (s.length() < t.length()) {
            return 0;
        }

        if (s.length() == t.length()) {
            return s.equals(t) ? 1 : 0;
        }

        final int rows = t.length() + 1;
        final int cols = s.length() + 1;
        int[][] sol = new int[rows][cols];

        // fill 1-st row with '1'
        for (int col = 0; col < cols; ++col) {
            sol[0][col] = 1;
        }

        for (int row = 1; row < rows; ++row) {
            for (int col = 1; col < cols; ++col) {
                if (s.charAt(col - 1) == t.charAt(row - 1)) {
                    sol[row][col] = sol[row - 1][col - 1] + sol[row][col - 1];
                } else {
                    sol[row][col] = sol[row][col - 1];
                }
            }
        }

        return sol[rows - 1][cols - 1];
    }
}
