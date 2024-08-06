package com.github.mstepan.app17.concurrency.locks;

import com.github.mstepan.app17.concurrency.ExponentialBackoff;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/** Test-Test and Set lock with exponential backoff. */
public final class BackoffLock implements Lock {

    private final ExponentialBackoff backoff = new ExponentialBackoff();

    /**
     * 'true' - occupied
     *
     * <p>'false' - free to use
     */
    private final AtomicBoolean status = new AtomicBoolean(false);

    @Override
    public void lock() {

        while (true) {
            // Test - Test and Set approach
            while (status.get()) {
                Thread.onSpinWait();
            }

            if (!status.getAndSet(true)) {
                return;
            } else {
                try {
                    backoff.backoff();
                } catch (InterruptedException interEx) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    @Override
    public void unlock() {
        status.set(false);
    }
}
