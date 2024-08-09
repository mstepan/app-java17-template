package com.github.mstepan.app17.concurrency.locks;

import com.github.mstepan.app17.concurrency.atomic.AtomicBooleanArray;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/** Two threads spin lock solution. */
public final class PetersonLock implements Lock {

    private final AtomicBooleanArray flag = new AtomicBooleanArray(2);
    private volatile int victim;

    @Override
    public void lock() {
        int i = Integer.parseInt(Thread.currentThread().getName());
        int j = 1 - i;

        flag.set(i);
        victim = i;

        while (flag.get(j) && victim == i) {
            Thread.onSpinWait();
        }
    }

    @Override
    public void unlock() {
        int i = Integer.parseInt(Thread.currentThread().getName());
        flag.clear(i);
    }

    public static volatile int counter = 0;

    public static void main(String[] args) throws Exception {

        PetersonLock mutex = new PetersonLock();

        List<Callable<Void>> tasks = new ArrayList<>();

        for (int i = 0; i < 2; ++i) {

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

        System.out.println("PetersonLock done...");
    }
}
