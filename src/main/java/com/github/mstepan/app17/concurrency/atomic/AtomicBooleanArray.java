package com.github.mstepan.app17.concurrency.atomic;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;

/**
 * Atomic boolean array similar to AtomicIntegerArray. Uses VarHandle to get/set array elements
 * with volatile semantic.
 */
public final class AtomicBooleanArray {

    private final boolean[] arr;

    private final VarHandle arrayHandler = MethodHandles.arrayElementVarHandle(boolean[].class);

    public AtomicBooleanArray(int length) {
        this.arr = new boolean[length];
    }

    public void set(int idx) {
        arrayHandler.setVolatile(arr, idx, true);
    }

    public void clear(int idx) {
        arrayHandler.setVolatile(arr, idx, false);
    }

    public boolean get(int idx) {
        return (boolean) arrayHandler.getVolatile(arr, idx);
    }

    public int length() {
        return arr.length;
    }
}
