package com.max.app17;

public class Main {

    public static void main(String[] args) throws Exception {

        int[] nums = {1, 2, 31, 33};
        int n = 2_147_483_647;

        System.out.println(minPatches(nums, n));

        System.out.println("Main done...");
    }

    /*
    time: O(N)
    space: O(1)
    */
    public static int minPatches(int[] nums, int n) {
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
