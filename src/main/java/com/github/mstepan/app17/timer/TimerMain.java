package com.github.mstepan.app17.timer;

import java.time.Instant;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class TimerMain {

    public static void main(String[] args) throws Exception {
        HashedHierarchicalTimingWheels timeWheels = HashedHierarchicalTimingWheels.newInstance();

        Instant now = Instant.now();

        final int threadsCount = 5;
        final CountDownLatch allCompleted = new CountDownLatch(threadsCount);

        ExecutorService pool = Executors.newFixedThreadPool(threadsCount);

        for (int th = 0; th < threadsCount; ++th) {
            pool.execute(
                    () -> {
                        try {
                            for (int delay = 5; delay < 100; delay += 5) {
                                final int delayInSec = delay;

                                timeWheels.addCallback(
                                        now.plusSeconds(delayInSec),
                                        () -> System.out.printf("%d seconds%n", delayInSec));
                            }
                        } finally {
                            allCompleted.countDown();
                        }
                    });
        }

        allCompleted.await();

        System.out.println("All callbacks added");

        pool.shutdownNow();

        TimeUnit.MINUTES.sleep(1);

        System.out.println("TimerMain done...");
    }
}
