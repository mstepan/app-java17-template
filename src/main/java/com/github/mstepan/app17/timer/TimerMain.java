package com.github.mstepan.app17.timer;

import java.time.Instant;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class TimerMain {

    public static void main(String[] args) throws Exception {
        final HashedHierarchicalTimingWheels timeWheels = HashedHierarchicalTimingWheels.newInstance();

        final Instant now = Instant.now();

        final int threadsCount = 200;
        final int callbacksCountPerThread = 10_000;
        final int maxPossibleDelayInSec = 30;

        final CountDownLatch allCompleted = new CountDownLatch(threadsCount);

        ExecutorService pool = Executors.newFixedThreadPool(threadsCount);

        final AtomicInteger counter = new AtomicInteger(0);

        for (int th = 0; th < threadsCount; ++th) {
            pool.execute(
                    () -> {
                        try {
                            ThreadLocalRandom rand = ThreadLocalRandom.current();

                            for (int it = 0; it < callbacksCountPerThread; ++it) {
                                int delayInSec = 1 + rand.nextInt(maxPossibleDelayInSec);

                                timeWheels.addCallback(
                                        now.plusSeconds(delayInSec), counter::incrementAndGet);
                            }
                        } finally {
                            allCompleted.countDown();
                        }
                    });
        }

        allCompleted.await();

        System.out.println("All callbacks added");

        pool.shutdownNow();

        TimeUnit.SECONDS.sleep(maxPossibleDelayInSec + 5);

        if (counter.get() != threadsCount * callbacksCountPerThread) {
            throw new AssertionError(
                    "Counter value is incorrect, expected = %d, actual = %d"
                            .formatted(threadsCount * callbacksCountPerThread, counter.get()));
        }

        System.out.printf("counter: %d%n", counter.get());

        System.out.println("TimerMain done...");
    }
}
