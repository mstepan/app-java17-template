package com.github.mstepan.app17;

import java.util.concurrent.atomic.AtomicInteger;
import org.jetbrains.annotations.NotNull;

class BankAccountNonBlocking {

    @NotNull final AtomicInteger amount;

    public BankAccountNonBlocking(int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("amount can't be negative: %d".formatted(amount));
        }
        this.amount = new AtomicInteger(amount);
    }

    public int getAmount() {
        return amount.get();
    }

    public boolean withdraw(int value) {

        if (value < 0) {
            throw new IllegalArgumentException(
                    "Can't withdraw negative value: %d".formatted(value));
        }

        while (true) {
            int prevAmount = amount.get();

            Main.yieldCurThread();

            if (prevAmount < value) {
                return false;
            }

            int newAmount = prevAmount - value;

            if (amount.compareAndSet(prevAmount, newAmount)) {
                return true;
            }
        }
    }
}
