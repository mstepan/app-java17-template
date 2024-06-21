package com.github.mstepan.app17.leetcode.medium;

import java.util.Objects;

/**
 * 2906. Construct Product Matrix
 *
 * <p>https://leetcode.com/problems/construct-product-matrix/
 */
public class ConstructProductMatrix {

    private static final int MOD = 12345;

    /**
     * Time: O(N*M)
     *
     * <p>Space: O(N*M)
     */
    public int[][] constructProductMatrix(int[][] grid) {
        Objects.requireNonNull(grid);
        if (grid[0] == null) {
            throw new IllegalArgumentException("grid[0] is null");
        }

        final int rows = grid.length;
        final int cols = grid[0].length;

        int[][] suffixProdGrid = new int[rows][cols];

        long suffixProd = 1L;

        for (int row = rows - 1; row >= 0; --row) {
            for (int col = cols - 1; col >= 0; --col) {
                suffixProd = (suffixProd * grid[row][col]) % MOD;
                suffixProdGrid[row][col] = (int)suffixProd;
            }
        }

        int[][] res = new int[rows][cols];
        long prefixProd = 1L;

        for (int row = 0; row < rows; ++row) {
            for (int col = 0; col < cols; ++col) {

                long curProd = (prefixProd * nextSuffix(suffixProdGrid, row, col)) % MOD;
                res[row][col] = (int)curProd;
                prefixProd = (prefixProd * grid[row][col]) % MOD;
            }
        }

        return res;
    }

    private int nextSuffix(int[][] suffixProdGrid, int curRow, int curCol) {

        final int rows = suffixProdGrid.length;
        final int cols = suffixProdGrid[0].length;

        int nextRow = curRow;
        int nextCol = curCol + 1;

        if (nextCol == cols) {
            nextRow += 1;
            nextCol = 0;
        }

        if (nextRow >= rows) {
            return 1;
        }

        return suffixProdGrid[nextRow][nextCol];
    }
}
