package com.github.mstepan.app17;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public class Main {

    public static void main(String[] args) throws Exception {

        System.out.println("Counter started");

        AtomicLong val = new AtomicLong(0L);
        while (!Thread.currentThread().isInterrupted()) {
            val.incrementAndGet();

            TimeUnit.SECONDS.sleep(60L);

            System.out.printf("counter: %d%n", val.get());
        }

        System.out.printf("Final counter: %d%n", val.get());

        System.out.println("Main done...");
    }
}
