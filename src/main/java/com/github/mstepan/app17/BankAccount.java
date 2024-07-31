package com.github.mstepan.app17;

class BankAccount {

    private final Object mutex = new Object();

    int amount;

    public BankAccount(int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("amount can't be negative: %d".formatted(amount));
        }
        this.amount = amount;
    }

    public int getAmount() {
        synchronized (mutex) {
            return amount;
        }
    }

    public boolean withdraw(int value) {
        synchronized (mutex) {
            if (amount >= value) {
                Main.yieldCurThread();
                amount -= value;
                return true;
            }

            return false;
        }
    }
}

