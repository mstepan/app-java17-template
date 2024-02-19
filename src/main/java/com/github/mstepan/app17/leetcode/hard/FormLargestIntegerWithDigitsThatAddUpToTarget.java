package com.max.app17.leetcode.hard;

import java.util.Comparator;

/**
 * 1449. Form Largest Integer With Digits That Add up to Target
 * https://leetcode.com/problems/form-largest-integer-with-digits-that-add-up-to-target/
 */
public class FormLargestIntegerWithDigitsThatAddUpToTarget {
    public static void main(String[] args) throws Exception {
        final int target = 9;
        final int[] cost = {4, 3, 2, 5, 6, 7, 2, 5, 5};

        // expected = '7772'
        String maxStr =
                new FormLargestIntegerWithDigitsThatAddUpToTarget().largestNumber(cost, target);

        System.out.printf("maxStr: '%s'%n", maxStr);

        System.out.println("Main done...");
    }

    private static final Comparator<String> STR_AS_NUMBER_CMP_ASC =
            Comparator.comparingInt(String::length).thenComparing(Comparator.naturalOrder());

    /**
     * Bottom up dynamic programming solution with 1-dimension array. M = target time: O(M*9) ~ O(M)
     * space: O(M)
     */
    public String largestNumber(int[] cost, int target) {

        String[] sol = new String[target + 1];
        sol[0] = "";

        for (int i = 1; i < sol.length; ++i) {
            final int curTarget = i;
            String maxCur = null;

            for (int j = 0; j < cost.length; ++j) {
                final int digit = j + 1;
                if (curTarget >= cost[j]) {

                    final String partialSol = sol[curTarget - cost[j]];

                    if (partialSol != null) {
                        String curStr = digit + partialSol;

                        if (maxCur == null || STR_AS_NUMBER_CMP_ASC.compare(curStr, maxCur) > 0) {
                            maxCur = curStr;
                        }
                    }
                }
            }

            sol[i] = maxCur;
        }

        final String largestStr = sol[sol.length - 1];

        return largestStr == null ? "0" : largestStr;
    }
}
