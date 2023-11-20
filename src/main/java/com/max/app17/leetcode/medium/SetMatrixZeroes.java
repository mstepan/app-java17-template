package com.max.app17.leetcode.medium;


import java.util.BitSet;
import java.util.Objects;

/**
 * 73. Set Matrix Zeroes
 *
 * <p>https://leetcode.com/problems/set-matrix-zeroes/
 */
public class SetMatrixZeroes {

    public static void main(String[] args) {
        int[][] m = {
            {0, 1, 2, 0},
            {3, 4, 5, 2},
            {1, 3, 1, 5}
        };

        new SetMatrixZeroes().setZeroes(m);

        System.out.println("SetMatrixZeroes done...");
    }

    public void setZeroes(int[][] m) {
        Objects.requireNonNull(m);

        final int rows = m.length;
        final int cols = m[0].length;

        BitSet zeroRows = new BitSet(rows);
        BitSet zeroCols = new BitSet(cols);

        for (int row = 0; row < rows; ++row) {
            for (int col = 0; col < cols; ++col) {
                if (m[row][col] == 0) {
                    zeroRows.set(row);
                    zeroCols.set(col);
                }
            }
        }

        for (int row = 0; row < rows; ++row) {
            for (int col = 0; col < cols; ++col) {
                if (zeroRows.get(row) || zeroCols.get(col)) {
                    m[row][col] = 0;
                }
            }
        }
    }
}
