package com.github.mstepan.app17.leetcode.medium;

import java.util.Objects;

/**
 * 583. Delete Operation for Two Strings
 *
 * <p>https://leetcode.com/problems/delete-operation-for-two-strings/description/
 */
public class DeleteOperationForTwoStrings {

    public static void main(String[] args) {

        String s1 = "leetcode";
        String s2 = "etco";

        int minDistance = new DeleteOperationForTwoStrings().minDistance(s1, s2);

        System.out.printf("minDistance: %d%n", minDistance);

        System.out.println("DeleteOperationForTwoStrings done...");
    }

    /**
     * time: O(N*M)
     *
     * <p>space: O(N*M), can be reduced to O(min(N, M))
     */
    public int minDistance(String word1, String word2) {
        Objects.requireNonNull(word1);
        Objects.requireNonNull(word2);

        return word1.length() + word2.length() - 2 * lcs(word1, word2);
    }

    private int lcs(String word1, String word2) {

        final int rows = word1.length() + 1;
        final int cols = word2.length() + 1;

        int[][] sol = new int[rows][cols];

        for (int row = 1; row < rows; ++row) {
            for (int col = 1; col < cols; ++col) {
                char ch1 = word1.charAt(row - 1);
                char ch2 = word2.charAt(col - 1);

                if (ch1 == ch2) {
                    sol[row][col] = 1 + sol[row - 1][col - 1];
                } else {
                    sol[row][col] = Math.max(sol[row - 1][col], sol[row][col - 1]);
                }
            }
        }

        return sol[rows - 1][cols - 1];
    }
}
