package com.github.mstepan.app17.numbers;

/** Not thread safe int counter. */
public class IntCounter {

    private int value;

    public void inc() {
        value += 1;
    }

    public int value() {
        return value;
    }
}
