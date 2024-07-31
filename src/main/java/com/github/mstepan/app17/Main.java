package com.github.mstepan.app17;

import java.util.concurrent.TimeUnit;

public class Main {

    public static void main(String[] args) throws Exception {

        System.out.println("Main done...");
    }

    private static void sleep() {
        try {
            TimeUnit.MILLISECONDS.sleep(10);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }

    public static void yieldCurThread() {
        //                Thread.onSpinWait();
        Thread.yield();
    }
}
