package com.max.app17.leetcode.hard;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.Objects;

/**
 * 239. Sliding Window Maximum
 * https://leetcode.com/problems/sliding-window-maximum/
 */
public class SlidingWindowMaximum {

    public static void main(String[] args) {

        int[] nums = {1, 3, -1, -3, 5, 3, 6, 7};
        int k = 3;

        int[] res = new SlidingWindowMaximum().maxSlidingWindow(nums, k);

        System.out.println(Arrays.toString(res));

        System.out.println("SlidingWindowMaximum done...");
    }

    /** time: O(N) space: O(K) */
    public int[] maxSlidingWindow(int[] arr, int k) {
        Objects.requireNonNull(arr);
        if (k < 0) {
            throw new IllegalArgumentException("k < 0 should be positive or zero, k = " + k);
        }

        if (k == 0) {
            return new int[] {};
        }

        if (k >= arr.length) {
            assert arr.length > 0 : "can't get max value from empty 'arr'";
            return new int[] {Arrays.stream(arr).max().getAsInt()};
        }

        int[] maxSliding = new int[arr.length - k + 1];

        Deque<Integer> sortedSeq = new ArrayDeque<>();

        for (int i = 0; i < k; ++i) {
            addToSortedSeq(sortedSeq, arr[i]);
        }

        assert !sortedSeq.isEmpty() : "empty 'sortedSeq' detected";
        maxSliding[0] = sortedSeq.peekFirst();

        for (int j = k, idx = 1; j < arr.length; ++j, ++idx) {
            int remVal = arr[j - k];

            if (sortedSeq.peekFirst() == remVal) {
                sortedSeq.removeFirst();
            }

            addToSortedSeq(sortedSeq, arr[j]);

            assert !sortedSeq.isEmpty() : "empty 'sortedSeq' detected";
            maxSliding[idx] = sortedSeq.peekFirst();
        }

        return maxSliding;
    }

    private void addToSortedSeq(Deque<Integer> sortedSeq, int val) {
        while (!sortedSeq.isEmpty() && sortedSeq.peekLast() < val) {
            sortedSeq.pollLast();
        }
        sortedSeq.addLast(val);
    }
}
