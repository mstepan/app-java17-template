package com.max.app17.leetcode.hard;

import java.util.Arrays;

/** 1478. Allocate Mailboxes https://leetcode.com/problems/allocate-mailboxes/ */
public class AllocateMailboxes {

    public static void main(String[] args) throws Exception {

        final int[] houses = {8, 14, 20, 23, 4, 25};
        final int k = 3;

        // expected = 9
        int res = new AllocateMailboxes().minDistance(houses, k);

        System.out.printf("res = %d%n", res);

        System.out.println("AllocateMailboxes done...");
    }

    /*
     * Uses top-down dynamic programming approach.
     * Time: O(N*lgN + N^2*K) ~ O(N^2*K)
     * Space: O(N*K), but can be reduced to O(N)
     */
    public int minDistance(int[] houses, int kVal) {

        // we need to sort first, otherwise we can't quickly calculate min distance for case when k
        // = 1
        Arrays.sort(houses);

        final int rows = kVal + 1;
        final int cols = houses.length;

        int[][] sol = new int[rows][cols];

        for (int i = 0; i < cols; ++i) {
            sol[1][i] = calcDistanceForSubarray(houses, 0, i);
        }

        for (int k = 2; k < rows; ++k) {
            for (int i = 0; i < cols; ++i) {
                if (k - 1 > i) {
                    // -1 means no solution exist
                    sol[k][i] = -1;
                } else if (k - 1 == i) {
                    sol[k][i] = 0;
                } else {
                    int curBest = Integer.MAX_VALUE;

                    for (int j = i - 1; j >= 0; --j) {
                        if (sol[k - 1][j] >= 0) {
                            curBest =
                                    Math.min(
                                            curBest,
                                            sol[k - 1][j]
                                                    + calcDistanceForSubarray(houses, j + 1, i));
                        }
                    }

                    sol[k][i] = (curBest == Integer.MAX_VALUE) ? -1 : curBest;
                }
            }
        }

        return sol[rows - 1][cols - 1];
    }

    private int calcDistanceForSubarray(int[] houses, int from, int to) {

        int elemsCount = to - from + 1;
        int mid = from + (to - from) / 2;

        if ((elemsCount & 1) == 0) {
            // even case
            int median = (houses[mid] + houses[mid + 1]) / 2;
            return distanceTo(houses, from, to, median);
        } else {
            // odd case
            return distanceTo(houses, from, to, houses[mid]);
        }
    }

    private int distanceTo(int[] arr, int from, int to, int baseValue) {
        int distance = 0;

        for (int i = from; i <= to; ++i) {
            distance += Math.abs(arr[i] - baseValue);
        }

        return distance;
    }
}
