package com.github.mstepan.app17.concurrency;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.LockSupport;

public final class SimpleLock {

    private final Queue<Thread> waitQueue = new ConcurrentLinkedQueue<>();

    public void lock() {
        final Thread curThread = Thread.currentThread();
        waitQueue.add(curThread);

        while (waitQueue.peek() != curThread) {
            LockSupport.park();
        }
    }

    public void unlock() {
        waitQueue.remove();
        LockSupport.unpark(waitQueue.peek());
    }
}
