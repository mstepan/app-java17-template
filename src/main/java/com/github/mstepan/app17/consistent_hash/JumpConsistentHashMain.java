package com.github.mstepan.app17.consistent_hash;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.BiFunction;

public class JumpConsistentHashMain {

    public static void main(String[] args) throws Exception {

        hashToBuckets(JumpConsistentHashMain::jumpConsistentHash);
        hashToBuckets(JumpConsistentHashMain::jumpConsistentHashOptimized);

        //        System.out.println(jumpConsistentHash("hello".hashCode(), 8));
        //        System.out.println(jumpConsistentHashOptimized("hello".hashCode(), 8));

        //        String str = "hello";
        //        int bucketsCount = 8;
        //
        //        for (int i = 0; i < 4; ++i) {
        //            System.out.println(jumpConsistentHash(str.hashCode(), bucketsCount));
        //            System.out.println(jumpConsistentHash2(str.hashCode(), bucketsCount));
        //        }

        System.out.println("JumpConsistentHashMain done...");
    }

    static void hashToBuckets(BiFunction<Integer, Integer, Integer> hashFunc) {
        int numOfBuckets = 21;

        int[] bucketsCnt = new int[numOfBuckets];

        for (int val = 0; val < 1_000_000; ++val) {
            String randStr = randomString(3 + ThreadLocalRandom.current().nextInt(20));
            int bucketIdx = hashFunc.apply(randStr.hashCode(), numOfBuckets);
            ++bucketsCnt[bucketIdx];
        }

        System.out.println(Arrays.toString(bucketsCnt));
    }

    static String randomString(int length) {

        char[] arr = new char[length];

        for (int i = 0; i < arr.length; ++i) {
            char randCh = (char) ('a' + ThreadLocalRandom.current().nextInt('z' - 'a' + 1));
            arr[i] = randCh;
        }

        return String.valueOf(arr);
    }

    /**
     * White paper that describes algorithms in details https://arxiv.org/pdf/1406.2294.pdf Thread
     * safe.
     */
    static int jumpConsistentHashOptimized(long initialKey, int numBuckets) {
        long key = initialKey;
        int prevVal = -1;

        int curVal = 0;
        while (curVal < numBuckets) {
            prevVal = curVal;
            key = key * 2_862_933_555_777_941_757L + 1;
            curVal = (int) ((prevVal + 1) * (double) (1L << 31) / ((key >>> 33) + 1));
        }

        return prevVal;
    }

    private static final ThreadLocal<Random> RANDOM_THREAD_LOCAL_RANDOM =
            ThreadLocal.withInitial(Random::new);

    /** Thread safe. */
    static int jumpConsistentHash(int key, int numBuckets) {
        final Random rand = RANDOM_THREAD_LOCAL_RANDOM.get();
        rand.setSeed(key);

        int prev = 1;
        int cur = 0;

        while (cur < numBuckets) {
            prev = cur;
            cur = (int) Math.floor((prev + 1) / rand.nextDouble());
        }

        return prev;
    }
}
