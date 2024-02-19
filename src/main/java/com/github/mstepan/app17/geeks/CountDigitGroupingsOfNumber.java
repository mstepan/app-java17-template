package com.github.mstepan.app17.geeks;

import java.util.Objects;

/**
 * Problem of a day 29th of January 2024.
 *
 * <p>Count digit groupings of a number.
 *
 * <p>Given a string str consisting of digits, you can divide it into sub-groups by separating the
 * string into substrings. For example, "112" can be divided as {"1", "1", "2"}, {"11", "2"}, {"1",
 * "12"}, and {"112"}.
 *
 * <p>A valid grouping can be done if you are able to divide sub-groups where the sum of digits in a
 * sub-group is less than or equal to the sum of the digits of the sub-group immediately right to
 * it. Your task is to determine the total number of valid groupings that could be done for a given
 * string.
 *
 * <p>https://www.geeksforgeeks.org/problems/count-digit-groupings-of-a-number1520/1
 */
public class CountDigitGroupingsOfNumber {

    public static void main(String[] args) throws Exception {

        // "1119" => 7
        // "05175" => 6
        // "0029953218302319" => 112
        String str = "0029953218302319";

        int count = TotalCount(str);

        System.out.printf("count: %d%n", count);

        System.out.println("CountDigitGroupingsOfNumber done...");
    }

    /**
     * Max. str.length = 100
     *
     * <p>max sum of digits = 100 * 9 = 900 (K)
     *
     * <p>time: O(N * K^2)
     *
     * <p>space: O(N*K)
     *
     * <p>Solved using dynamic programming approach.
     */
    public static int TotalCount(String str) {
        Objects.requireNonNull(str);
        if (str.length() > 100) {
            throw new IllegalArgumentException("'str' is too long, should be less or equal to 100");
        }

        checkStringHasCharactersInRange(str, '0', '9');

        final char[] arr = str.toCharArray();

        int totalArrSum = sumOfDigist(arr);

        int[][] sol = new int[totalArrSum + 1][arr.length + 1];
        sol[0][0] = 1;

        // handle all zeros as prefix, like: 0,0,0,....
        for (int col = 1, powOfTwo = 1;
                col < sol[0].length && isZeroDigit(arr, col - 1);
                ++col, powOfTwo *= 2) {
            sol[0][col] = powOfTwo;
        }

        for (int row = 1; row < sol.length; ++row) {
            for (int col = 1; col < sol[0].length; ++col) {

                int sum = row;
                int curElemIdx = col - 1;

                int j = subarraySumIdx(arr, 0, curElemIdx, sum);

                if (j != -1) {
                    int curSol = 0;

                    do {
                        for (int otherSum = sum; otherSum >= 0; --otherSum) {
                            curSol += sol[otherSum][j];
                        }

                        --j;
                    } while (j >= 0 && arr[j] == '0');

                    sol[row][col] = curSol;
                }
            }
        }

        return columnSum(sol, sol[0].length - 1);
    }

    private static boolean isZeroDigit(char[] arr, int i) {
        return arr[i] == '0';
    }

    private static boolean notZeroDigit(char[] arr, int i) {
        return arr[i] != '0';
    }

    private static void checkStringHasCharactersInRange(String str, char fromCh, char toCh) {
        for (int i = 0; i < str.length(); ++i) {
            char ch = str.charAt(i);

            if (ch < fromCh || ch > toCh) {
                throw new IllegalArgumentException(
                        String.format(
                                "Incorrect character detected for string '%s' at position %d",
                                str, i));
            }
        }
    }

    private static int sumOfDigist(char[] arr) {
        int sum = 0;

        for (char ch : arr) {
            assert ch >= '0' && ch <= '9';
            sum += (ch - '0');
        }

        return sum;
    }

    private static int subarraySumIdx(char[] arr, int from, int to, int sumToFind) {
        int curSum = 0;

        for (int i = to; i >= from && curSum <= sumToFind; --i) {
            curSum += (arr[i] - '0');
            if (curSum == sumToFind) {
                return i;
            }
        }

        return -1;
    }

    private static int columnSum(int[][] sol, int col) {
        int rowSum = 0;

        for (int row = 0; row < sol.length; ++row) {
            rowSum += sol[row][col];
        }

        return rowSum;
    }
}
