package leetcode.concurrency;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * https://leetcode.com/problems/print-foobar-alternately/
 */
public class PrintFoobarAlternately {

    static class FooBar {

        public final Semaphore FOO_SEM = new Semaphore(1);
        public final Semaphore BAR_SEM = new Semaphore(0);

        private final int n;

        public FooBar(int n) {
            this.n = n;
        }

        public void foo() {
            for (int i = 0; i < n && !Thread.currentThread().isInterrupted(); i++) {
                try {
                    FOO_SEM.acquire();
                    System.out.print("foo");
                    BAR_SEM.release();
                }
                catch (InterruptedException interEx) {
                    Thread.currentThread().interrupt();
                }
            }
        }

        public void bar() {
            for (int i = 0; i < n; i++) {
                try {
                    BAR_SEM.acquire();
                    System.out.print("bar|");
                    FOO_SEM.release();
                }
                catch (InterruptedException interEx) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    public static void main(String[] args) {

        final FooBar obj = new FooBar(5);

        final CountDownLatch allCompleted = new CountDownLatch(2);
        ExecutorService pool = Executors.newFixedThreadPool(2);

        try {
            pool.execute(() -> {
                try {
                    obj.foo();
                }
                finally {
                    allCompleted.countDown();
                }
            });
            pool.execute(() -> {
                try {
                    obj.bar();
                }
                finally {
                    allCompleted.countDown();
                }
            });

            allCompleted.await();
        }
        catch (InterruptedException interEx) {
            Thread.currentThread().interrupt();
        }
        finally {
            pool.shutdownNow();
            //pool.awaitTermination(1L, TimeUnit.SECONDS)
        }

    }
}
