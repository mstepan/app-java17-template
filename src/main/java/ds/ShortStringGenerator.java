package ds;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Short strign generator. Can be used for concurrent URL shortening.
 */
public class ShortStringGenerator {

    private static final int SHORT_STR_LENGTH = 5;

    // 0..9
    // A..Z
    // a..z = 62
    private static final char[] ALPHABET = {
        '0', '1', '9',
        'A', 'B', 'Z',
        'a', 'b', 'z',
    };

    private static final Map<Character, Integer> INDEX_MAP = new HashMap<>();

    static {
        for (int i = 0; i < ALPHABET.length; ++i) {
            INDEX_MAP.put(ALPHABET[i], i);
        }
    }

    private static final char FIRST_CH = ALPHABET[0];
    private static final char LAST_CH = ALPHABET[ALPHABET.length - 1];

    private static final String FIRST_CH_AS_STR = String.valueOf(FIRST_CH);

    private static final String LAST_CH_AS_STR = String.valueOf(LAST_CH);

    private final Bucket[] buckets = new Bucket[ALPHABET.length];


    private int lastUsed = buckets.length - 1;

    public ShortStringGenerator() {
        for (int i = 0; i < ALPHABET.length; ++i) {
            buckets[i] = new Bucket(ALPHABET[i]);
        }
    }


    public String next() {
        final ThreadLocalRandom rand = ThreadLocalRandom.current();

        while (true) {
            int randIndex = rand.nextInt(buckets.length);

            Bucket randBucket = buckets[randIndex];

            randBucket.lock();

            try {
                if (randBucket.hasNext()) {
                    return randBucket.next();
                }
                else {
                    // move current bucket to not used space
                    System.out.printf("Bucket %s, will try another one.\n", randBucket);
                }
            }
            finally {
                randBucket.unlock();
            }

        }
    }

    private static class Bucket {

        private final Lock mutex = new ReentrantLock();
        private String cur;

        private final String first;

        private final String last;

        Bucket(char ch) {
            first = ch + FIRST_CH_AS_STR.repeat(SHORT_STR_LENGTH - 1);
            last = ch + LAST_CH_AS_STR.repeat(SHORT_STR_LENGTH - 1);
            cur = first;
        }

        @Override
        public String toString() {
            return "[" + first + "...." + cur + "..." + last + "]";
        }

        public boolean hasNext() {
            return !cur.equals(last);
        }

        public String next() {
            String retValue = cur;

            cur = inc(cur);

            return retValue;
        }

        String inc(String val) {

            char[] arr = val.toCharArray();

            for (int i = arr.length - 1; i >= 0; --i) {

                if (arr[i] == LAST_CH) {
                    arr[i] = FIRST_CH;
                }
                else {
                    int chIndex = INDEX_MAP.get(arr[i]);
                    arr[i] = ALPHABET[chIndex + 1];
                    break;
                }
            }

            return String.valueOf(arr);
        }

        public void lock() {
            mutex.lock();
        }

        public void unlock() {
            mutex.unlock();
        }
    }

    public static void main(String[] args) throws Exception {

        ShortStringGenerator generator = new ShortStringGenerator();

        Set<String> generatedValues = new ConcurrentSkipListSet<>();

//        System.out.println(Math.pow(9.0, 5.0));  ==> 59_049
        final int threadsCount = 59;
        final int itCount = 1000;

        CountDownLatch allCompleted = new CountDownLatch(threadsCount);
        ExecutorService pool = Executors.newFixedThreadPool(threadsCount);

        for (int i = 0; i < threadsCount; ++i) {
            pool.execute(() -> {
                try {
                    for (int it = 0; it < itCount; ++it) {
                        String val = generator.next();
                        boolean wasAdded = generatedValues.add(val);
                        if (!wasAdded) {
                            throw new IllegalStateException("Duplicate value detected during generation");
                        }
                    }
                }
                finally {
                    allCompleted.countDown();
                }
            });
        }
        allCompleted.await();
        pool.shutdownNow();

        System.out.printf("Generated unique values count: %d \n", generatedValues.size());

    }


}
