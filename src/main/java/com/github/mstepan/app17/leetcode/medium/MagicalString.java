package com.max.app17.leetcode.medium;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * 481. Magical String
 *
 * <p>https://leetcode.com/problems/magical-string/
 */
public class MagicalString {

    public static void main(String[] args) throws Exception {

        int onesCount = new MagicalString().magicalString(1000);

        System.out.printf("ones count: %d%n", onesCount);

        System.out.println("MagicalString done...");
    }

    /** time: O(N) space: O(N/2) */
    public int magicalString(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("Negative or zero n detected: %d".formatted(n));
        }

        if (n <= 3) {
            return 1;
        }

        int onesCount = 1;
        int index = 3;

        Deque<Integer> originalQueue = new ArrayDeque<>();

        Deque<Integer> freqQueue = new ArrayDeque<>();
        freqQueue.add(2);

        int last = 2;

        while (true) {

            while (!freqQueue.isEmpty()) {
                int freqVal = freqQueue.poll();

                last = flip(last);
                for (int i = 0; i < freqVal; ++i) {
                    originalQueue.add(last);
                }
            }

            System.out.printf("originalQueue.size: %d%n", originalQueue.size());

            while (!originalQueue.isEmpty()) {
                int value = originalQueue.poll();

                if (value == 1) {
                    ++onesCount;
                }

                ++index;

                if (index == n) {
                    return onesCount;
                }

                freqQueue.add(value);
            }
        }
    }

    private int flip(int last) {
        return (last == 1) ? 2 : 1;
    }
}
