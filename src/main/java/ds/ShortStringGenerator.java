package ds;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Short string generator. Can be used for highly concurrent URL shortening service.
 */
public class ShortStringGenerator {

    private static final int SHORT_STR_LENGTH = 6;

    // [0..9] + [A..Z] + [a..z] = 62
    private static final char[] ALPHABET = {
        '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',

        'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O',
        'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',

        'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o',
        'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'
    };

    // 62 ^ 6 = 56_800_235_584
    private static final double TOTAL_POSSIBLE_ELEMENTS_COUNT = Math.pow(ALPHABET.length, SHORT_STR_LENGTH);

    /**
     * Percentage of allowed filled buckets count. As soon as the threshold will be reached, the call to 'next' method will
     * throw an 'IllegalStateException'.
     */
    private static final double PERCENTAGE_FAILURE_THRESHOLD = 75.0;

    /**
     * Maps 'char' back to 'int' index.
     */
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

    /**
     * Create bucket for every character in ALPHABET to reduce thread contention.
     */
    private final Bucket[] buckets = new Bucket[ALPHABET.length];

    private final AtomicInteger generatedElementsCount = new AtomicInteger(0);


    // TODO: below metrics should be removed after testing

    private static final AtomicLong CONTENTIONS_COUNT = new AtomicLong(0L);

    private final AtomicInteger collisionsCount = new AtomicInteger(0);

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

            // null value means bucket is fully completed
            String value = randBucket.next();

            if (value != null) {
                generatedElementsCount.incrementAndGet();
                return value;
            }

            collisionsCount.incrementAndGet();

            double usedPercentage = Math.round((generatedElementsCount.get() * 100.0) / TOTAL_POSSIBLE_ELEMENTS_COUNT);

            if (Double.compare(usedPercentage, PERCENTAGE_FAILURE_THRESHOLD) > 0) {
                throw new IllegalStateException(
                    "Used buckets percentage is too high: expected maximum = %.0f, actual = %.0f" .
                        formatted(PERCENTAGE_FAILURE_THRESHOLD, usedPercentage));
            }
            // move current bucket to not used space
//            System.out.printf("Bucket %s, will try another one.\n", randBucket);
        }
    }

    private static class Bucket {

        /*
         'cur' should be accessed through 'CUR_HANDLE' only
         */
        private String cur;

        private static final VarHandle CUR_HANDLE;

        static {
            try {
                CUR_HANDLE = MethodHandles
                    .privateLookupIn(Bucket.class, MethodHandles.lookup())
                    .findVarHandle(Bucket.class, "cur", String.class);

            }
            catch (ReflectiveOperationException ex) {
                throw new ExceptionInInitializerError("Can't obtain VarHandle for 'cur' String field");
            }
        }

        private final String first;

        private final String last;

        Bucket(char ch) {
            first = ch + FIRST_CH_AS_STR.repeat(SHORT_STR_LENGTH - 1);
            last = ch + LAST_CH_AS_STR.repeat(SHORT_STR_LENGTH - 1);
            CUR_HANDLE.set(this, first);
        }

        @Override
        public String toString() {
            return "[" + first + "...." + CUR_HANDLE.get(this) + "..." + last + "]";
        }

        public String next() {
            return incAndGet();
        }

        String incAndGet() {

            while (true) {
                String val = (String) CUR_HANDLE.get(this);

                if (val.equals(last)) {
                    return null;
                }

                String nextValue = nextValue(val);

                if (CUR_HANDLE.compareAndSet(this, val, nextValue)) {
                    return val;
                }
                else {
                    CONTENTIONS_COUNT.incrementAndGet();
                }
            }
        }

        String nextValue(String val) {
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
    }

    public static void main(String[] args) throws Exception {

        long usedMemoryBefore = usedMemory();

        ShortStringGenerator generator = new ShortStringGenerator();

        Set<String> generatedValues = new ConcurrentSkipListSet<>();

        final int threadsCount = 100;
        final int itCount = 400_000;

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

        long usedMemoryAfter = usedMemory();

        System.out.printf("RAM used: %.2f GB\n", (usedMemoryAfter - usedMemoryBefore) / (1024.0 * 1024.0 * 1024.0));
        System.out.printf("Collisions count: %d\n", generator.collisionsCount.get());
        System.out.printf("Contentions count: %d\n", ShortStringGenerator.CONTENTIONS_COUNT.get());

        System.out.printf("Generated unique values count: %d \n", generatedValues.size());

    }

    private static long usedMemory() {
        Runtime runtime = Runtime.getRuntime();
        return runtime.totalMemory() - runtime.freeMemory();
    }


}
