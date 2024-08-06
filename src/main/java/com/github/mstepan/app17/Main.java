package com.github.mstepan.app17;

import com.github.mstepan.app17.concurrency.locks.BackoffLock;
import com.github.mstepan.app17.concurrency.locks.Lock;
import com.github.mstepan.app17.concurrency.locks.SpinLock;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Main {

    public static volatile int counter = 0;

    public static void main(String[] args) throws Exception {

        Lock mutex = new BackoffLock();

        List<Callable<Void>> tasks = new ArrayList<>();

        for (int i = 0; i < 200; ++i) {

            final int idx = i;
            tasks.add(
                    () -> {
                        Thread.currentThread().setName(idx == 0 ? "0" : "1");

                        for (int it = 0;
                                it < 1_000_000 && !Thread.currentThread().isInterrupted();
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

        ExecutorService pool = Executors.newFixedThreadPool(2);
        List<Future<Void>> futures = pool.invokeAll(tasks);

        for (Future<Void> singleFuture : futures) {
            singleFuture.get();
        }

        pool.shutdownNow();

        System.out.printf("counter: %d%n", counter);

        System.out.println("Main done...");
    }
}
