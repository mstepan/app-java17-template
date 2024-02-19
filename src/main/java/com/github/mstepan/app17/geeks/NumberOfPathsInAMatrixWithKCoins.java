package com.github.mstepan.app17.geeks;

/**
 * Number of paths in a matrix with k coins
 *
 * <p>https://www.geeksforgeeks.org/problems/number-of-paths-in-a-matrix-with-k-coins2728/1?utm_source=geeksforgeeks&utm_medium=newui_home&utm_campaign=potd
 */
public class NumberOfPathsInAMatrixWithKCoins {

    public static void main(String[] args) throws Exception {

        int k = 12;
        int n = 3;
        int[][] arr = {
            {1, 2, 3},
            {4, 6, 5},
            {3, 2, 1}
        };

        long res = numberOfPath(n, k, arr);

        System.out.printf("res: %d%n", res);

        System.out.println("NumberOfPathsInAMatrixWithKCoins done...");
    }

    /**
     * time: O(N^2 * K)
     *
     * <p>space: O(N^2 * K)
     *
     * <p>N = 100, K = 100
     *
     * <p>So, maxValue = 1M
     */
    static long numberOfPath(int n, int k, int[][] arr) {
        int[][][] sol = new int[n][n][k + 1];

        for (int curK = 0; curK <= k; ++curK) {

            sol[0][0][curK] = (arr[0][0] == curK) ? 1 : 0;

            for (int row = 0; row < n; ++row) {
                for (int col = 0; col < n; ++col) {
                    if (row == 0 && col == 0) {
                        continue;
                    }

                    if (arr[row][col] <= curK) {
                        sol[row][col][curK] =
                                (col == 0 ? 0 : sol[row][col - 1][curK - arr[row][col]])
                                        + (row == 0 ? 0 : sol[row - 1][col][curK - arr[row][col]]);
                    }
                }
            }
        }

        return sol[n - 1][n - 1][k];
    }
}
