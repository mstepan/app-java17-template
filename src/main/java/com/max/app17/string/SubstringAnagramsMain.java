package com.max.app17.string;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class SubstringAnagramsMain {

    public static void main(String[] args) throws Exception {

        List<Integer> res = findAllAnagramsPositions("cbtrtabc", "567");

        //  findAllAnagramsPositions("cbaebabacd", "abc");

        System.out.println(res);

        System.out.println("Main done...");
    }

    /** time: O(N*26) space: O(26) */
    static List<Integer> findAllAnagramsPositions(String text, String pattern) {
        Objects.requireNonNull(text);
        Objects.requireNonNull(pattern);
        checkIsLowerCaseCharacters(text);
        checkIsLowerCaseCharacters(pattern);

        if (pattern.length() > text.length() || pattern.equals("")) {
            return Collections.emptyList();
        }

        List<Integer> res = new ArrayList<>();

        final int anagramLength = pattern.length();

        final int[] tSign = createSignature(text, 0, anagramLength - 1);
        final int[] pSign = createSignature(pattern, 0, anagramLength - 1);

        if (Arrays.equals(tSign, pSign)) {
            res.add(0);
        }

        for (int right = anagramLength; right < text.length(); ++right) {
            char charToDel = text.charAt(right - anagramLength);
            char newChar = text.charAt(right);
            recalculateSignatureInPlace(tSign, charToDel, newChar);

            if (Arrays.equals(tSign, pSign)) {
                res.add(right - anagramLength + 1);
            }
        }

        return res;
    }

    private static void checkIsLowerCaseCharacters(String str) {
        for (int i = 0; i < str.length(); ++i) {
            char ch = str.charAt(i);
            if (ch < FIRST_CH || ch > LAST_CH) {
                throw new IllegalArgumentException(
                        ("Incorrect character detected at position: %d for string: '%s', "
                                        + "expected lower case letter, but found: '%c'")
                                .formatted(i, str, ch));
            }
        }
    }

    private static final char FIRST_CH = 'a';
    private static final char LAST_CH = 'z';

    private static final int ALPHABET_SIZE = LAST_CH - FIRST_CH + 1;

    private static int[] createSignature(String str, int from, int to) {
        assert str != null;
        assert from >= 0 && from < str.length();
        assert to >= 0 && to < str.length();
        assert from <= to;

        int[] signature = new int[ALPHABET_SIZE];

        for (int i = from; i <= to; ++i) {
            char ch = str.charAt(i);
            assert ch >= FIRST_CH && ch <= LAST_CH;
            signature[ch - FIRST_CH] += 1;
        }

        return signature;
    }

    private static void recalculateSignatureInPlace(int[] signature, char charToDel, char newChar) {
        assert signature != null;
        assert charToDel >= FIRST_CH && charToDel <= LAST_CH;
        assert newChar >= FIRST_CH && newChar <= LAST_CH;

        signature[charToDel - FIRST_CH] -= 1;
        signature[newChar - FIRST_CH] += 1;
    }
}
