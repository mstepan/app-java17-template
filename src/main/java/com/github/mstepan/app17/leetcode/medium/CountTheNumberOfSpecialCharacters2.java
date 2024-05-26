package com.github.mstepan.app17.leetcode.medium;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 3121. Count the Number of Special Characters II
 *
 * <p>https://leetcode.com/problems/count-the-number-of-special-characters-ii/
 */
public class CountTheNumberOfSpecialCharacters2 {

    public static void main(String[] args) {

        String str = "abaaACAcB";

        int count = new CountTheNumberOfSpecialCharacters2().numberOfSpecialChars(str);

        System.out.printf("count: %d%n", count);

        System.out.println("CountTheNumberOfSpecialCharacters2 main done...");
    }

    /** time: O(N) space: O(52) ~ O(1) */
    public int numberOfSpecialChars(String word) {
        Objects.requireNonNull(word);

        char[] arr = word.toCharArray();

        Map<Character, Integer> lowerRightmostIdx = new HashMap<>();
        Map<Character, Integer> upperLeftmostIdx = new HashMap<>();

        for (int i = 0; i < arr.length; ++i) {
            char ch = arr[i];

            if (!Character.isLetter(ch)) {
                throw new IllegalStateException(
                        "Expected letters only characters in string '%s' but found '%c'".formatted(word, ch));
            }

            if (Character.isLowerCase(ch)) {
                lowerRightmostIdx.put(ch, i);
            } else if (Character.isUpperCase(ch) && !upperLeftmostIdx.containsKey(ch)) {
                upperLeftmostIdx.put(ch, i);
            }
        }

        int cnt = 0;

        for (Map.Entry<Character, Integer> lowerEntry : lowerRightmostIdx.entrySet()) {
            char lowerCh = lowerEntry.getKey();
            int lowerIdx = lowerEntry.getValue();

            Integer upperdIdx = upperLeftmostIdx.get(Character.toUpperCase(lowerCh));

            if (upperdIdx != null && upperdIdx > lowerIdx) {
                ++cnt;
            }
        }

        return cnt;
    }
}
