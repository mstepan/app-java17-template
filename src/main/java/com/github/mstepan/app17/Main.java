package com.github.mstepan.app17;

import com.github.mstepan.app17.concurrency.locks.Lock;
import com.github.mstepan.app17.concurrency.locks.QueueLock;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/*

java.util.concurrent.locks.ReentrantLock: <-- BASELINE
Elapsed time: 6_004 ms

java.util.concurrent.locks.ReentrantLock, FAIR lock: <-- BASELINE
Elapsed time:  ms

BackoffLock:
Elapsed time: 3_540 ms

SpinLock:
Elapsed time: Infinity ms

ArrayLock(64), FAIR lock:
Elapsed time: Infinity ms

ArrayLock(1024), FAIR lock:
Elapsed time: Infinity ms

SimpleLock:
Elapsed time: Infinity ms
 */
public class Main {

    public static int counter = 0;

    private static final int THREADS_COUNT = 20;

    private static final int ITERATIONS_COUNT = 500;

    public static void main(String[] args) throws Exception {

        //                  java.util.concurrent.locks.Lock mutex = new
        //                 java.util.concurrent.locks.ReentrantLock(true);

        Lock mutex = new QueueLock();

        List<Callable<Void>> tasks = new ArrayList<>();

        for (int i = 0; i < THREADS_COUNT; ++i) {
            tasks.add(
                    () -> {
                        for (int it = 0;
                                it < ITERATIONS_COUNT && !Thread.currentThread().isInterrupted();
                                ++it) {

                            mutex.lock();

                            try {
                                counter += 1;
                            } finally {
                                mutex.unlock();
                            }
                        }
                        return null;
                    });
        }

        ExecutorService pool = Executors.newCachedThreadPool();

        long startTime = System.nanoTime();
        List<Future<Void>> futures = pool.invokeAll(tasks);

        for (Future<Void> singleFuture : futures) {
            singleFuture.get();
        }

        long endTime = System.nanoTime();
        pool.shutdownNow();

        System.out.printf("Counter: %d%n", counter);
        System.out.printf(
                "Elapsed time: %d ms%n",
                Duration.of(endTime - startTime, ChronoUnit.NANOS).toMillis());

        System.out.println("Main done...");
    }
}
