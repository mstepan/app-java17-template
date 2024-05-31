package com.github.mstepan.app17.leetcode.medium;

import java.util.Objects;

/**
 * 3163. String Compression III
 *
 * <p>https://leetcode.com/problems/string-compression-iii/description/
 */
public class StringCompression3 {

    public static void main(String[] args) {

        String str = "aaaaaaaaaaaaaabb";

        // expected: "9a5a2b"
        // actual:   "9a5a2b"
        String compressed = new StringCompression3().compressedString(str);

        System.out.println(compressed);

        System.out.println("StringCompression3 done...");
    }

    /**
     * time: O(N)
     *
     * <p>space: O(N)
     */
    public String compressedString(String word) {
        Objects.requireNonNull(word);

        if (word.isEmpty()) {
            return word;
        }

        StringBuilder compressed = new StringBuilder(2 * word.length());

        char last = word.charAt(0);
        assert last >= 'a' && last <= 'z';

        int cnt = 1;

        for (int i = 1; i < word.length(); ++i) {
            char cur = word.charAt(i);

            assert cur >= 'a' && cur <= 'z';

            if (last == cur) {
                ++cnt;
                if (cnt > 9) {
                    compressed.append(9);
                    compressed.append(last);
                    cnt -= 9;
                }
            } else {
                compressed.append(cnt);
                compressed.append(last);

                last = cur;
                cnt = 1;
            }
        }

        compressed.append(cnt);
        compressed.append(last);

        return compressed.toString();
    }
}
