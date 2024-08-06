package com.github.mstepan.app17.concurrency.locks;

import com.github.mstepan.app17.concurrency.atomic.AtomicBooleanArray;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Array based lock that provides FAIR lock (locks acquired in FIFO order). To reduce false sharing
 * we need to use padding that prevents CACHE line invalidation.
 *
 * <p>sysctl -a | grep cachelinesize hw.cachelinesize: 64 bytes
 */
public final class ArrayLock implements Lock {

    private static final int CACHE_LINE_SIZE_IN_BYTES = 64;

    private static final int SINGLE_BOOLEAN_SIZE_IN_BYTES = 1;

    private static final int CACHE_LINE_OFFSET =
            CACHE_LINE_SIZE_IN_BYTES / SINGLE_BOOLEAN_SIZE_IN_BYTES;

    private final AtomicBooleanArray flags;
    private final int slotsLength;

    private final AtomicInteger slot = new AtomicInteger(0);

    private static final ThreadLocal<Integer> LOCAL_SLOT = ThreadLocal.withInitial(() -> 0);

    public ArrayLock() {
        this(64);
    }

    public ArrayLock(int capacity) {
        if (capacity < 2) {
            throw new IllegalArgumentException(
                    "ArrayLock should have capacity at least '2', provided: '%d'"
                            .formatted(capacity));
        }
        slotsLength = capacity;
        flags = new AtomicBooleanArray(capacity * CACHE_LINE_OFFSET);
        flags.set(0);
    }

    @Override
    public void lock() {
        int curSlot = nextSlot();
        int idx = indexForSlot(curSlot);
        LOCAL_SLOT.set(curSlot);

        while (!flags.get(idx)) {
            Thread.onSpinWait();
        }
    }

    private int nextSlot() {
        while (true) {
            // We can't use slot.getAndIncrement() b/c int value may eventually overflow so we need to reset value
            int slotValue = slot.get();
            int nextValue = (slotValue + 1) % slotsLength;

            if( slot.compareAndSet(slotValue, nextValue) ){
                return slotValue;
            }
        }
    }

    @Override
    public void unlock() {
        int curSlot = LOCAL_SLOT.get();
        flags.clear(indexForSlot(curSlot));
        flags.set(indexForSlot((curSlot + 1) % slotsLength));
    }

    private int indexForSlot(int slot) {
        return slot * CACHE_LINE_OFFSET;
    }
}
