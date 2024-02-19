package com.max.app17.string;

import java.math.BigInteger;
import java.util.concurrent.ThreadLocalRandom;

final class RollingHash {

    /** 'base' and 'mod' should be relatively prime values */
    private final int base;

    private final int mod;

    private final long baseModPow;

    RollingHash(int windowSize) {
        ThreadLocalRandom rand = ThreadLocalRandom.current();
        this.base = rand.nextInt(10_000, 100_000);
        this.mod = BigInteger.probablePrime(31, rand).intValue();
        this.baseModPow = calculateBaseModPow(windowSize - 1);
    }

    long initialHash(String str, int from, int to) {
        long res = 0L;

        for (int i = from; i <= to; ++i) {
            res = (((res * base) % mod) + str.charAt(i)) % mod;
        }

        return res;
    }

    long nextHash(String text, long prevHash, int left, int right) {
        long newHash = ((prevHash - ((text.charAt(left - 1) * baseModPow) % mod)) + mod) % mod;

        newHash = (((newHash * base) % mod) + text.charAt(right)) % mod;

        return newHash;
    }

    private long calculateBaseModPow(int power) {
        long res = 1L;

        for (int i = 0; i < power; ++i) {
            res = (res * base) % mod;
        }

        return res;
    }

    @Override
    public String toString() {
        return "base: %d, mod: %d".formatted(base, mod);
    }
}
