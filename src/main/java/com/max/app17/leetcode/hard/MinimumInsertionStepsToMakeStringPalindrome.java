package com.max.app17.leetcode.hard;

import java.util.Objects;

public class MinimumInsertionStepsToMakeStringPalindrome {

    public static void main(String[] args) {
        String[] allStrs = {"zzazz", "mbadm", "leetcode"};

        for (String str : allStrs) {
            System.out.printf("'%s': %d%n", str, minInsertions(str));
        }

        System.out.println("MinimumInsertionStepsToMakeStringPalindrome done...");
    }

    /** time: O(N^2) space: O(N^2) can be reduced to O(N) */
    public static int minInsertions(String s) {
        Objects.requireNonNull(s);

        final int n = s.length();
        int[][] sol = new int[n][n];

        for (int row = n - 2; row >= 0; --row) {
            for (int col = row + 1; col < n; ++col) {
                if (s.charAt(row) == s.charAt(col)) {
                    sol[row][col] = sol[row + 1][col - 1];
                } else {
                    sol[row][col] = 1 + Math.min(sol[row + 1][col], sol[row][col - 1]);
                }
            }
        }

        return sol[0][n - 1];
    }
}
