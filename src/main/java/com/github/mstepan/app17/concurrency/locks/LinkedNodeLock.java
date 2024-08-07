package com.github.mstepan.app17.concurrency.locks;

import java.util.concurrent.atomic.AtomicReference;

public final class LinkedNodeLock implements Lock {

    private final AtomicReference<Node> tail = new AtomicReference<>(new Node("DUMMY"));

    private final ThreadLocal<Node> curNodeLocal;


    public LinkedNodeLock() {
        curNodeLocal = new ThreadLocal<>();
    }

    @Override
    public void lock() {
        Node cur = new Node(Thread.currentThread().getName());
        cur.locked = true;

        curNodeLocal.set(cur);

        Node predecessor = tail.getAndSet(cur);

        while (predecessor.locked) {
            Thread.onSpinWait();
        }

//        System.out.printf("locked-%d%n", Thread.currentThread().getId());
    }

    @Override
    public void unlock() {
        Node cur = curNodeLocal.get();
        cur.locked = false;
        curNodeLocal.remove();
//        System.out.printf("unlocked-%d%n", Thread.currentThread().getId());
    }

    private static final class Node {
        final String name;
        volatile boolean locked;

        public Node(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }
}
