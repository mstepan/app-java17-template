package com.max.app17.leetcode.medium;

import java.util.Objects;

/**
 * 2875. Minimum Size Subarray in Infinite Array
 * https://leetcode.com/problems/minimum-size-subarray-in-infinite-array/
 */
public class MinimumSizeSubarrayInInfiniteArray {

    public static void main(String[] args) throws Exception {

        int[] arr = {5, 5, 4, 1, 2, 2, 2, 3, 2, 4, 2, 5};
        int target = 56;

        //        int[] arr = {1, 1, 1};
        //        int target = 1_000_000_000;

        int minSize = new MinimumSizeSubarrayInInfiniteArray().minSizeSubarray(arr, target);

        System.out.printf("minSize: %d%n", minSize);

        System.out.println("MinimumSizeSubarrayInInfiniteArray done...");
    }

    /** N = arr.length K = target value time: O(N + K) space: O(1) */
    public int minSizeSubarray(int[] arr, int target) {
        Objects.requireNonNull(arr);

        long totalSum = sum(arr);

        if (allValuesAreSame(arr)) {
            if (target % arr[0] != 0) {
                return -1;
            }

            return target / arr[0];
        }

        int from = 0;
        int to = 0;
        long windowSum = arr[0];

        int boundary = 2 * arr.length;

        if (totalSum < target) {
            boundary = (int) ((target / totalSum) + 1) * arr.length * 2;
        }

        int smallestDistance = Integer.MAX_VALUE;

        while (true) {

            if (windowSum <= target) {
                if (windowSum == target) {
                    smallestDistance = Math.min(smallestDistance, to - from + 1);
                }

                ++to;
                if (to == boundary) {
                    break;
                }

                windowSum += arr[to % arr.length];
            } else {
                windowSum -= arr[from % arr.length];
                ++from;

                if (from > to) {
                    ++to;
                    if (to == boundary) {
                        break;
                    }

                    windowSum += arr[to % arr.length];
                }
            }
        }

        return smallestDistance == Integer.MAX_VALUE ? -1 : smallestDistance;
    }

    private boolean allValuesAreSame(int[] arr) {
        for (int i = 1; i < arr.length; ++i) {
            if (arr[i] != arr[i - 1]) {
                return false;
            }
        }

        return true;
    }

    private long sum(int[] arr) {

        long res = 0L;

        for (int val : arr) {
            res += val;
        }
        return res;
    }
}
