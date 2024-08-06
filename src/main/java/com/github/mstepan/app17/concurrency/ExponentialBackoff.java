package com.github.mstepan.app17.concurrency;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public final class ExponentialBackoff {

    private final TimeUnit timeUnit;

    private final int maxDelay;

    private int limit;

    private final Random rand;

    public ExponentialBackoff(int minDelay, int maxDelay, TimeUnit timeUnit) {
        if (minDelay > maxDelay) {
            throw new IllegalArgumentException(
                    "minDelay > maxDelay, %d > %d".formatted(minDelay, maxDelay));
        }
        this.limit = minDelay;
        this.maxDelay = maxDelay;
        this.timeUnit = timeUnit;
        this.rand = ThreadLocalRandom.current();
    }

    public ExponentialBackoff(int minDelay, int maxDelay) {
        this(minDelay, maxDelay, TimeUnit.MILLISECONDS);
    }

    public ExponentialBackoff() {
        this(10, 250, TimeUnit.MILLISECONDS);
    }

    public void backoff() throws InterruptedException {
        timeUnit.sleep(nextDelay());
    }

    private int nextDelay() {
        int curDelay = rand.nextInt(limit);
        limit = Math.min(2 * limit, maxDelay);
        return curDelay;
    }
}
