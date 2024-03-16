package com.github.mstepan.app17.geeks;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.PriorityQueue;

/**
 * Game with String
 *
 * <p>https://www.geeksforgeeks.org/problems/game-with-string4100/1?utm_source=geeksforgeeks&utm_medium=newui_home&utm_campaign=potd
 */
public final class GameWithString {

    public static void main(String[] args) throws Exception {
        //        String str = "a".repeat(100_000);
        //        int k = 0;

        String str = "aabcbcbcabcc";
        int k = 3;

        long res = minValue(str, k);

        System.out.println(res);

        System.out.println("GameWithString done...");
    }

    /**
     * M = 27 ('z' - 'a' + 1)
     *
     * <p>time: O(K*lgM)
     *
     * <p>space: O(M)
     */
    static long minValue(String str, int k) {
        Objects.requireNonNull(str);
        if (k < 0) {
            throw new IllegalArgumentException("k < 0: k = %d".formatted(k));
        }
        if (k > str.length()) {
            throw new IllegalArgumentException("k > str.length");
        }

        if (k == str.length()) {
            return 0;
        }

        Map<Character, Integer> freqMap = new HashMap<>();

        for (int i = 0; i < str.length(); ++i) {
            char ch = str.charAt(i);
            freqMap.compute(ch, (key, count) -> count == null ? 1 : count + 1);
        }

        PriorityQueue<CharAndFreq> maxHeap = new PriorityQueue<>();

        for (Map.Entry<Character, Integer> entry : freqMap.entrySet()) {
            maxHeap.add(new CharAndFreq(entry.getKey(), entry.getValue()));
        }

        for (int it = 0; it < k; ++it) {
            CharAndFreq elem = maxHeap.poll();
            elem.freq -= 1;
            maxHeap.add(elem);
        }

        // we should use long here, otherwise numerical overflow possible
        // (10^5)^2 = 10^10 > Integer.MAX_VALUE
        long res = 0L;

        for (CharAndFreq elem : maxHeap) {
            res += ((long) elem.freq * elem.freq);
        }

        return res;
    }

    private static class CharAndFreq implements Comparable<CharAndFreq> {
        final char ch;
        int freq;

        CharAndFreq(char ch, int freq) {
            this.ch = ch;
            this.freq = freq;
        }

        @Override
        public int compareTo(CharAndFreq other) {
            return -Integer.compare(freq, other.freq);
        }

        @Override
        public String toString() {
            return "(%c => %d".formatted(ch, freq);
        }
    }
}
