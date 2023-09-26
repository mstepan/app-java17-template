package com.max.app17.leetcode.medium;

import java.util.Arrays;

/**
 * 1658. Minimum Operations to Reduce X to Zero
 * https://leetcode.com/problems/minimum-operations-to-reduce-x-to-zero/
 */
public class MinimumOperationsToReduceToZero {

    public static void main(String[] args) throws Exception {

        int[] nums = {
            6285, 2511, 3617, 8040, 6970, 8187, 5617, 7665, 5069, 875, 3570, 378, 6556, 1147, 8616,
            3140, 561, 2875, 5087, 1372, 2617, 756, 9076, 1381, 5428, 498, 1386, 6984, 5624, 7908,
            5724, 9921, 4368, 7036, 92, 4584, 2654, 2942, 9947, 9832, 9969, 9965, 9991, 9999, 10000,
            10000, 10000, 10000, 10000, 10000, 10000, 10000, 10000, 10000, 10000, 10000, 10000,
            10000, 10000, 10000, 10000, 10000, 10000, 10000, 10000, 10000, 10000, 10000, 10000,
            10000, 10000, 10000, 10000, 10000, 10000, 10000, 10000, 10000, 10000, 10000, 10000,
            10000, 10000, 10000, 10000, 9992, 10000, 9985, 8145, 8244, 4960, 7560, 7757, 3981, 8841,
            3482, 2188, 3475, 1594, 5101, 4404, 9679, 3500, 6984, 5108, 7258, 9696, 702, 9031, 2502,
            2326, 5099, 4767, 7164, 9432, 2084, 5294, 7382, 7564, 809
        };
        int x = 842_910;

        int res = new MinimumOperationsToReduceToZero().minOperations(nums, x);

        System.out.printf("res = %d%n", res);

        System.out.println("MinimumOperationsToReduceToZero done...");
    }

    /**
     * Time: O(N)
     * Space: O(1)
     */
    public int minOperations(int[] arr, int x) {

        int windowSum = Arrays.stream(arr).sum() - x;

        if (windowSum < 0) {
            return -1;
        }

        if (windowSum == 0) {
            return arr.length;
        }

        int from = 0;
        int to = 0;
        int curSum = arr[0];

        int maxWindowSize = -1;

        while (true) {
            if (curSum < windowSum) {
                // move 'right' pointer
                ++to;
                if (to >= arr.length) {
                    break;
                }
                curSum += arr[to];
            } else if (curSum > windowSum) {
                // move 'left' pointer
                curSum -= arr[from];
                ++from;
            } else {
                // find max windows size so far
                // and move 'right' pointer
                maxWindowSize = Math.max(maxWindowSize, to - from + 1);
                ++to;
                if (to >= arr.length) {
                    break;
                }
                curSum += arr[to];
            }
        }

        // no solution found
        if (maxWindowSize == -1) {
            return -1;
        }

        return arr.length - maxWindowSize;
    }
}
