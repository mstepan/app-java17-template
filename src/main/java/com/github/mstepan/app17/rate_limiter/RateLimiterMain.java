package com.github.mstepan.app17.rate_limiter;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public class RateLimiterMain {

    static class RateLimitException extends RuntimeException {
        public RateLimitException(long expected, long actual) {
            super("rate limit exceeded: expected = " + expected + ", actual =  " + actual);
        }
    }

    public static void main(String[] args) throws Exception {

        ThreadLocalRandom rand = ThreadLocalRandom.current();
        ExecutorService pool = Executors.newFixedThreadPool(10);

        final AtomicLong inFlight = new AtomicLong(0L);
        final AtomicLong limit = new AtomicLong(Long.MAX_VALUE);

        final VegasCongestionLimiter limiter = new VegasCongestionLimiter();

        final AtomicLong rateLimitedEx = new AtomicLong(0L);

        for (int i = 0; i < 100_000; ++i) {
            pool.submit(
                    () -> {
                        long start = System.nanoTime() / 1_000_000L;

                        boolean rateLimited = false;
                        try {
                            long curInProgressCount = inFlight.incrementAndGet();
                            long rateLimit = limit.get();

                            if (curInProgressCount > rateLimit) {
                                rateLimited = true;
                                rateLimitedEx.incrementAndGet();
                                throw new RateLimitException(rateLimit, curInProgressCount);
                            }
                            TimeUnit.MILLISECONDS.sleep(10 + rand.nextInt(20));
                        } catch (InterruptedException interEx) {
                            Thread.currentThread().interrupt();
                        } finally {
                            if (!rateLimited) {
                                long end = System.nanoTime() / 1_000_000L;
                                limit.set(limiter.updateMtr(end - start));
                            }
                            inFlight.decrementAndGet();
                        }
                    });
        }

        TimeUnit.SECONDS.sleep(10);

        System.out.printf("rateLimitedEx: %d\n", rateLimitedEx.get());

        pool.shutdownNow();
        pool.awaitTermination(3L, TimeUnit.SECONDS);

        System.out.println("RateLimiterMain done...");
    }

    /**
     * https://vikas-kumar.medium.com/handling-overload-with-concurrency-control-and-load-shedding-part-2-6b8b594d4405
     */
    static class VegasCongestionLimiter {

        // reset 'MTR No Load' value every 1000 requests
        private static final long MRT_NO_LOAD_RESET_THRESHOLD = 1000L;

        private final AtomicLong mrtNoLoad = new AtomicLong(Long.MAX_VALUE);

        private final AtomicLong requestsCount = new AtomicLong(0L);

        private final AtomicLong concurrencyLimit = new AtomicLong(20L);

        public long updateMtr(long mrtCur) {

            long totalRequestsCount = requestsCount.incrementAndGet();

            if (totalRequestsCount == MRT_NO_LOAD_RESET_THRESHOLD) {
                mrtNoLoad.set(mrtCur);
                requestsCount.set(0);
                return concurrencyLimit.get();
            } else {
                while (true) {
                    long mtrNoLoadValue = mrtNoLoad.get();
                    if (mrtCur < mtrNoLoadValue) {
                        if (mrtNoLoad.compareAndSet(mtrNoLoadValue, mrtCur)) {
                            return concurrencyLimit.get();
                        }
                    } else {
                        return adjustConcurrencyLimit(mrtCur);
                    }
                }
            }
        }

        private long adjustConcurrencyLimit(long mrtCur) {

            while (true) {
                final long curLimit = concurrencyLimit.get();
                final long queueSize =
                        (long) (curLimit * (1.0 - ((double) mrtNoLoad.get() / mrtCur)));
                final long alpha = (long) (3 * Math.log10(curLimit));
                final long beta = (long) (6 * Math.log10(curLimit));
                final long threshold = (long) Math.log(curLimit);

                // No Queuing(increase limit aggressive): curLimit = curLimit + beta
                if (queueSize <= threshold) {
                    if (tryUpdateLimit(curLimit, curLimit + beta)) {
                        return curLimit + beta;
                    }
                }
                // Manageable Queue(slightly increase limit): curLimit = curLimit + LOG10(curLimit)
                else if (queueSize < alpha) {
                    if (tryUpdateLimit(curLimit, curLimit + (long) Math.log(curLimit))) {
                        return curLimit + (long) Math.log(curLimit);
                    }
                }
                // Excessive Queuing (decrease limit): curLimit = curLimit - LOG10(curLimit)
                else if (queueSize > beta) {
                    if (tryUpdateLimit(curLimit, curLimit - (long) Math.log(curLimit))) {
                        return curLimit - (long) Math.log(curLimit);
                    }
                }
                // We have found SWEET spot, nothing to do here
                else {
                    return curLimit;
                }
            }
        }

        private boolean tryUpdateLimit(long curLimit, long newLimit) {
            if (concurrencyLimit.compareAndSet(curLimit, newLimit)) {
                return true;
            }
            return false;
        }
    }
}
