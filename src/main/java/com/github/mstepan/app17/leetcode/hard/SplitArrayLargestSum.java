package com.max.app17.leetcode.hard;

/**
 * 410. Split Array Largest Sum
 *
 * <p>https://leetcode.com/problems/split-array-largest-sum/
 */
public class SplitArrayLargestSum {

    public static void main(String[] args) throws Exception {

        final int k = 2;
        final int[] arr = new int[] {7, 2, 5, 10, 8};

        int res = new SplitArrayLargestSum().splitArray(arr, k);

        System.out.printf("res: %d%n", res);

        System.out.println("SplitArrayLargestSum done...");
    }

    /** time: O(N^2 * K) space: O(N) */
    public int splitArray(int[] arr, int k) {

        int[] prev = prefixSum(arr);

        for (int curK = 2; curK <= k; ++curK) {

            int[] cur = new int[arr.length];

            for (int i = 0; i + 1 < curK; ++i) {
                cur[i] = Integer.MAX_VALUE;
            }

            for (int i = curK - 1; i < arr.length; ++i) {
                int curSum = arr[i];

                int bestSoFar = Integer.MAX_VALUE;

                for (int j = i - 1; j >= 0; --j) {
                    bestSoFar = Math.min(bestSoFar, Math.max(prev[j], curSum));
                    curSum += arr[j];
                }

                cur[i] = bestSoFar;
            }

            prev = cur;
        }

        return prev[arr.length - 1];
    }

    private int[] prefixSum(int[] arr) {

        int[] res = new int[arr.length];
        res[0] = arr[0];

        for (int i = 1; i < res.length; ++i) {
            res[i] = arr[i] + res[i - 1];
        }

        return res;
    }
}
