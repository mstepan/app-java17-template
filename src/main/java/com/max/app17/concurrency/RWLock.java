package com.max.app17.concurrency;

import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/** Classic read-write lock with READERs preference (in other words writers will starve) */
public final class RWLock {

    private final Lock readLock = new ReentrantLock();

    private int readersCount;

    /**
     * We can't use 'ReentrantLock' here instead of Semaphore, b/c ReentrantLock will throw
     * IllegalMonitorStateException if released from different thread (the thread that has not
     * acquired the lock before)
     */
    private final Semaphore writeSemaphore = new Semaphore(1);

    void readLock() throws InterruptedException {
        readLock.lock();

        try {
            readersCount += 1;
            if (readersCount == 1) {
                writeSemaphore.acquire();
            }
        } finally {
            readLock.unlock();
        }
    }

    void readUnlock() {
        readLock.lock();

        try {
            readersCount -= 1;
            if (readersCount == 0) {
                writeSemaphore.release();
            }
        } finally {
            readLock.unlock();
        }
    }

    void writeLock() throws InterruptedException {
        writeSemaphore.acquire();
    }

    void writeUnlock() {
        writeSemaphore.release();
    }
}
