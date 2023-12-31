package com.max.app17.leetcode.medium;


import java.util.BitSet;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.ThreadLocalRandom;

public class SearchA2dMatrix2Concurrent {

    private static final ForkJoinPool POOL = new ForkJoinPool();

    public static void main(String[] args) throws Exception {

        //        int[][] matrix = {
        //            {1, 4, 7, 11, 15},
        //            {2, 5, 8, 12, 19},
        //            {3, 6, 9, 16, 22},
        //            {10, 13, 14, 17, 24},
        //            {18, 21, 23, 26, 30}
        //        };

        final int size = 20_000;
        final int iterationsToCheck = 100;

        int[][] matrix = generateRandomMatrix(size);
        SubMatrix subMatrix = new SubMatrix(matrix, 0, 0, matrix.length - 1, matrix[0].length - 1);

        ThreadLocalRandom rand = ThreadLocalRandom.current();

        for(int it = 0; it < iterationsToCheck; ++it){

            int randRow = rand.nextInt(size);
            int randCol = rand.nextInt(size);

            int target = matrix[randRow][randCol];

            boolean found =
                POOL.invoke(new SubMatrixSearchTask(subMatrix, target)).booleanValue();

            if (!found) {
                throw new IllegalStateException("Not found, BUG.");
            }
        }


        BitSet notInMatrixValues = notInMatrix(matrix, size);

        for (int it = 0, cur = notInMatrixValues.nextSetBit(0);
             it < iterationsToCheck;
             ++it, cur = notInMatrixValues.nextSetBit(cur + 1)) {

            boolean found = POOL.invoke(new SubMatrixSearchTask(subMatrix, cur)).booleanValue();

            if (found) {
                throw new IllegalStateException("Not existed value found, BUG.");
            }
        }

        System.out.println("SearchA2dMatrix2Concurrent done...");
    }

    private static BitSet notInMatrix(int[][] matrix, int upperBoundary) {

        SubMatrix subMatrix = new SubMatrix(matrix, 0, 0, matrix.length - 1, matrix[0].length - 1);

        BitSet notInMatrix = new BitSet(upperBoundary);

        for (int i = 0; i < upperBoundary; ++i) {
            if (!searchSubMatrixSequentially(subMatrix, i)) {
                notInMatrix.set(i);
            }
        }

        return notInMatrix;
    }

    private static int[][] generateRandomMatrix(int size) {

        ThreadLocalRandom rand = ThreadLocalRandom.current();

        int[][] matrix = new int[size][size];

        for (int row = 0; row < size; ++row) {

            for (int col = 0; col < size; ++col) {

                int baseValue =
                    Math.max(
                        col == 0 ? 0 : matrix[row][col - 1],
                        row == 0 ? 0 : matrix[row - 1][col]);

                matrix[row][col] = baseValue + rand.nextInt(100);
            }
        }

        return matrix;
    }

    private static final class SubMatrixSearchTask extends RecursiveTask<Boolean> {

        private final SubMatrix subMatrix;
        private final int target;

        public SubMatrixSearchTask(SubMatrix subMatrix, int target) {
            this.subMatrix = subMatrix;
            this.target = target;
        }

        @Override
        protected Boolean compute() {
            if (subMatrix.elementsCount() < 1000) {
                return searchSubMatrixSequentially(subMatrix, target);
            }

            final int fromRow = subMatrix.fromRow;
            final int fromCol = subMatrix.fromCol;

            final int toRow = subMatrix.toRow;
            final int toCol = subMatrix.toCol;

            final int midRow = fromRow + (toRow - fromRow) / 2;
            final int midCol = fromCol + (toCol - fromCol) / 2;

            SubMatrix first = new SubMatrix(subMatrix.matrix, fromRow, fromCol, midRow, midCol);
            ForkJoinTask<Boolean> firstResult = new SubMatrixSearchTask(first, target).fork();

            SubMatrix second = new SubMatrix(subMatrix.matrix, fromRow, midCol + 1, midRow, toCol);
            ForkJoinTask<Boolean> secondResult = new SubMatrixSearchTask(second, target).fork();

            SubMatrix third = new SubMatrix(subMatrix.matrix, midRow + 1, fromCol, toRow, midCol);
            ForkJoinTask<Boolean> thirdResult = new SubMatrixSearchTask(third, target).fork();

            SubMatrix fourth =
                new SubMatrix(subMatrix.matrix, midRow + 1, midCol + 1, toRow, toCol);

            SubMatrixSearchTask fourthTask = new SubMatrixSearchTask(fourth, target);

            return fourthTask.compute()
                || firstResult.join()
                || secondResult.join()
                || thirdResult.join();
        }
    }

    record SubMatrix(int[][] matrix, int fromRow, int fromCol, int toRow, int toCol) {

        int elementsCount() {
            return (toRow - fromRow + 1) * (toCol - fromCol + 1);
        }

        @Override
        public String toString() {
            return "(%d, %d) ... (%d, %d)".formatted(fromRow, fromCol, toRow, toCol);
        }
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
    public static boolean searchSubMatrixSequentially(SubMatrix subMatrix, int target) {
        int row = subMatrix.fromRow;
        int col = subMatrix.toCol;

        while (row <= subMatrix.toRow && col >= subMatrix.fromCol) {
            if (subMatrix.matrix[row][col] == target) {
                return true;
            }

            if (subMatrix.matrix[row][col] < target) {
                ++row;
            } else {
                --col;
            }
        }

        return false;
    }

}
