package com.max.app17.leetcode.medium;

import com.max.app17.Main;

import java.util.Objects;

/**
 * 299. Bulls and Cows
 *
 * <p>https://leetcode.com/problems/bulls-and-cows/
 */
public class BullsAndCows {

    public static void main(String[] args) throws Exception {

        String hint = new BullsAndCows().getHint("1123", "0111");

        System.out.println(hint);

        System.out.println("BullsAndCows done...");
    }

    /** time: O(N) space: O(N), b/c we convert String to char[] array. */
    public String getHint(String secretStr, String guessStr) {
        Objects.requireNonNull(secretStr);
        Objects.requireNonNull(guessStr);

        assert secretStr.length() == guessStr.length() : "secretStr.length() != guessStr.length()";

        char[] secret = secretStr.toCharArray();
        char[] guess = guessStr.toCharArray();

        int[] secretFreq = new int[10]; // 0...9
        int[] guessFreq = new int[10]; // 0...9

        int bullsCount = 0;

        for (int i = 0; i < secret.length; ++i) {
            char secretCh = secret[i];
            char guessCh = guess[i];

            if (secretCh == guessCh) {
                ++bullsCount;
            } else {

                assert secretCh - '0' >= 0 && secretCh - '0' <= 9
                        : "'secret' character is not a digit: " + secretCh;
                assert guessCh - '0' >= 0 && guessCh - '0' <= 9
                        : "'guess' character is not a digit: " + guessCh;
                ;

                secretFreq[secretCh - '0'] += 1;
                guessFreq[guessCh - '0'] += 1;
            }
        }

        int cowsCount = 0;

        for (int i = 0; i < secretFreq.length; ++i) {
            cowsCount += Math.min(secretFreq[i], guessFreq[i]);
        }

        return "%dA%dB".formatted(bullsCount, cowsCount);
    }
}
