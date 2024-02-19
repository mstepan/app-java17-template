package com.max.app17.leetcode.hard;

import java.util.concurrent.ThreadLocalRandom;

/** 233. Number of Digit One https://leetcode.com/problems/number-of-digit-one/ */
public class NumberOfDigitOne {

    public static void main(String[] args) throws Exception {

        ThreadLocalRandom rand = ThreadLocalRandom.current();

        for (int it = 0; it < 1000; ++it) {
            int val = 1 + rand.nextInt(100_000);

            int expected = countDigitOneBruteforce(val);
            int actual = new NumberOfDigitOne().countDigitOne(val);

            if (expected != actual) {
                throw new IllegalStateException(
                        "expected(%d) != actual(%d) for value %d".formatted(expected, actual, val));
            }
        }

        System.out.println("NumberOfDigitOne completed, no errors detected.");
    }

    private static final int[] ONES_CNT_IN_DIGITS = new int[10];

    static {
        ONES_CNT_IN_DIGITS[1] = 1;

        for (int i = 2; i < ONES_CNT_IN_DIGITS.length; ++i) {
            ONES_CNT_IN_DIGITS[i] = 10 * ONES_CNT_IN_DIGITS[i - 1] + (int) Math.pow(10, i - 1);
        }
    }

    public int countDigitOne(int val) {

        int suffixValue = 0;
        int res = val;
        int onesCnt = 0;
        int digitsIndex = 1;

        while (res != 0) {
            int digit = res % 10;

            if (digit == 1) {
                onesCnt += (digit * ONES_CNT_IN_DIGITS[digitsIndex - 1]) + (suffixValue + 1);
            } else if (digit > 0) {
                onesCnt +=
                        (digit * ONES_CNT_IN_DIGITS[digitsIndex - 1])
                                + (int) Math.pow(10.0, digitsIndex - 1);
            }

            suffixValue = digit * (int) Math.pow(10.0, digitsIndex - 1) + suffixValue;
            res /= 10;
            ++digitsIndex;
        }

        return onesCnt;
    }

    private static int countDigitOneBruteforce(int threshold) {
        int cnt = 0;

        for (int i = 1; i <= threshold; ++i) {
            cnt += numberOfDecimalOnes(i);
        }
        return cnt;
    }

    private static int numberOfDecimalOnes(int val) {

        // -2147483648 => 1 decimal one
        if (val == Integer.MIN_VALUE) {
            return 1;
        }

        int res = Math.abs(val);
        int onesCnt = 0;

        while (res != 0) {
            int digit = res % 10;
            onesCnt += (digit == 1 ? 1 : 0);
            res /= 10;
        }

        return onesCnt;
    }
}
