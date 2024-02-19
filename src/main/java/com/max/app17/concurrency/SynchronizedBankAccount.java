package com.max.app17.concurrency;

public class SynchronizedBankAccount {

    private final Object mutex = new Object();

    public volatile int balance;

    public SynchronizedBankAccount(int initialBalance) {
        this.balance = initialBalance;
    }

    public int getBalance() {
        return balance;
    }

    public void add(int amount) {
        checkNotNegative(amount >= 0, "Can't add negative amount: " + amount);
        synchronized (mutex) {
            balance += amount;
        }
    }

    public boolean withdraw(int amount) {
        checkNotNegative(amount >= 0, "Can't withdraw negative amount: " + amount);
        synchronized (mutex) {
            int newBalance = balance - amount;
            if (newBalance >= 0) {
                balance = newBalance;
                return true;
            }
            return false;
        }
    }

    private void checkNotNegative(boolean predicate, String errorMsg) {
        if (!predicate) {
            throw new IllegalStateException(errorMsg);
        }
    }

    @Override
    public String toString() {
        return "balance = " + balance;
    }
}
