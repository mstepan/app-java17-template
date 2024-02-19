package com.max.app17.numbers;

import java.math.BigInteger;
import java.util.BitSet;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public class NumbersUtil {

    private static final Set<Integer> CARMICHAEL_NUMBERS =
            Set.of(
                    561, 1105, 1729, 2465, 2821, 6601, 8911, 10585, 15841, 29341, 41041, 46657,
                    52633, 62745, 63973, 75361, 101101, 115921, 126217, 162401, 172081, 188461,
                    252601, 278545, 294409, 314821, 334153, 340561, 399001, 410041, 449065, 488881,
                    512461);

    /**
     * Check if value is prime. Returns probably prime or certainly composite.
     *
     * @return true - if number is probably prime (false positive possible), false - if number is
     *     100% composite.
     */
    public static boolean isProbablyPrime(int p) {
        if (CARMICHAEL_NUMBERS.contains(p)) {
            return false;
        }

        final ThreadLocalRandom rand = ThreadLocalRandom.current();

        final BigInteger power = BigInteger.valueOf(p - 1);
        final BigInteger prime = BigInteger.valueOf(p);

        for (int it = 0; it < 20; ++it) {

            int a = 1 + rand.nextInt(p - 1);

            BigInteger rem = BigInteger.valueOf(a).modPow(power, prime);

            if (!rem.equals(BigInteger.ONE)) {
                return false;
            }
        }

        return true;
    }

    public static int mod(int val, int m) {
        return val >= 0 ? val % m : (val % m) + m;
    }

    /**
     * Will return negative value for gcd(Integer.MIN_VALUE, Integer.MIN_VALUE) b/c
     * Math.abs(Integer.MIN_VALUE) == Integer.MIN_VALUE
     */
    public static int gcd(int v1, int v2) {
        if (v1 == 0 || v2 == 0) {
            return 0;
        }

        int a = v1;
        int b = v2;

        while (b != 0) {
            int temp = a;
            a = b;
            b = temp % b;
        }

        return Math.abs(a);
    }

    /**
     * <a href="https://en.wikipedia.org/wiki/Binary_GCD_algorithm">Binary gcd, a.k.a. Stein's
     * method</a>
     */
    public static int binaryGcd(int v1, int v2) {

        int a = Math.abs(v1);
        int b = Math.abs(v2);

        int res = 1;

        while (a != 0 && b != 0) {

            int aZerosCnt = trailingZeros(a);
            int bZerosCnt = trailingZeros(b);

            res <<= Math.min(aZerosCnt, bZerosCnt);
            a >>= aZerosCnt;
            b >>= bZerosCnt;

            if (a == b) {
                res *= a;
                break;
            }

            int bigger = Math.max(a, b);
            int smaller = Math.min(a, b);
            a = bigger - smaller;
            b = smaller;

            //            // both even
            //            if (isEven(a) && isEven(b)) {
            //                res *= 2;
            //                a >>= 1;
            //                b >>= 1;
            //            }
            //            // both odd
            //            else if (isOdd(a) && isOdd(b)) {
            //                int bigger = Math.max(a, b);
            //                int smaller = Math.min(a, b);
            //                a = bigger - smaller;
            //                b = smaller;
            //            }
            //            // one even, another odd
            //            else {
            //                if (isEven(a)) {
            //                    a >>= 1;
            //                }
            //                else {
            //                    b >>= 1;
            //                }
            //            }
        }

        return res;
    }

    public static int trailingZeros(int initialValue) {
        int value = initialValue;
        int zerosCnt = 0;

        while ((value & 1) == 0) {
            ++zerosCnt;
            value >>= 1;
        }

        return zerosCnt;
    }

    public static boolean isEven(int val) {
        return (val & 1) == 0;
    }

    public static boolean isOdd(int val) {
        return !isEven(val);
    }

    public static boolean isPrime(int value) {

        // negative values, 0, and 1 are not prime by definition
        if (value < 2) {
            return false;
        }
        if (value == 2 || value == 3) {
            return true;
        }

        int boundary = (int) (Math.sqrt(value));

        BitSet allPrimes = sievePrimes(boundary);

        for (int prime = allPrimes.nextSetBit(0);
                prime != -1;
                prime = allPrimes.nextSetBit(prime + 1)) {
            if ((value % prime) == 0) {
                return false;
            }
        }

        return true;
    }

    public static BitSet sievePrimes(int n) {

        BitSet primes = new BitSet(n + 1);
        primes.set(2, n + 1, true);

        for (int curPrime = primes.nextSetBit(0);
                curPrime != -1 && curPrime * curPrime <= n;
                curPrime = primes.nextSetBit(curPrime + 1)) {
            for (int composite = curPrime + curPrime; composite <= n; composite += curPrime) {
                primes.clear(composite);
            }
        }
        return primes;
    }
}
