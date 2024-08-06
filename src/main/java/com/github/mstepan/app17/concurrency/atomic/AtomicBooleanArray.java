package com.github.mstepan.app17.concurrency.atomic;

import java.util.concurrent.atomic.AtomicIntegerArray;

/** Atomic boolean array similar to AtomicIntegerArray */
public final class AtomicBooleanArray {

    private final AtomicIntegerArray arr;

    public AtomicBooleanArray(int length) {
        this.arr = new AtomicIntegerArray(length);
    }

    public void set(int idx) {
        arr.set(idx, 1);
    }

    public void clear(int idx) {
        arr.set(idx, 0);
    }

    public boolean get(int idx) {
        return arr.get(idx) == 1;
    }
}
