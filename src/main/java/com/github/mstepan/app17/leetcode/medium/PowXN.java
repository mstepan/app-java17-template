package com.github.mstepan.app17.leetcode.medium;

/**
 * 50. Pow(x, n)
 *
 * <p>https://leetcode.com/problems/powx-n/description/
 */
public class PowXN {

    public static void main(String[] args) {

        double x = 2.0;
        int n = -7;

        double res = new PowXN().myPow(x, n);

        System.out.printf("res(expected): \t %f%n", Math.pow(x, n));
        System.out.printf("res(actual): \t %f%n", res);

        System.out.println("PowXN main done...");
    }

    /**
     * Treat power(n) as binary value. As an example, n = 11 = 1011 For every iteration track
     * shiftedValue as 5^1, 5^2, 5^4, ..., 5^2k If last bit from n is 1, multiply result by shifted
     * value, otherwise just skip. And for every iteration shift n to the right by one.
     *
     * <p>N = 32 (size of integer in bits)
     *
     * <p>Time: O(lgN) Space: O(1)
     */
    public double myPow(double x, int n) {

        int pow = Math.abs(n);
        double res = 1.0;
        double shiftedValue = x;

        while (pow != 0) {

            if ((pow & 1) != 0) {
                res *= shiftedValue;
            }

            shiftedValue *= shiftedValue;
            pow >>>= 1;
        }

        return (n < 0) ? 1.0 / res : res;
    }
}
