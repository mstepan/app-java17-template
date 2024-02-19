package com.github.mstepan.app17.concurrency;

/**
 * Classic semaphore implementation using synchronized blocks and wait/notify conditions variables.
 */
public final class ClassicSemaphore {

    private final Object lock = new Object();

    private final int totalPermits;

    private int availablePermits;

    public ClassicSemaphore(int permits) {
        if (permits <= 0) {
            throw new IllegalArgumentException(
                    "Can't create semaphore with negative permits count: %d".formatted(permits));
        }
        this.totalPermits = permits;
        this.availablePermits = permits;
    }

    public void acquire() throws InterruptedException {
        acquire(1);
    }

    public void acquire(int requestedPermits) throws InterruptedException {
        if (requestedPermits > totalPermits) {
            throw new IllegalArgumentException(
                    "Requested permits count > totalPermits for semaphore: %d > %d"
                            .formatted(requestedPermits, totalPermits));
        }

        synchronized (lock) {
            while (availablePermits < requestedPermits) {
                lock.wait();
            }

            availablePermits -= requestedPermits;

            assert availablePermits >= 0;
        }
    }

    public void release() {
        release(1);
    }

    public void release(int count) {
        if (count > totalPermits) {
            throw new IllegalArgumentException(
                    "Release permits count > totalPermits for semaphore: %d > %d"
                            .formatted(count, totalPermits));
        }

        synchronized (lock) {
            availablePermits += count;

            assert availablePermits <= totalPermits;

            lock.notifyAll();
        }
    }
}
