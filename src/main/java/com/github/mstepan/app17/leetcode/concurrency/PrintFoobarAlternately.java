package com.github.mstepan.app17.leetcode.concurrency;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * 1115. Print FooBar Alternately
 *
 * <p>https://leetcode.com/problems/print-foobar-alternately/
 */
public class PrintFoobarAlternately {

    static class FooBar {

        public final Semaphore FOO_SEM = new Semaphore(1);
        public final Semaphore BAR_SEM = new Semaphore(0);

        private final int n;

        public FooBar(int n) {
            this.n = n;
        }

        public void foo(Runnable printFoo) throws InterruptedException {
            for (int i = 0; i < n; i++) {
                try {
                    FOO_SEM.acquire();
                    printFoo.run();
                    BAR_SEM.release();
                } catch (InterruptedException interEx) {
                    Thread.currentThread().interrupt();
                }
            }
        }

        public void bar(Runnable printBar) throws InterruptedException {
            for (int i = 0; i < n; i++) {
                try {
                    BAR_SEM.acquire();
                    printBar.run();
                    FOO_SEM.release();
                } catch (InterruptedException interEx) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    public static void main(String[] args) {

        final FooBar obj = new FooBar(2);

        final CountDownLatch allCompleted = new CountDownLatch(2);
        ExecutorService pool = Executors.newFixedThreadPool(2);

        try {
            pool.execute(
                    () -> {
                        try {
                            obj.foo(() -> System.out.print("foo"));
                        } catch (InterruptedException interEx) {
                            Thread.currentThread().interrupt();
                            ;
                        } finally {
                            allCompleted.countDown();
                        }
                    });
            pool.execute(
                    () -> {
                        try {
                            obj.bar(() -> System.out.print("bar"));
                        } catch (InterruptedException interEx) {
                            Thread.currentThread().interrupt();
                            ;
                        } finally {
                            allCompleted.countDown();
                        }
                    });

            allCompleted.await();
        } catch (InterruptedException interEx) {
            Thread.currentThread().interrupt();
        } finally {
            pool.shutdownNow();
            // pool.awaitTermination(1L, TimeUnit.SECONDS)
        }
    }
}
