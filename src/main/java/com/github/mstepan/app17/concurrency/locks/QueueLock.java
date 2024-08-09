package com.github.mstepan.app17.concurrency.locks;

import java.util.concurrent.atomic.AtomicReference;

public final class QueueLock implements Lock {

    private final AtomicReference<Node> tail = new AtomicReference<>(null);
    private final ThreadLocal<Node> myNode = ThreadLocal.withInitial(Node::new);

    @Override
    public void lock() {
        Node cur = myNode.get();
        Node pred = tail.getAndSet(cur);

        if (pred != null) {
            cur.locked = true;
            pred.next = cur;

            while (cur.locked) {
                Thread.onSpinWait();
            }
        }
    }

    @Override
    public void unlock() {
        Node cur = myNode.get();

        if (cur.next == null) {
            if (tail.compareAndSet(cur, null)) {
                return;
            }

            // another thread is changing tail, wait till next is null
            while (cur.next == null) {
                Thread.onSpinWait();
            }
        }

        // signal cur.next that it can enter critical section
        cur.next.locked = false;
        cur.next = null;
    }

    private static final class Node {
        volatile boolean locked;

        volatile Node next;
    }
}
