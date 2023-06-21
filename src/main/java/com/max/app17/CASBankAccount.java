package com.max.app17;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;

public class CASBankAccount {

//    private static final Unsafe UNSAFE = createUnsafe();

    private static final VarHandle BALANCE_HANDLE;

    static {
        try {
            BALANCE_HANDLE = MethodHandles.lookup().findVarHandle(CASBankAccount.class, "balance", int.class);
        }
        catch (Exception ex) {
            throw new ExceptionInInitializerError(ex);
        }

//        try {
//            BALANCE_OFFSET = UNSAFE.objectFieldOffset(CASBankAccount.class.getDeclaredField("balance"));
//        }
//        catch (Exception ex) {
//            throw new ExceptionInInitializerError(ex);
//        }
    }

    public volatile int balance;
//    private static final long BALANCE_OFFSET;

    public CASBankAccount(int initialBalance) {
        this.balance = initialBalance;
    }

    public int getBalance() {
        return balance;
    }

    public void add(int amount) {
        checkNotNegative(amount >= 0, "Can't add negative amount: " + amount);

        BALANCE_HANDLE.getAndAdd(this, amount);

//        UNSAFE.getAndAddInt(this, BALANCE_OFFSET, amount);
    }

    public boolean withdraw(int amount) {
        checkNotNegative(amount >= 0, "Can't withdraw negative amount: " + amount);
        int newBalance = balance - amount;
        if (newBalance >= 0) {

            if (BALANCE_HANDLE.compareAndSet(this, balance, newBalance)) {
                return true;
            }

//            if (UNSAFE.compareAndSwapInt(this, BALANCE_OFFSET, balance, newBalance)) {
//                return true;
//            }
        }
        return false;
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

//    private static Unsafe createUnsafe() {
//        try {
//            Field theUnsafe = Unsafe.class.getDeclaredField("theUnsafe");
//            theUnsafe.setAccessible(true);
//            return (Unsafe) theUnsafe.get(null);
//        }
//        catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException ex) {
//            throw new IllegalStateException(ex);
//        }
//    }
}
