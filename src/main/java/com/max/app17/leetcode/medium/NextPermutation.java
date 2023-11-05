package com.max.app17.leetcode.medium;


import java.util.Arrays;

/** 31. Next Permutation https://leetcode.com/problems/next-permutation/ */
public class NextPermutation {

    public static void main(String[] args) throws Exception {

        int[] arr = new int[] {1, 2, 3};
        System.out.println(Arrays.toString(arr));

        for (int i = 0; i < 10; ++i) {
            new NextPermutation().nextPermutation(arr);
            System.out.println(Arrays.toString(arr));
        }

        System.out.println("NextPermutation done...");
    }

    /** time: O(N) space: O(1), in-place */
    public void nextPermutation(int[] arr) {
        for (int i = arr.length - 1; i > 0; --i) {

            // found place for next permutation
            if (arr[i - 1] < arr[i]) {

                int swapIdx = i - 1;

                int biggerValueIdx = findBiggerFromTheEnd(arr, arr[swapIdx]);

                assert biggerValueIdx >= 0 : "can't find bigger value from end of array";
                swap(arr, swapIdx, biggerValueIdx);
                reverseSubarray(arr, i, arr.length - 1);

                return;
            }
        }

        // Can't find next permutation, b/c it doesn't exist at all, so switching to first one.
        reverseSubarray(arr, 0, arr.length - 1);
    }

    private void reverseSubarray(int[] arr, int from, int to) {
        assert arr != null;
        assert from >= 0 && from < arr.length;
        assert to >= 0 && to < arr.length;
        assert from <= to;

        int left = from;
        int right = to;

        while (left < right) {
            swap(arr, left, right);
            ++left;
            --right;
        }
    }

    private void swap(int[] arr, int from, int to) {
        int temp = arr[from];
        arr[from] = arr[to];
        arr[to] = temp;
    }

    private int findBiggerFromTheEnd(int[] arr, int val) {

        for (int i = arr.length - 1; i >= 0; --i) {
            if (arr[i] > val) {
                return i;
            }
        }

        return -1;
    }
}
