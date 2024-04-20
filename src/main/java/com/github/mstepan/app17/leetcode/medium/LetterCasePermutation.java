package com.github.mstepan.app17.leetcode.medium;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 784. Letter Case Permutation
 *
 * <p>https://leetcode.com/problems/letter-case-permutation/description/
 */
public class LetterCasePermutation {

    public static void main(String[] args) {

        String str = "a1b2";
        List<String> allPermutations = new LetterCasePermutation().letterCasePermutation(str);

        for (String singlePerm : allPermutations) {
            System.out.println(singlePerm);
        }

        System.out.println("LetterCasePermutation done...");
    }

    /**
     * time: O(N * 2^lettersCount)
     *
     * <p>space: O(N * 2^lettersCount)
     */
    public List<String> letterCasePermutation(String str) {
        Objects.requireNonNull(str);
        char[] arr = str.toCharArray();

        int lettersCount = countLetters(arr);

        List<String> results = new ArrayList<>();

        for (int mask = 0; mask < (1 << lettersCount); ++mask) {
            results.add(generateFromMask(arr, mask));
        }

        return results;
    }

    private String generateFromMask(char[] arr, int mask) {
        StringBuilder res = new StringBuilder(arr.length);

        for (char val : arr) {
            if (Character.isLetter(val)) {
                char modifiedVal =
                        (mask & 1) == 0 ? Character.toLowerCase(val) : Character.toUpperCase(val);
                res.append(modifiedVal);
                mask >>= 1;
            } else {
                res.append(val);
            }
        }

        return res.toString();
    }

    private int countLetters(char[] arr) {
        assert arr != null;

        int cnt = 0;

        for (char val : arr) {
            if (Character.isLetter(val)) {
                ++cnt;
            }
        }

        return cnt;
    }
}
