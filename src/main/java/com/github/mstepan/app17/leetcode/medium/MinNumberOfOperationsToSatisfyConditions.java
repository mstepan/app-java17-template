package com.github.mstepan.app17.leetcode.medium;


/**
 * 3122. Minimum Number of Operations to Satisfy Conditions.
 *
 * <p>https://leetcode.com/problems/minimum-number-of-operations-to-satisfy-conditions/description/
 */
public class MinNumberOfOperationsToSatisfyConditions {

    public static void main(String[] args) {

        int[][] grid = {
            {1, 1, 3},
            {1, 2, 3},
            {1, 1, 4},
        };

        int minOpsCount = new MinNumberOfOperationsToSatisfyConditions().minimumOperations(grid);

        System.out.printf("minOpsCount: %d%n", minOpsCount);

        System.out.println("MinNumberOfOperationsToSatisfyConditions main done...");
    }

    /**
     * Use dynamic programming approach.
     *
     * <p>Time: O(N^2 + N*10) Space: O(N*10 + N)
     */
    public int minimumOperations(int[][] grid) {
        final int rows = grid.length;
        final int cols = grid[0].length;

        FreqTable[] freqTables = FreqTable.createFreqTables(grid);

        final int digitsCount = 10;

        int[][] solutions = new int[digitsCount][cols];

        for (int col = 0; col < cols; ++col) {
            for (int row = 0; row < solutions.length; ++row) {

                int curDigit = row;

                FreqTable curFreq = freqTables[col];

                int curChangeCost = rows - curFreq.count(curDigit);

                if (col == 0) {
                    solutions[row][col] = curChangeCost;
                } else {
                    int minPrevCost = Integer.MAX_VALUE;

                    for (int digit = 0; digit < 10; ++digit) {
                        if (digit != curDigit) {
                            minPrevCost = Math.min(minPrevCost, solutions[digit][col - 1]);
                        }
                    }
                    solutions[row][col] = curChangeCost + minPrevCost;
                }
            }
        }

        return minFromColumn(solutions, cols - 1);
    }

    private int minFromColumn(int[][] solutions, int columnIdx) {

        int minVal = Integer.MAX_VALUE;

        for (int row = 0; row < solutions.length; ++row) {
            minVal = Math.min(minVal, solutions[row][columnIdx]);
        }

        return minVal;
    }

    private static final class FreqTable {

        static FreqTable[] createFreqTables(int[][] grid) {
            final int rows = grid.length;
            final int cols = grid[0].length;

            FreqTable[] freqTables = new FreqTable[cols];
            for (int i = 0; i < freqTables.length; ++i) {
                freqTables[i] = new FreqTable();
            }

            for (int row = 0; row < rows; ++row) {
                for (int col = 0; col < cols; ++col) {
                    freqTables[col].add(grid[row][col]);
                }
            }

            return freqTables;
        }

        private final int[] freqCounters = new int[10];

        void add(int digit) {
            assert digit >= 0 && digit < 10;
            freqCounters[digit] += 1;
        }

        int count(int digit) {
            assert digit >= 0 && digit < 10;
            return freqCounters[digit];
        }
    }
}
