package com.github.mstepan.app17.concurrency.locks;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.LockSupport;

public final class SimpleLock implements Lock {

    private final Queue<Thread> waitQueue = new ConcurrentLinkedQueue<>();

    @Override
    public void lock() {
        final Thread curThread = Thread.currentThread();
        waitQueue.add(curThread);

        while (waitQueue.peek() != curThread) {
            LockSupport.park();
        }
    }

    @Override
    public void unlock() {
        waitQueue.remove();
        LockSupport.unpark(waitQueue.peek());
    }
}
