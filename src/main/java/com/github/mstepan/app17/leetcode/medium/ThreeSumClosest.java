package com.max.app17.leetcode.medium;

import java.util.Arrays;

/** <a href="https://leetcode.com/problems/3sum-closest/">16. 3Sum Closest</a> */
public class ThreeSumClosest {

    public static void main(String[] args) throws Exception {

        int[] arr = {-1, 2, 1, -4};
        int target = 1;

        int closest3Sum = new ThreeSumClosest().threeSumClosest(arr, target);

        System.out.printf("closestSum: %d%n", closest3Sum);

        System.out.println("Main done...");
    }

    /** time: O(N*lgN + N^2) ~ O(N^2) space: O(1) */
    public int threeSumClosest(int[] arr, int target) {
        assert arr != null && arr.length >= 3;
        Arrays.sort(arr);

        int bestSum = arr[0] + arr[1] + arr[2];
        int bestDiff = Math.abs(target - bestSum);

        for (int pivotIdx = 0; pivotIdx < arr.length; ++pivotIdx) {

            int pivot = arr[pivotIdx];
            int target2Sum = target - pivot;

            int twoSumCloses = twoSumClosest(arr, target2Sum, pivotIdx);

            int curSum = pivot + twoSumCloses;
            int curDiff = Math.abs(target - curSum);

            if (curDiff < bestDiff) {
                bestDiff = curDiff;
                bestSum = curSum;
            }
        }

        return bestSum;
    }

    private int twoSumClosest(int[] arr, int target, int skipIdx) {
        // 'arr' already sorted
        assert arr != null && arr.length >= 3;
        assert skipIdx >= 0 && skipIdx < arr.length;

        int bestSum = firstFewNotSkippedElementsSum(arr, skipIdx);
        int bestDiff = Math.abs(target - bestSum);

        int left = 0;
        int right = arr.length - 1;

        while (left < right) {
            if (left == skipIdx) {
                ++left;
                continue;
            }

            if (right == skipIdx) {
                --right;
                continue;
            }

            int curSum = arr[left] + arr[right];
            int curDiff = Math.abs(target - curSum);

            if (curDiff < bestDiff) {
                bestDiff = curDiff;
                bestSum = curSum;
            }

            // we can skip earlier if we wound exact value
            if (curSum == target) {
                return curSum;
            }

            if (curSum < target) {
                ++left;
            } else {
                --right;
            }
        }

        return bestSum;
    }

    private static int firstFewNotSkippedElementsSum(int[] arr, int skipIdx) {
        // 0, 1
        int sum = arr[0] + arr[1];
        if (skipIdx == 0) {
            // [0], 1, 2
            sum = arr[1] + arr[2];
        } else if (skipIdx == 1) {
            // 0, [1], 2
            sum = arr[0] + arr[2];
        }
        return sum;
    }
}
