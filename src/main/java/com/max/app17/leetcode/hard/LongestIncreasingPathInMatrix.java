package com.max.app17.leetcode.hard;

import java.util.Arrays;

/*
329. Longest Increasing Path in a Matrix
https://leetcode.com/problems/longest-increasing-path-in-a-matrix/
 */
public class LongestIncreasingPathInMatrix {


    public static void main(String[] args) throws Exception {

        int[][] m = {
                {0, 1, 2, 3, 4, 5, 6, 7, 8, 9},
                {19, 18, 17, 16, 15, 14, 13, 12, 11, 10},
                {20, 21, 22, 23, 24, 25, 26, 27, 28, 29},
                {39, 38, 37, 36, 35, 34, 33, 32, 31, 30},
                {40, 41, 42, 43, 44, 45, 46, 47, 48, 49},
                {59, 58, 57, 56, 55, 54, 53, 52, 51, 50},
                {60, 61, 62, 63, 64, 65, 66, 67, 68, 69},
                {79, 78, 77, 76, 75, 74, 73, 72, 71, 70},
                {80, 81, 82, 83, 84, 85, 86, 87, 88, 89},
                {99, 98, 97, 96, 95, 94, 93, 92, 91, 90},
                {100, 101, 102, 103, 104, 105, 106, 107, 108, 109},
                {119, 118, 117, 116, 115, 114, 113, 112, 111, 110},
                {120, 121, 122, 123, 124, 125, 126, 127, 128, 129},
                {139, 138, 137, 136, 135, 134, 133, 132, 131, 130},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0}
        };

        //        int[][] m = {
        //            {3, 4, 5},
        //            {3, 2, 6},
        //            {2, 2, 1}
        //        };

        System.out.printf("longestIncreasingPath: %d%n", longestIncreasingPath(m));

        System.out.println("LongestIncreasingPathInMatrix done...");
    }

    /*
    NxM = matrix size
    time: O(N^2*M^2)
    space: O(N*M)

    Use algorithm similar to Bellman-Ford.
    Iterate N*M times over all cells and try to see if any better path can be constructed.
    If we can't construct better path (path with bigger length) we just terminate algorithm.
     */
    public static int longestIncreasingPath(int[][] m) {
        final int rows = m.length;
        final int cols = m[0].length;

        int[][] longestPath = new int[rows][cols];

        for (int[] curRow : longestPath) {
            Arrays.fill(curRow, 1);
        }

        for (int it = 0; it < rows * cols; ++it) {
            int[][] newLongestPath = relaxExistingPaths(m, longestPath);
            if (newLongestPath == null) {
                break;
            }

            longestPath = newLongestPath;
            //            printMatrix(longestPath);
            //            System.out.println();
        }

        return findMax(longestPath);
    }

    private static void printMatrix(int[][] m) {
        for (int[] curRow : m) {
            System.out.println(Arrays.toString(curRow));
        }
    }

    private static int findMax(int[][] longestPath) {

        int maxVal = -1;

        for (int[] curRow : longestPath) {
            for (int val : curRow) {
                maxVal = Math.max(maxVal, val);
            }
        }

        return maxVal;
    }

    private static final int[] OFFSETS = {
            1, 0,
            0, -1,
            -1, 0,
            0, 1
    };

    private static int[][] relaxExistingPaths(int[][] m, int[][] prevLongestPath) {

        final int rows = m.length;
        final int cols = m[0].length;

        boolean wasRelaxed = false;

        int[][] newLongestPath = copy(prevLongestPath);

        for (int row = 0; row < rows; ++row) {
            for (int col = 0; col < cols; ++col) {

                final int curVal = m[row][col];

                for (int i = 1; i < OFFSETS.length; i += 2) {
                    int rowOffset = OFFSETS[i - 1];
                    int colOffset = OFFSETS[i];

                    final int newRow = row + rowOffset;
                    final int newCol = col + colOffset;

                    if (newRow < 0 || newRow >= rows || newCol < 0 || newCol >= cols) {
                        continue;
                    }

                    final int newVal = m[newRow][newCol];

                    if (newVal < curVal) {
                        int newPathLength = prevLongestPath[newRow][newCol] + 1;

                        if (newPathLength > newLongestPath[row][col]) {
                            newLongestPath[row][col] = newPathLength;
                            wasRelaxed = true;
                        }
                    }
                }
            }
        }

        if (wasRelaxed) {
            return newLongestPath;
        }

        return null;
    }

    private static int[][] copy(int[][] m) {

        int[][] copy = new int[m.length][m[0].length];

        for (int row = 0; row < m.length; ++row) {
            for (int col = 0; col < m[0].length; ++col) {
                copy[row][col] = m[row][col];
            }
        }

        return copy;
    }

}
