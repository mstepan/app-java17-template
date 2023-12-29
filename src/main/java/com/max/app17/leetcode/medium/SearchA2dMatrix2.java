package com.max.app17.leetcode.medium;

import com.max.app17.Main;

import java.util.Objects;

/**
 * https://leetcode.com/problems/search-a-2d-matrix-ii/
 *
 * <p>240. Search a 2D Matrix II
 */
public final class SearchA2dMatrix2 {

    public static void main(String[] args) throws Exception {
        int[][] matrix = {
            {1, 4, 7, 11, 16},
            {2, 5, 8, 12, 19},
            {3, 6, 9, 15, 22},
            {10, 13, 14, 17, 24},
            {18, 21, 23, 26, 30}
        };
        int target = 15;

        boolean found = new SearchA2dMatrix2().searchMatrix(matrix, target);

        System.out.printf("found: %b%n", found);

        System.out.println("SearchA2dMatrix2 done...");
    }

    /**
     * N = matrix.length
     *
     * <p>M = matrix[row].length
     *
     * <p>time: O(N+M)
     *
     * <p>space: O(1)
     */
    public boolean searchMatrix(int[][] matrix, int target) {
        Objects.requireNonNull(matrix);
        int row = 0;
        int col = matrix[row].length - 1;

        while (row < matrix.length && col >= 0) {
            if (matrix[row][col] == target) {
                return true;
            }

            if (matrix[row][col] < target) {
                ++row;
            } else {
                --col;
            }
        }

        return false;
    }
}
