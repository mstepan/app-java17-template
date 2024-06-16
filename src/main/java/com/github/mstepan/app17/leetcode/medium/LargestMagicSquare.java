package com.github.mstepan.app17.leetcode.medium;

import java.util.Arrays;
import java.util.Objects;

/** 1895. Largest Magic Square https://leetcode.com/problems/largest-magic-square/description/ */
public class LargestMagicSquare {

    /** time: O(N^2*M^2) space: O(1) */
    public int largestMagicSquare(int[][] grid) {
        Objects.requireNonNull(grid);
        final int rows = grid.length;
        if (rows == 0) {
            return 0;
        }
        Objects.requireNonNull(grid[0]);
        final int cols = grid[0].length;
        if (cols == 0) {
            return 0;
        }

        int largest = 1;
        for (int row = 0; row < rows; ++row) {
            for (int col = 0; col < cols; ++col) {

                for (int size = 2; (row + (size - 1) < rows && col + (size - 1) < cols); ++size) {
                    if (isMagicSquare(grid, row, col, size)) {
                        largest = Math.max(largest, size);
                    }
                }
            }
        }

        return largest;
    }

    private boolean isMagicSquare(int[][] grid, int row, int col, int size) {

        final int[] rowsSum = new int[size];
        final int[] colsSum = new int[size];

        int diagonalSum = 0;
        int revDiagonalSum = 0;

        for (int curRow = row; curRow < (row + size); ++curRow) {
            for (int curCol = col; curCol < (col + size); ++curCol) {
                final int val = grid[curRow][curCol];
                rowsSum[curRow - row] += val;
                colsSum[curCol - col] += val;

                if (isDiagonal(row, col, curRow, curCol)) {
                    diagonalSum += val;
                }

                if (isReverseDiagonal(row, col + size - 1, curRow, curCol)) {
                    revDiagonalSum += val;
                }
            }
        }

        if (diagonalSum != revDiagonalSum) {
            return false;
        }

        final int expected = diagonalSum;

        for (int rowSum : rowsSum) {
            if (rowSum != expected) {
                return false;
            }
        }

        for (int colSum : colsSum) {
            if (colSum != expected) {
                return false;
            }
        }

        return true;
    }

    private boolean isReverseDiagonal(int baseRow, int baseCol, int row, int col) {
        return row - baseRow == -(col - baseCol);
    }

    private boolean isDiagonal(int baseRow, int baseCol, int row, int col) {
        return row - baseRow == col - baseCol;
    }
}
