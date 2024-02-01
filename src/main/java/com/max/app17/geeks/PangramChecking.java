package com.max.app17.geeks;

import java.util.BitSet;
import java.util.Objects;

/**
 * Panagram Checking
 *
 * <p>https://www.geeksforgeeks.org/problems/pangram-checking-1587115620/1?utm_source=geeksforgeeks&utm_medium=newui_home&utm_campaign=potd
 *
 * <p>Given a string s check if it is "Panagram" or not.
 *
 * <p>A "Panagram" is a sentence containing every letter in the English Alphabet.
 */
public class PangramChecking {

    public static void main(String[] args) throws Exception {

        String str1 = "Bawds jog, flick quartz, vex nymph";
        System.out.println(checkPangram(str1));

        String str2 = "Bawds og, flick quartz, vex nymph";
        System.out.println(checkPangram(str2));

        System.out.println("PangramChecking done...");
    }

    private static int ALPHABET_SIZE = 26;

    /**
     * time: O(N)
     *
     * <p>space: O(1)
     */
    public static boolean checkPangram(String str) {
        Objects.requireNonNull(str, "null 'str' detected");

        BitSet alphabetChars = new BitSet(ALPHABET_SIZE);

        for (int i = 0; i < str.length(); ++i) {
            int digitIdx = Character.toLowerCase(str.charAt(i)) - 'a';

            if (digitIdx >= 0 && digitIdx < ALPHABET_SIZE) {
                alphabetChars.set(digitIdx);
            }
        }

        return alphabetChars.cardinality() == ALPHABET_SIZE;
    }
}
