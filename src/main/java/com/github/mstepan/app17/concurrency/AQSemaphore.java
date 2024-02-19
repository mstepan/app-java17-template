package com.github.mstepan.app17.concurrency;

import java.util.concurrent.locks.AbstractQueuedSynchronizer;

/** Standard semaphore implemented using AbstractQueuedSynchronizer class. */
public final class AQSemaphore {

    private final Synch synch;

    public AQSemaphore(int permits) {
        this.synch = new Synch(permits);
    }

    public void acquire() throws InterruptedException {
        acquire(1);
    }

    public void acquire(int count) throws InterruptedException {
        synch.acquireShared(count);
    }

    public void release() {
        release(1);
    }

    public void release(int count) {
        synch.releaseShared(count);
    }

    private static final class Synch extends AbstractQueuedSynchronizer {

        public Synch(int permits) {
            setState(permits);
        }

        @Override
        protected int tryAcquireShared(int count) {
            final int permits = getState();

            if (permits < count) {
                return -1;
            }

            if (compareAndSetState(permits, permits - count)) {
                return permits - count;
            }

            return -1;
        }

        @Override
        protected boolean tryReleaseShared(int count) {
            while (true) {
                int permits = getState();

                if (compareAndSetState(permits, permits + count)) {
                    break;
                }
            }

            return true;
        }
    }
}
