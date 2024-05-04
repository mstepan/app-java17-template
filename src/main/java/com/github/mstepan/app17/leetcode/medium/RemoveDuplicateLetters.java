package com.github.mstepan.app17.leetcode.medium;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * 316. Remove Duplicate Letters
 *
 * <p>https://leetcode.com/problems/remove-duplicate-letters/
 */
public class RemoveDuplicateLetters {

    public static void main(String[] args) {
        //        String s = "bcabc";
        //        String s = "cbacdcbc";

        String s = "thesqtitxyetpxloeevdeqifkz";

        String res = new RemoveDuplicateLetters().removeDuplicateLetters(s);

        System.out.printf("res: '%s'%n", res);

        System.out.println("RemoveDuplicateLetters done...");
    }

    /** time: O(N) space: O(26) ~ O(1) */
    public String removeDuplicateLetters(String str) {
        Objects.requireNonNull(str, "null 'str' detected");

        FreqTable freq = new FreqTable(str);

        Set<Character> used = new HashSet<>();
        Deque<Character> res = new ArrayDeque<>();

        for (int i = 0; i < str.length(); ++i) {
            char ch = str.charAt(i);
            freq.decrease(ch);

            if (used.contains(ch)) {
                continue;
            }

            while (!res.isEmpty()) {
                char last = res.peekLast();

                if (last > ch && freq.get(last) != 0) {
                    res.pollLast();
                    used.remove(last);
                } else {
                    break;
                }
            }

            res.addLast(ch);
            used.add(ch);
        }

        return toSting(res);
    }

    private String toSting(Deque<Character> res) {
        StringBuilder buf = new StringBuilder(res.size());

        for (char val : res) {
            buf.append(val);
        }

        return buf.toString();
    }

    static class FreqTable {
        private final Map<Character, Integer> data = new HashMap<>();

        public FreqTable(String str) {
            assert str != null;
            for (int i = 0; i < str.length(); ++i) {
                data.compute(str.charAt(i), (key, count) -> count == null ? 1 : count + 1);
            }
        }

        public void decrease(char ch) {
            assert data.containsKey(ch);
            data.compute(ch, (key, count) -> count - 1);
        }

        public int get(char ch) {
            assert data.containsKey(ch);
            return data.get(ch);
        }
    }
}
