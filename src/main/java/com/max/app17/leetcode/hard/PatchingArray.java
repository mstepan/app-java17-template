package com.max.app17.leetcode.hard;

/*
330. Patching Array
https://leetcode.com/problems/patching-array/
 */
public class PatchingArray {

    public static void main(String[] args) throws Exception {

        int[] nums = {1, 5, 10};
        int n = 20;

        System.out.println(minPatches(nums, n));

        System.out.println("PatchingArray done...");
    }

    /*
    time: O(N)
    space: O(1)
    */
    public static int minPatches(int[] nums, int n) {

        // use 'long' here to mitigate overflow errors
        long covered = 0L;
        int i = 0;

        int patchesCount = 0;

        for (long cur = 1L; cur <= n; ) {

            while (i < nums.length && nums[i] <= cur) {
                covered += nums[i];
                ++i;
            }

            if (covered < cur) {
                covered += cur;
                ++patchesCount;
            }

            cur = covered + 1;
        }

        return patchesCount;
    }
}
