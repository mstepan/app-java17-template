package com.github.mstepan.app17.concurrency.locks;

import com.github.mstepan.app17.concurrency.locks.Lock;

import java.util.concurrent.atomic.AtomicBoolean;

public final class SpinLock implements Lock {

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
            }
        }
    }

    @Override
    public void unlock() {
        status.set(false);
    }
}
