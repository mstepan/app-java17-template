package com.max.app17.concurrency;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.LockSupport;

public final class SimpleSemaphore {

    private final AtomicInteger permitsCount;

    private final ConcurrentMap<Long, Thread> waitingThreads = new ConcurrentHashMap<>();

    public SimpleSemaphore(int permitsCount) {
        this.permitsCount = new AtomicInteger(permitsCount);
    }

    public void acquire() {

        while (true) {

            int curPermCount = permitsCount.get();

            while (curPermCount == 0) {
                waitingThreads.put(Thread.currentThread().getId(), Thread.currentThread());
                LockSupport.park();
                curPermCount = permitsCount.get();
                waitingThreads.remove(Thread.currentThread().getId());
            }

            if (permitsCount.compareAndSet(curPermCount, curPermCount - 1)) {
                return;
            }
        }
    }

    public void release() {
        while (true) {
            int curPermCnt = permitsCount.get();

            if (permitsCount.compareAndSet(curPermCnt, curPermCnt + 1)) {
                for (Thread singleWaitingThread : waitingThreads.values()) {
                    LockSupport.unpark(singleWaitingThread);
                }
                break;
            }
        }
    }


    public static void main(String[] args) throws Exception {

        int threadsCount = 128;

        final SimpleSemaphore semaphore = new SimpleSemaphore(5);

        final CountDownLatch alCompleted = new CountDownLatch(threadsCount);

        ExecutorService pool = Executors.newFixedThreadPool(threadsCount);

        final AtomicInteger activeThreads = new AtomicInteger(0);
        final AtomicInteger maxActiveThreadsCount = new AtomicInteger(0);

        for (int i = 0; i < threadsCount; ++i) {
            pool.submit(
                    () -> {
                        try {
                            for (int it = 0;
                                 it < 10000 && !Thread.currentThread().isInterrupted();
                                 ++it) {
                                semaphore.acquire();
                                try {
                                    recordMaxActiveThreadsCount(maxActiveThreadsCount, activeThreads.incrementAndGet());
                                } finally {
                                    recordMaxActiveThreadsCount(maxActiveThreadsCount, activeThreads.decrementAndGet());
                                    semaphore.release();
                                }
                            }
                        } finally {
                            alCompleted.countDown();
                        }
                    });
        }
        alCompleted.await();

        System.out.printf("max active threads count: %d%n", maxActiveThreadsCount.get());

        pool.shutdownNow();
        pool.awaitTermination(1L, TimeUnit.SECONDS);

        System.out.println("SimpleSemaphore main done...");
    }

    private static void recordMaxActiveThreadsCount(AtomicInteger maxActiveThreadsCount, int curValue) {
        while (true) {
            int maxSoFar = maxActiveThreadsCount.get();
            if (maxSoFar >= curValue) {
                break;
            }

            if (maxActiveThreadsCount.compareAndSet(maxSoFar, curValue)) {
                break;
            }
        }
    }

}
