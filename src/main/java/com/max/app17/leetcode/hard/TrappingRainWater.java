package com.max.app17.leetcode.hard;

/** 42. Trapping Rain Water https://leetcode.com/problems/trapping-rain-water/ */
public class TrappingRainWater {

    public static void main(String[] args) throws Exception {

        int[] arr = {4, 2, 0, 3, 2, 5};

        int trappedWater = trap(arr);

        System.out.printf("trappedWater: %d\n", trappedWater);

        System.out.println("TrappingRainWater done...");
    }

    /** time: O(N) space: O(N) */
    public static int trap(int[] height) {
        int[] maxRight = new int[height.length];

        maxRight[maxRight.length - 1] = height[height.length - 1];

        for (int i = maxRight.length - 2; i >= 0; --i) {
            maxRight[i] = Math.max(height[i], maxRight[i + 1]);
        }

        int trappedSum = 0;

        int maxLeft = height[0];
        for (int i = 1; i < height.length - 1; ++i) {
            maxLeft = Math.max(maxLeft, height[i]);

            int curTrapped = Math.min(maxLeft, maxRight[i + 1]) - height[i];

            trappedSum += Math.max(curTrapped, 0);
        }

        return trappedSum;
    }
}
