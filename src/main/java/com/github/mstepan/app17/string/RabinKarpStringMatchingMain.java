package com.github.mstepan.app17.string;

import java.util.Arrays;

public class RabinKarpStringMatchingMain {

    public static void main(String[] args) throws Exception {

        String pattern = "hello";

        String text = "help hello beautiful hello beast helo world";

        printAllMatches(text, pattern);

        System.out.println("Rabin-Karp string matching done...");
    }

    static void printAllMatches(String text, String pattern) {

        // generate 3 rolling hashes to minimize collisions
        RollingHash[] rollingHashes = new RollingHash[3];
        Arrays.fill(rollingHashes, new RollingHash(pattern.length()));

        for (int index = indexOf(text, pattern, 0, rollingHashes);
                index != -1;
                index = indexOf(text, pattern, index + 1, rollingHashes)) {
            System.out.printf("Found match at index: %d\n", index);
        }
    }

    /** Rabin-Karp string matching algorithm using rollign hash. */
    public static int indexOf(String text, String pattern, int from, RollingHash... rollingHashes) {

        int left = from;
        int right = from + pattern.length() - 1;

        if (right >= text.length()) {
            return -1;
        }

        final long[] patternHash = initialHashes(rollingHashes, pattern, 0, pattern.length() - 1);
        final long[] textHash = initialHashes(rollingHashes, text, left, right);

        while (true) {
            if (isMatched(text, textHash, pattern, patternHash, left)) {
                return left;
            }

            ++left;
            ++right;

            if (right >= text.length()) {
                break;
            }

            nextHashesInPlace(text, textHash, left, right, rollingHashes);
        }

        return -1;
    }

    private static void nextHashesInPlace(
            String str, long[] curHashes, int left, int right, RollingHash... rollingHashes) {
        for (int i = 0; i < curHashes.length; ++i) {
            curHashes[i] = rollingHashes[i].nextHash(str, curHashes[i], left, right);
        }
    }

    private static long[] initialHashes(RollingHash[] rollingHashes, String str, int from, int to) {
        assert rollingHashes != null;

        long[] values = new long[rollingHashes.length];

        for (int i = 0; i < values.length; ++i) {
            values[i] = rollingHashes[i].initialHash(str, from, to);
        }

        return values;
    }

    private static boolean isMatched(
            String text, long[] textHash, String pattern, long[] patternHash, int offset) {
        if (!Arrays.equals(textHash, patternHash)) {
            return false;
        }

        for (int i = 0; i < pattern.length(); ++i) {
            if (pattern.charAt(i) != text.charAt(offset + i)) {
                return false;
            }
        }

        return true;
    }
}
