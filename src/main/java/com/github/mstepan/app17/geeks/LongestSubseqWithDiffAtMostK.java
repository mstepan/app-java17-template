package com.github.mstepan.app17.geeks;

import java.util.Arrays;
import java.util.Objects;

public class LongestSubseqWithDiffAtMostK {

    /**
     * https://www.geeksforgeeks.org/longest-subsequence-with-difference-between-max-and-min-at-most-k/
     *
     * <p>Longest Subsequence with difference between max and min at most K.
     *
     * <p>Given an array arr[] of size N and a non-negative integer K, the task is to find the
     * length of the longest subsequence such that the difference between the maximum and the
     * minimum of the subsequence is at most K.
     */
    public static void main(String[] args) {

        final int[] arr = {12, 3, 5, 9, 7};
        final int k = 8;

        final int maxLength = maxSubseqLengthWithDiff(arr, k);

        System.out.println(maxLength);

        System.out.printf("Main done... java version: %s%n", System.getProperty("java.version"));
    }

    /** time: O(N*lgN) space: O(1) */
    static int maxSubseqLengthWithDiff(int[] arr, int maxDiff) {
        Objects.requireNonNull(arr, "null 'arr' parameter");
        checkArgument(maxDiff >= 0, "'maxDiff' should be positive or zero, but found " + maxDiff);

        if (maxDiff == 0) {
            return (arr.length == 0) ? 0 : 1;
        }

        // time: O(N*lgN)
        // space: O(1), b/c primitive array will use quicksort under the hood
        Arrays.sort(arr);

        int left = 0;
        int right = 0;
        int maxLength = 0;

        // time: O(N)
        while (right < arr.length) {
            assert left <= right : "left > right, detected";

            final long curDiff = Math.abs((long) arr[right] - arr[left]);

            // overflow:
            // arr = {-1, Integer.MAX_VALUE}, K = 10
            //            final int curDiff = Math.abs(arr[right] - arr[left]);

            if (curDiff <= maxDiff) {
                maxLength = Math.max(maxLength, right - left + 1);
                ++right;
            } else {
                ++left;
            }
        }

        return maxLength;
    }

    private static void checkArgument(boolean predicate, String errorMsg) {
        if (!predicate) {
            throw new IllegalArgumentException(errorMsg);
        }
    }
}
