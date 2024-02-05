package com.max.app17.leetcode.medium;

import java.util.Arrays;

/**
 * 1734. Decode XORed Permutation
 *
 * <p>https://leetcode.com/problems/decode-xored-permutation/description/
 */
public class DecodeXoredPermutation {

    public static void main(String[] args) throws Exception {

        // original arr = [1, 3, 4, 2, 5]
        int[] arr = decode(new int[] {1 ^ 3, 3 ^ 4, 4 ^ 2, 2 ^ 5});

        System.out.println(Arrays.toString(arr));

        System.out.println("DecodeXoredPermutation done...");
    }

    /**
     * time: O(N)
     *
     * <p>space: O(N)
     */
    public static int[] decode(int[] encoded) {
        int n = encoded.length + 1;

        int allExceptOne = 0;

        for (int i = 0; i < encoded.length; i += 2) {
            allExceptOne ^= encoded[i];
        }

        // below value will have all elements of an array XOR-ed, except pre-last one
        allExceptOne ^= encoded[encoded.length - 1];

        // find xor of all values in range [1...N]
        int allValues = findXor(n);

        // find pre-last element
        int elemVal = allExceptOne ^ allValues;

        int[] arr = new int[n];
        arr[arr.length - 1] = encoded[encoded.length - 1] ^ elemVal;

        // reconstruct original array usign backward iteration
        for (int i = encoded.length - 1; i >= 0; --i) {
            arr[i] = (encoded[i] ^ arr[i + 1]);
        }

        return arr;
    }

    private static int findXor(int n) {
        int res = 0;

        for (int i = 1; i <= n; ++i) {
            res ^= i;
        }

        return res;
    }
}
