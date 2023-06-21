package com.max.app17.leetcode.hard;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/**
 * 60. Permutation Sequence
 * https://leetcode.com/problems/permutation-sequence/
 */
public class PermutationSequence {

    public static void main(String[] args) throws Exception {

        final int n = 4;
        final int k = 10;

        String permutation = getPermutation(n, k);
        System.out.println(permutation);

        System.out.println("PermutationSequence done...");
    }


    /**
     * Uses factoradic system to generate K-th permutation.
     * For more details check: https://en.wikipedia.org/wiki/Factorial_number_system
     */
    public static String getPermutation(int n, int k) {
        int index = k - 1;

        Deque<Integer> stack = toFactoradic(index);

        // add leading zeros if not enough digits
        while (stack.size() != n) {
            stack.push(0);
        }

        List<Integer> elems = new ArrayList<>();
        for (int val = 1; val <= n; ++val) {
            elems.add(val);
        }

        StringBuilder res = new StringBuilder(n);
        while (!stack.isEmpty()) {
            int curElemIndex = stack.pop();

            res.append(elems.get(curElemIndex));
            elems.remove(curElemIndex);
        }

        return res.toString();
    }

    /* Convert base10 value into factoradic value.
     */
    private static Deque<Integer> toFactoradic(int initialValue) {
        Deque<Integer> factoradicValue = new ArrayDeque<>();

        int leftValue = initialValue;

        for (int div = 1; leftValue != 0; ++div) {
            factoradicValue.push(leftValue % div);
            leftValue /= div;
        }

        return factoradicValue;
    }


}
