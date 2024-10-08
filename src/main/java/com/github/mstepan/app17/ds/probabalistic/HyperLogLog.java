package com.github.mstepan.app17.ds.probabalistic;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
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
public final class HyperLogLog<T> {

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

    private final Map<Integer, Integer> log2Precalculated;

    public HyperLogLog() {
        buckets = new int[1 << M];
        bucketsCount = buckets.length;
        bitsUsedForValue = Integer.SIZE - M;
        log2Precalculated = new HashMap<>();

        int oneShifted = 1;
        for (int i = 0; i <= bitsUsedForValue; ++i) {
            log2Precalculated.put(oneShifted, i);
            oneShifted <<= 1;
        }
    }

    public void add(T value) {
        Objects.requireNonNull(value);

        int hashedValue = value.hashCode();

        // use 'M' most significant bits as a bucket index
        int bucketIdx = (hashedValue >>> bitsUsedForValue);

        int zerosCount = countZerosFromRight(hashedValue, bitsUsedForValue);

        @SuppressWarnings("unused")
        String binaryStr = Integer.toBinaryString(hashedValue);

        buckets[bucketIdx] = Math.max(buckets[bucketIdx], zerosCount + 1);
    }

    // TODO: use below function for long hashCode values
    @SuppressWarnings("unused")
    private long hashCodeAsLong(T value) {
        return 1125899906842597L * value.hashCode();
    }

    /**
     * Count continuous 0-s from right to left (from least significant to most digit) but no more
     * than 'bitsUsedForValue'.
     */
    private int countZerosFromRight(int hashedValue, int bitsUsedForValue) {
        int lestSignificantBitSet =
                Math.min(leastSignificantSetBit(hashedValue), 1 << bitsUsedForValue);

        return log2Precalculated.get(lestSignificantBitSet);
    }

    /** Extract the least significant bit set to '1' using 'value & (~value + 1)' formula */
    private static int leastSignificantSetBit(int value) {
        return value & (~value + 1);
    }

    public int cardinality() {
        return (int) Math.round(alpha() * (bucketsCount * bucketsCount) * indicator(buckets));
    }

    /**
     * Harmonic mean should be used instead of ordinary mean to minimize outlier impact.
     * https://en.wikipedia.org/wiki/Harmonic_mean
     */
    private static double indicator(int[] arr) {
        assert arr != null : "null 'arr' detected";

        double sum = 0.0;

        for (int val : arr) {
            sum += Math.pow(2.0, -val);
        }

        return 1.0 / sum;
    }

    private double alpha() {
        return 0.7213 / (1.0 + (1.079 / bucketsCount));
    }

    public static void main(String[] args) throws Exception {

        HyperLogLog<Integer> data = new HyperLogLog<>();

        Set<Integer> realData = new HashSet<>();

        Random rand = ThreadLocalRandom.current();
        for (int i = 0; i < 1_000_000; ++i) {
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
