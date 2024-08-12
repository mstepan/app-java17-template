package com.github.mstepan.app17.algorithms;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.ThreadLocalRandom;

public class MaxSubarraySum {

    public static void main(String[] args) {

        for (int it = 0; it < 10_000; ++it) {

            int[] arr = randomArray(); // {-2, 1, -3, 4, -1, 2, 1, -5, 4};

            int res1 = maxSubarraySumSequantial(arr, 0, arr.length - 1);
            int res2 = maxSubarraySumParallel(arr);

            if (res1 != res2) {
                System.out.printf(
                        "res[sequential]: %d%n", maxSubarraySumSequantial(arr, 0, arr.length - 1));
                System.out.printf("  res[parallel]: %d%n", maxSubarraySumParallel(arr));
            }
        }

        System.out.println("Main done...");
    }

    private static int[] randomArray() {
        ThreadLocalRandom rand = ThreadLocalRandom.current();
        int[] arr = new int[10 + rand.nextInt(1000)];

        for (int i = 0; i < arr.length; ++i) {
            arr[i] = rand.nextInt(1000);
        }

        return arr;
    }

    public static int maxSubarraySumParallel(int[] nums) {
        if (nums.length == 0) {
            return 0;
        }

        if (nums.length == 1) {
            return nums[0];
        }

        ForkJoinPool pool = new ForkJoinPool();

        int result = pool.invoke(new MaxSubarraySumRecursiveTask(nums, 0, nums.length - 1));

        pool.shutdownNow();

        return result;
    }

    private static final class MaxSubarraySumRecursiveTask extends RecursiveTask<Integer> {

        private final int[] arr;
        private final int from;
        private final int to;

        public MaxSubarraySumRecursiveTask(int[] arr, int from, int to) {
            this.arr = arr;
            this.from = from;
            this.to = to;
        }

        @Override
        protected Integer compute() {

            int elemsCount = (to - from + 1);

            if (elemsCount < 5) {
                int res = maxSubarraySumSequantial(arr, from, to);
                return res;
            }

            int mid = from + (to - from) / 2;

            ForkJoinTask<Integer> leftTask =
                    new MaxSubarraySumRecursiveTask(arr, from, mid - 1).fork();
            ForkJoinTask<Integer> rightTask = new MaxSubarraySumRecursiveTask(arr, mid, to).fork();

            int leftMax = 0;
            int leftSum = 0;

            for (int i = mid - 1; i >= from; --i) {
                leftSum = leftSum + arr[i];
                leftMax = Math.max(leftMax, leftSum);
            }

            int rightMax = 0;
            int rightSum = 0;
            for (int i = mid; i <= to; ++i) {
                rightSum = rightSum + arr[i];
                rightMax = Math.max(rightMax, rightSum);
            }

            int sumInSubarrays = Math.max(leftTask.join(), rightTask.join());
            int middleSum = leftMax + rightMax;

            return Math.max(sumInSubarrays, middleSum);
        }
    }

    public static int maxSubarraySumSequantial(int[] nums, int from, int to) {
        //        if (nums.length == 0) {
        //            return 0;
        //        }
        //
        //        if (nums.length == 1) {
        //            return nums[0];
        //        }

        int maxCur = 0;
        int maxSoFar = 0;
        int singleMaxValue = nums[0];

        for (int i = from; i <= to; ++i) {
            int val = nums[i];
            singleMaxValue = Math.max(singleMaxValue, val);

            if (willOverflow(maxCur, val)) {
                maxCur = 0;
            } else {
                maxCur = Math.max(0, maxCur + val);
            }

            maxSoFar = Math.max(maxSoFar, maxCur);
        }

        if (maxSoFar == 0) {
            return singleMaxValue;
        }

        return maxSoFar;
    }

    /**
     * left + right > Integer.MAX_VALUE
     *
     * <p>left > Integer.MAX_VALUE - right
     */
    private static boolean willOverflow(int left, int right) {
        return left > 0 && right > 0 && left > Integer.MAX_VALUE - right;
    }
}
