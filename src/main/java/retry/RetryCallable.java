package retry;

import java.util.Objects;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;


/**
 * Decorator that wraps original Callable<T> using exponential backoff retry policy.
 */
public final class RetryCallable<T> implements Callable<T> {

    // 200 ms
    private static final int DEFAULT_INITIAL_DELAY_IN_MS = 200;

    // 16 seconds
    private static final long MAX_POSSIBLE_DELAY_IN_MS = 16_000L;

    // by default will retry 10 times
    private static final int DEFAULT_MAX_RETRY_COUNT = 10;

    private final Callable<T> original;
    private final int initialDelayInMs;
    private final int maxRetryCount;

    public RetryCallable(Callable<T> original) {
        this(original, DEFAULT_INITIAL_DELAY_IN_MS, DEFAULT_MAX_RETRY_COUNT);
    }

    public RetryCallable(Callable<T> original, int initialDelayInMs, int maxRetryCount) {
        this.original = Objects.requireNonNull(original, "Can't decorate null Callable with retry logic");

        checkArgument(initialDelayInMs > 0, "Negative or zero initial delay detected: " + initialDelayInMs);
        this.initialDelayInMs = initialDelayInMs;

        checkArgument(maxRetryCount >= 0, "maxRetryCount can't be negative: " + maxRetryCount);
        this.maxRetryCount = maxRetryCount;
    }

    private static void checkArgument(boolean predicate, String errorMsg) {
        if (!predicate) {
            throw new IllegalArgumentException(errorMsg);
        }
    }


    @Override
    public T call() {
        final Random rand = ThreadLocalRandom.current();

        long curDelay = initialDelayInMs;

        for (int count = 0; count < maxRetryCount && !Thread.currentThread().isInterrupted(); ++count) {
            try {
                T result = original.call();
                System.out.printf("thread-%d successfully completed%n", Thread.currentThread().getId());
                return result;
            }
            catch (Exception ex) {
                boolean wasInterrupted = sleepMs(curDelay);
                if (wasInterrupted) {
                    return null;
                }
                curDelay = Math.min(MAX_POSSIBLE_DELAY_IN_MS, nextDelay(curDelay, rand));
            }
        }
        throw new IllegalStateException("Can't complete call after " + maxRetryCount + " attempts.");
    }

    private long nextDelay(long curDelay, Random rand) {
        return 2 * (curDelay + rand.nextInt(initialDelayInMs));
    }

    private static boolean sleepMs(long timeInMs) {
        try {
            System.out.printf("thread-%d sleeping for %d%n", Thread.currentThread().getId(), timeInMs);
            TimeUnit.MILLISECONDS.sleep(timeInMs);
        }
        catch (InterruptedException interEx) {
            Thread.currentThread().interrupt();
            return true;
        }
        return false;
    }
}
