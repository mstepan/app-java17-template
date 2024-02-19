package com.max.app17.leetcode.medium;

/*
633. Sum of Square Numbers
https://leetcode.com/problems/sum-of-square-numbers/
*/
public class SumOfSquareNumbers {

    public static void main(String[] args) throws Exception {
        int c = 4;
        boolean hasSum = new SumOfSquareNumbers().judgeSquareSum(c);

        System.out.printf("judgeSquareSum(%d) = %b%n", c, hasSum);
        System.out.println("SumOfSquareNumbers done...");
    }

    /*
    time: O(sqrt(N))
    space: O(1)
    */
    public boolean judgeSquareSum(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("Negative value detected, 'c = %d'".formatted(n));
        }

        // handle cases like n = 4
        // 0^2 + 2^2 = 4
        if (isPerfectSquare(n)) {
            return true;
        }

        long lo = 1L;
        long hi = (long) Math.sqrt(n);

        while (lo <= hi) {
            long curRes = lo * lo + hi * hi;

            if (curRes == n) {
                return true;
            }

            if (curRes < n) {
                ++lo;
            } else {
                --hi;
            }
        }

        return false;
    }

    private boolean isPerfectSquare(int n) {
        int sqrtVal = (int) Math.sqrt(n);
        return sqrtVal * sqrtVal == n;
    }
}
