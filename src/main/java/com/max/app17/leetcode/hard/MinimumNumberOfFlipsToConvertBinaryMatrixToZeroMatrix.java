package com.max.app17.leetcode.hard;

import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Objects;
import java.util.Queue;
import java.util.Set;

/**
 * 1284. Minimum Number of Flips to Convert Binary Matrix to Zero Matrix
 *
 * <p>https://leetcode.com/problems/minimum-number-of-flips-to-convert-binary-matrix-to-zero-matrix/
 */
public class MinimumNumberOfFlipsToConvertBinaryMatrixToZeroMatrix {

    public static void main(String[] args) throws Exception {

        int[][] mat = {
            {0, 0},
            {0, 1}
        };

        //        int[][] mat = {
        //            {1, 0, 0},
        //            {1, 0, 0}
        //        };

        int flipsCount = new MinimumNumberOfFlipsToConvertBinaryMatrixToZeroMatrix().minFlips(mat);

        System.out.printf("flipsCount: %d%n", flipsCount);

        System.out.println("MinimumNumberOfFlipsToConvertBinaryMatrixToZeroMatrix done...");
    }

    /**
     * Use BFS algorithm to find the shortest path from current matrix to matrix with all zeros.
     *
     * <p>K = N*M, max K = 3*3 = 9
     *
     * <p>time: O(2^K) = 512
     *
     * <p>space: O(2^K) = 512
     */
    public int minFlips(int[][] mat) {
        Objects.requireNonNull(mat);

        CompactMatrix start = new CompactMatrix(mat);

        Set<CompactMatrix> alreadyProcessed = new HashSet<>();
        alreadyProcessed.add(start);

        Queue<SolutionPath> queue = new ArrayDeque<>();
        queue.add(new SolutionPath(start, 0));

        while (!queue.isEmpty()) {

            SolutionPath path = queue.poll();

            //            System.out.printf("path count: %d%n", path.cnt);
            //
            //            printMatrix(path.matrix().toMatrix());

            if (path.matrix.hasAllZeros()) {
                return path.cnt();
            }

            CompactMatrix compactMatrix = path.matrix();
            int[][] pathMatrix = path.matrix.toMatrix();

            for (int row = 0; row < compactMatrix.rows; ++row) {
                for (int col = 0; col < compactMatrix.cols; ++col) {
                    flipCell(pathMatrix, row, col);

                    CompactMatrix candidate = new CompactMatrix(pathMatrix);

                    if (!alreadyProcessed.contains(candidate)) {
                        queue.add(new SolutionPath(candidate, path.cnt() + 1));
                        alreadyProcessed.add(candidate);
                    }

                    flipCell(pathMatrix, row, col);
                }
            }
        }

        return -1;
    }

    private static void printMatrix(int[][] matrix) {

        StringBuilder buf = new StringBuilder();

        for (int row = 0; row < matrix.length; ++row) {

            if (row != 0) {
                buf.append(System.getProperty("line.separator"));
            }

            for (int col = 0; col < matrix[row].length; ++col) {
                if (col != 0) {
                    buf.append(", ");
                }
                buf.append(matrix[row][col]);
            }
        }

        System.out.println(buf.toString());
    }

    private void flipCell(int[][] matrix, int row, int col) {
        // flip current cell value
        matrix[row][col] ^= 1;

        // up
        if (row != 0) {
            matrix[row - 1][col] ^= 1;
        }

        // down
        if (row != matrix.length - 1) {
            matrix[row + 1][col] ^= 1;
        }

        // left
        if (col != 0) {
            matrix[row][col - 1] ^= 1;
        }

        // right
        if (col != matrix[0].length - 1) {
            matrix[row][col + 1] ^= 1;
        }
    }

    record SolutionPath(CompactMatrix matrix, int cnt) {}

    /** Represent matrix in compact form using single 'int' value. max(N*M) = 3*3 = 9 */
    static class CompactMatrix {

        /*
        Matrix:
         | 1 1 |
         | 0 0 |
         Encoded: 1100 (MSB ... LSB)
         */
        final int state;
        final int rows;
        final int cols;

        CompactMatrix(int[][] matrix) {
            this.state = encode(matrix);
            this.rows = matrix.length;
            this.cols = matrix[0].length;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            CompactMatrix that = (CompactMatrix) o;

            if (state != that.state) return false;
            if (rows != that.rows) return false;
            return cols == that.cols;
        }

        @Override
        public int hashCode() {
            int result = state;
            result = 31 * result + rows;
            result = 31 * result + cols;
            return result;
        }

        int[][] toMatrix() {
            int[][] matrix = new int[rows][cols];

            int curState = state;

            for (int row = 0; row < rows; ++row) {
                for (int col = 0; col < cols; ++col) {
                    matrix[row][col] = curState & 1;
                    curState >>= 1;
                }
            }

            return matrix;
        }

        private int encode(int[][] matrix) {
            int encodedState = 0;

            final int rows = matrix.length;
            final int cols = matrix[0].length;

            for (int row = 0; row < rows; ++row) {
                for (int col = 0; col < cols; ++col) {
                    assert matrix[row][col] == 0 || matrix[row][col] == 1;

                    encodedState = (encodedState << 1) | matrix[row][col];
                }
            }

            return reverseBits(encodedState, rows * cols);
        }

        private static int reverseBits(int value, int bitsCount) {

            int reversedValue = 0;

            for (int i = 0; i < bitsCount; ++i) {
                reversedValue = (reversedValue << 1) | (value & 1);
                value >>= 1;
            }

            return reversedValue;
        }

        public boolean hasAllZeros() {
            return state == 0;
        }
    }
}
