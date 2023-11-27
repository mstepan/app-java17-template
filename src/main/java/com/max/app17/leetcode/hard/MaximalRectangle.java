package com.max.app17.leetcode.hard;

import com.max.app17.Main;

import java.util.Objects;

/*
85. Maximal Rectangle

https://leetcode.com/problems/maximal-rectangle/
 */
public class MaximalRectangle {

    public static void main(String[] args) throws Exception {

        // expected = 6
        char[][] m = {
            {'1', '0', '1', '1', '1'},
            {'0', '1', '0', '1', '0'},
            {'1', '1', '0', '1', '1'},
            {'1', '1', '0', '1', '1'},
            {'0', '1', '1', '1', '1'}
        };

        int largestRecArea = new MaximalRectangle().maximalRectangle(m);

        System.out.printf("largestRecArea: %d%n", largestRecArea);

        System.out.println("MaximalRectangle done...");
    }

    /*
    time: O(N*M*N) ~ O(N^2*M) ~ O(N^3) if N == M
    space: O(N*M) ~ O(N^2) if N == M
     */
    public int maximalRectangle(char[][] matrix) {
        Objects.requireNonNull(matrix);

        final int rows = matrix.length;
        final int cols = matrix[0].length;

        int[][] prefixSum = new int[rows][cols];

        for (int row = 0; row < rows; ++row) {
            for (int col = 0; col < cols; ++col) {

                if (matrix[row][col] == '1') {
                    int cnt = 1;

                    if (col != 0) {
                        cnt += matrix[row][col - 1] == '1' ? prefixSum[row][col - 1] : 0;
                    }

                    prefixSum[row][col] = cnt;
                }
            }
        }

        int maxArea = 0;

        for (int row = 0; row < rows; ++row) {
            for (int col = 0; col < cols; ++col) {

                if (prefixSum[row][col] != 0) {

                    int curPrefixValue = prefixSum[row][col];
                    maxArea = Math.max(maxArea, curPrefixValue);

                    for (int prevRow = row - 1; prevRow >= 0; --prevRow) {
                        if (prefixSum[prevRow][col] == 0) {
                            break;
                        }

                        curPrefixValue = Math.min(curPrefixValue, prefixSum[prevRow][col]);

                        int curArea = (row - prevRow + 1) * curPrefixValue;

                        maxArea = Math.max(maxArea, curArea);
                    }
                }
            }
        }

        return maxArea;
    }
}
