package com.github.mstepan.app17.leetcode.medium;

/**
 * https://leetcode.com/problems/bitwise-and-of-numbers-range/description/
 *
 * <p>201. Bitwise AND of Numbers Range
 */
public class BitwiseAndOfNumbersRange {

    public static void main(String[] args) {
        //        int left = 18;
        //        int right = 25;

        //        int left = 5;
        //        int right = 7;

        //        int left = 0;
        //        int right = 0;

        int left = 1;
        int right = Integer.MAX_VALUE;

        int res = new BitwiseAndOfNumbersRange().rangeBitwiseAnd(left, right);

        System.out.printf("res: %d%n", res);
    }

    /**
     * time: O(32) ~ O(1)
     *
     * <p>space: O(1)
     */
    public int rangeBitwiseAnd(int left, int right) {

        int diff = right - left;

        int stableBitIdx = lastChangedBitIdxDuringAdd(left, diff) + 1;

        return left & (-(1 << stableBitIdx));
    }

    private int lastChangedBitIdxDuringAdd(int value, int diff) {

        int carry = 0;
        int idx = -1;

        while (diff > 0 || carry > 0) {
            int curBit = (diff & 1) + (value & 1) + carry;

            ++idx;
            carry = curBit / 2;
            value >>>= 1;
            diff >>>= 1;
        }

        return idx;
    }
}
