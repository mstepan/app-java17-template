package com.github.mstepan.app17;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.StampedLock;

class RWBankAccount {

    private final StampedLock rwLock = new StampedLock();

    int amount;

    public RWBankAccount(int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("amount can't be negative: %d".formatted(amount));
        }
        this.amount = amount;
    }

    public int getAmount() {
        final long timestamp = rwLock.tryOptimisticRead();

        final int amountCopy = amount;

        if (rwLock.validate(timestamp)) {
            return amountCopy;
        }

        final long readTimestamp = rwLock.readLock();
        try {
            return amount;
        } finally {
            rwLock.unlockRead(readTimestamp);
        }
    }

    public boolean withdraw(int value) {
        if (amount < 0) {
            throw new IllegalArgumentException("amount can't be negative: %d".formatted(amount));
        }

        final long timestamp = rwLock.writeLock();
        try {
            if (amount >= value) {
                Main.yieldCurThread();
                amount -= value;
                return true;
            }

            return false;
        } finally {
            rwLock.unlockWrite(timestamp);
        }
    }
}
