package com.github.mstepan.app17.leetcode.medium;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Objects;
import java.util.PriorityQueue;
import java.util.Set;

/**
 * 3170. Lexicographically Minimum String After Removing Stars
 *
 * <p>https://leetcode.com/problems/lexicographically-minimum-string-after-removing-stars/description/
 */
public class LexicographicallyMinimumStringAfterRemovingStars {

    public static void main(String[] args) {

        String s = "aaba*";

        String clearedStr = new LexicographicallyMinimumStringAfterRemovingStars().clearStars(s);

        System.out.printf("Cleared str: %s%n", clearedStr);

        System.out.println("LexicographicallyMinimumStringAfterRemovingStars done...");
    }

    /**
     * time: O(N*lgN)
     *
     * <p>space: O(N)
     */
    public String clearStars(String s) {
        Objects.requireNonNull(s);

        Set<Integer> deletedPos = new HashSet<>();

        PriorityQueue<CharAndPos> minHeap = new PriorityQueue<>(CharAndPos.CHAR_ASC_POS_DESC);

        for (int i = 0; i < s.length(); ++i) {
            char ch = s.charAt(i);

            if (ch == '*') {
                assert !minHeap.isEmpty() : "minHeap is empty, can't delete any chars";
                CharAndPos charToDel = minHeap.poll();

                deletedPos.add(charToDel.pos);
                deletedPos.add(i);
            } else {
                minHeap.add(new CharAndPos(ch, i));
            }
        }

        return createStrSkippingDeletedChars(s, deletedPos);
    }

    private String createStrSkippingDeletedChars(String base, Set<Integer> deletedPos) {
        assert base != null;
        assert deletedPos != null;

        if (deletedPos.isEmpty()) {
            return base;
        }

        char[] res = new char[base.length() - deletedPos.size()];

        for (int i = 0, j = 0; i < base.length() && j < res.length; ++i) {
            if (!deletedPos.contains(i)) {
                res[j] = base.charAt(i);
                ++j;
            }
        }

        return new String(res);
    }

    static class CharAndPos {

        static final Comparator<CharAndPos> CHAR_ASC_POS_DESC =
                Comparator.comparingInt(CharAndPos::getCh)
                        .thenComparing(Comparator.comparingInt(CharAndPos::getPos).reversed());

        final char ch;
        final int pos;

        public CharAndPos(char ch, int pos) {
            this.ch = ch;
            this.pos = pos;
        }

        public char getCh() {
            return ch;
        }

        public int getPos() {
            return pos;
        }
    }
}
