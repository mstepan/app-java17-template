package com.github.mstepan.app17.leetcode.medium;

import java.util.Objects;

/**
 * 3153. Sum of Digit Differences of All Pairs
 *
 * <p>https://leetcode.com/problems/sum-of-digit-differences-of-all-pairs/description/
 */
public class SumOfDigitDifferencesOfAllPairs {

    public static void main(String[] args) {

        // expected: 4
        //                int[] nums = {13, 23, 12};

        // expected: 0
        //                int[] nums = {10, 10, 10, 10};

        // expected: 5
        //                int[] nums = {50, 28, 48};

        // expected: 67
        int[] nums = {824, 843, 837, 620, 836, 234, 276, 859};

        long diff = new SumOfDigitDifferencesOfAllPairs().sumDigitDifferences(nums);

        System.out.printf("diff: %d%n", diff);

        System.out.println("SumOfDigitDifferencesOfAllPairs done...");
    }

    /**
     * time: O(N)
     *
     * <p>space: O(1)
     */
    public long sumDigitDifferences(int[] nums) {
        Objects.requireNonNull(nums);

        if (nums.length < 2) {
            return 0;
        }

        int bucketsCount = countDigits(nums[0]);

        int[][] bucketsDigits = new int[bucketsCount][10];

        for (int val : nums) {
            for (int left = val, bucketIdx = 0; left != 0; left /= 10, ++bucketIdx) {
                int digit = left % 10;
                bucketsDigits[bucketIdx][digit] += 1;
            }
        }

        long digitsDiff = 0L;

        for (int[] singleBucket : bucketsDigits) {
            digitsDiff += calculateDiffForBucket(singleBucket);
        }

        return digitsDiff;
    }

    private long calculateDiffForBucket(int[] bucket) {
        long totalDiff = 0L;

        for (int digit = 0; digit < bucket.length - 1; ++digit) {
            for (int other = digit + 1; other < bucket.length; ++other) {
                totalDiff += (long) bucket[digit] * bucket[other];
            }
        }

        return totalDiff;
    }

    private int countDigits(int num) {
        int cnt = 0;

        for (int val = num; val != 0; val /= 10) {
            ++cnt;
        }

        return cnt;
    }
}
