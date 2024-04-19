package com.github.mstepan.app17.ds.probabalistic;

import java.util.HashSet;
import java.util.Objects;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Probabalistic data structure for cardinality estimation.
 *
 * <p>Good explanations can be found https://chengweihu.com/hyperloglog/
 *
 * <p>Original paper from Google:
 * https://static.googleusercontent.com/media/research.google.com/en//pubs/archive/40671.pdf
 */
public class HyperLogLog<T> {

    /**
     * Bits of hashCode that used as a bucket index. We will have 2^M buckets in total. Buckets
     * range[0...2^M-1].
     *
     * <p>Error rate will be calculated as: 1.04/sqrt(M) If we use log2(16384) = 14 bits per bucket,
     * we will have error rate ~ 0.81% (check http://www.antirez.com/news/75)
     *
     * <p>Memory will be 16384 buckets * 4 bytes each = 65536 bytes = 64 KBytes
     */
    private static final int M = 14;

    private final int[] buckets;
    private final int bucketsCount;

    private final int bitsUsedForValue;

    public HyperLogLog() {
        buckets = new int[1 << M];
        bucketsCount = buckets.length;
        bitsUsedForValue = Integer.SIZE - M;
    }

    public void add(T value) {
        Objects.requireNonNull(value);

        int hashedValue = value.hashCode();

        // use 'M' most significant bits as a bucket index
        int bucketIdx = (hashedValue >>> bitsUsedForValue);

        buckets[bucketIdx] =
                Math.max(buckets[bucketIdx], countZerosFromRight(hashedValue, bitsUsedForValue));
    }

    /**
     * Count continuous 0-s from right to left (from least significant to most digit) but no more
     * than 'bitsUsedForValue'.
     */
    private int countZerosFromRight(int initialHashedValue, int bitsUsedForValue) {

        int zerosCnt = 0;

        int hashedValue = initialHashedValue;

        for (int i = 0; i < bitsUsedForValue; ++i) {
            if ((hashedValue & 1) != 0) {
                break;
            }
            ++zerosCnt;
            hashedValue >>>= 1;
        }

        return zerosCnt;
    }

    public int cardinality() {
        return bucketsCount * (int) Math.round(Math.pow(2.0, harmonicMean(buckets)));
    }

    /**
     * Harmonic mean should be used instead of ordinary mean to minimize outlier impact.
     * https://en.wikipedia.org/wiki/Harmonic_mean
     */
    private static double harmonicMean(int[] arr) {
        assert arr != null : "null 'arr' detected";

        double inverseSum = 0.0;

        for (int val : arr) {
            inverseSum += 1.0 / val;
        }

        double n = arr.length;

        return n / inverseSum;
    }

    public static void main(String[] args) throws Exception {

        HyperLogLog<Integer> data = new HyperLogLog<>();

        Set<Integer> realData = new HashSet<>();

        Random rand = ThreadLocalRandom.current();
        for (int i = 0; i < 10_000_000; ++i) {
            int randValue = rand.nextInt();
            data.add(randValue);
            realData.add(randValue);
        }

        int real = realData.size();
        int estimated = data.cardinality();

        double error = 100.0 - (estimated * 100.0) / real;

        System.out.printf("cardinality real: %d%n", real);
        System.out.printf("cardinality estimated: %d%n", estimated);

        System.out.printf("error: %.1f %% %n", error);

        System.out.println("HyperLogLog main done...");
    }
}
