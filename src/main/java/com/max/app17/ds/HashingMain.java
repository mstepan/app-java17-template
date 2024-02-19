package com.max.app17.ds;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class HashingMain {

    public static void main(String[] args) throws Exception {

        MulHash2 myHash = new MulHash2();

        Map<Integer, Integer> bucketsSize = new HashMap<>();

        ThreadLocalRandom rand = ThreadLocalRandom.current();

        for (int it = -10_000_000; it < 10_000_000; ++it) {
            Integer val = it; // rand.nextInt();

            int hashRes = myHash.hash(val.hashCode());

            if (hashRes < 0 || hashRes > 1024) {
                throw new IllegalStateException("incorrect hash: " + hashRes);
            }
            bucketsSize.compute(hashRes, (key, oldValue) -> oldValue == null ? 1 : oldValue + 1);
        }

        int minSize = Integer.MAX_VALUE;
        int maxSize = Integer.MIN_VALUE;

        for (Map.Entry<Integer, Integer> entry : bucketsSize.entrySet()) {
            int curFreq = entry.getValue();
            minSize = Math.min(curFreq, minSize);
            maxSize = Math.max(curFreq, maxSize);
        }
        System.out.printf("min: %d, max: %d\n", minSize, maxSize);

        System.out.println("HashingMain done...");
    }

    //
    //    private static final class MulHash1 {
    //        static final int HASH_BITS = 10; // 1024
    //
    //        static final double GOLDEN_RATIO = (Math.sqrt(5.0) - 1) / 2.0;
    //
    //        // 2_654_435_769L
    //        static final long SALT = (long) (GOLDEN_RATIO * ((long) 1 << 32));
    //
    //        int hash(int value) {
    //            return ((int) (value * SALT) >>> (Integer.SIZE - HASH_BITS));
    //        }
    //    }

    private static final class MulHash2 {
        static final int HASH_BITS = 10; // 1024

        // just big prime value, we can't use Integer.MAX_VALUE here, b/c it gives bad distribution
        static final int SALT = 2_000_004_079; // Integer.MAX_VALUE;

        int hash(int value) {
            return (value * SALT) >>> (Integer.SIZE - HASH_BITS);
        }
    }
}
