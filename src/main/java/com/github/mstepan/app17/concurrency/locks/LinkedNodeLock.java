package com.github.mstepan.app17.concurrency.locks;

import java.util.concurrent.atomic.AtomicReference;

public final class LinkedNodeLock implements Lock {

    private final AtomicReference<Node> tail = new AtomicReference<>(new Node(-1L));

    private final ThreadLocal<Node> curNodeLocal = new ThreadLocal<>();

    @Override
    public void lock() {
        Node cur = new Node(Thread.currentThread().getId());
        cur.locked = true;

        curNodeLocal.set(cur);

        Node predecessor = tail.getAndSet(cur);

        while (predecessor.locked) {
            Thread.onSpinWait();
        }

//                System.out.printf("locked-%d%n", Thread.currentThread().getId());
    }

    @Override
    public void unlock() {
        Node cur = curNodeLocal.get();
        cur.locked = false;
        curNodeLocal.remove();
//                System.out.printf("unlocked-%d%n", Thread.currentThread().getId());
    }

    private static final class Node {
        final long id;
        volatile boolean locked;

        public Node(long id) {
            this.id = id;
        }

        @Override
        public String toString() {
            return String.valueOf(id);
        }
    }
}
