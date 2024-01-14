package com.max.app17;

import com.max.app17.concurrency.AQSemaphore;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class Main {

    public static void main(String[] args) throws Exception {

        AQSemaphore semaphore = new AQSemaphore(4);
        ExecutorService pool = Executors.newCachedThreadPool();

        int threadsCount = 10;
        List<Callable<Void>> allTasks = new ArrayList<>();

        for (int i = 0; i < threadsCount; ++i) {
            allTasks.add(new MyTask(semaphore));
        }

        List<Future<Void>> futureResults = pool.invokeAll(allTasks);

        for (Future<Void> singleRes : futureResults) {
            singleRes.get();
        }

        pool.shutdownNow();

        System.out.println("Main done...");
    }

    private static final class MyTask implements Callable<Void> {

        final AQSemaphore semaphore;

        public MyTask(AQSemaphore semaphore) {
            this.semaphore = semaphore;
        }

        @Override
        public Void call() {
            try {
                semaphore.acquire(2);

                System.out.printf("thread: %d started%n", Thread.currentThread().getId());

                TimeUnit.SECONDS.sleep(1 + ThreadLocalRandom.current().nextInt(5));

            } catch (InterruptedException interEx) {
                Thread.currentThread().interrupt();
            } finally {
                System.out.printf("thread: %d completed%n", Thread.currentThread().getId());
                semaphore.release(2);
            }

            return null;
        }
    }
}
