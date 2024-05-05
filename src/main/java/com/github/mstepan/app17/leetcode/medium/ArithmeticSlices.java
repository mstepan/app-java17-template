package com.github.mstepan.app17.leetcode.medium;

import java.util.Objects;

public class ArithmeticSlices {
    public static void main(String[] args) {
        int[] arr = {1, 2, 3, 4};
        int slicesCount = new ArithmeticSlices().numberOfArithmeticSlices(arr);

        System.out.printf("slicesCount: %d%n", slicesCount);
    }

    /**
     * time: O(N)
     *
     * <p>space: O(1)
     */
    public int numberOfArithmeticSlices(int[] arr) {
        Objects.requireNonNull(arr, "null 'arr' detected");

        if (arr.length < 3) {
            return 0;
        }

        int cnt = 0;

        int prevDiff = arr[1] - arr[0];
        int size = 2;

        for (int i = 2; i < arr.length; ++i) {

            int curDiff = arr[i] - arr[i - 1];

            if (curDiff == prevDiff) {
                ++size;
                if (size >= 3) {
                    cnt += (size - 3 + 1);
                }
            } else {
                prevDiff = curDiff;
                size = 2;
            }
        }

        return cnt;
    }
}
