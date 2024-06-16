package com.github.mstepan.app17.leetcode.medium;

import java.util.Objects;

public class ConstructKPalindromeStrings {

    /**
     * time: O(N)
     *
     * <p>space: O(26) ~ O(1)
     */
    public boolean canConstruct(String s, int k) {
        Objects.requireNonNull(s);
        if (k < 0) {
            throw new IllegalArgumentException("Negative k detected: " + k);
        }

        if (k > s.length()) {
            return false;
        }

        if (k == s.length()) {
            return true;
        }

        int[] freq = new int['z' - 'a' + 1];

        for (int i = 0; i < s.length(); ++i) {
            char ch = s.charAt(i);
            assert ch >= 'a' && ch <= 'z';
            freq[ch - 'a'] += 1;
        }

        int oddCharsCount = 0;

        for (int i = 0; i < freq.length; ++i) {
            if ((freq[i] & 1) != 0) {
                ++oddCharsCount;
            }
        }

        return k >= oddCharsCount;
    }
}
