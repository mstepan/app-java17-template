package com.max.app17.leetcode.hard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

/** 354. Russian Doll Envelopes https://leetcode.com/problems/russian-doll-envelopes/ */
public class RussianDollEnvelopes {
    public static void main(String[] args) throws Exception {

        int res =
                maxEnvelopes(
                        new int[][] {
                            {5, 4},
                            {6, 4},
                            {6, 7},
                            {2, 3}
                        });

        System.out.printf("largest possible nesting count = %d%n", res);

        System.out.println("Russian Doll Envelopes done...");
    }

    /**
     * time: O(N*lgN + N*lgK) space: O(N)
     *
     * <p>N = arr.length K = longest increased subsequence length
     */
    public static int maxEnvelopes(int[][] arr) {

        Envelope[] envelopes = toEnvelopes(arr);

        // time: O(N*lgN), space: O(N)
        Arrays.sort(envelopes, Envelope.WIDTH_ASC_HEIGHT_DESC);

        int longestSubseqCnt = 0;
        List<Envelope> sortedByHeight = new ArrayList<>();

        for (Envelope cur : envelopes) {
            int index = Collections.binarySearch(sortedByHeight, cur, Envelope.HEIGHT_ASC);

            if (index < 0) {

                int insertIdx = Math.abs(index) - 1;

                if (insertIdx >= sortedByHeight.size()) {
                    sortedByHeight.add(insertIdx, cur);
                } else {
                    sortedByHeight.set(insertIdx, cur);
                }
                longestSubseqCnt = Math.max(longestSubseqCnt, sortedByHeight.size());
            }
        }

        return longestSubseqCnt;
    }

    record Envelope(int width, int height) {
        private static final Comparator<Envelope> WIDTH_ASC_HEIGHT_DESC =
                Comparator.comparingInt(Envelope::width)
                        .thenComparing(Comparator.comparingInt(Envelope::height).reversed());

        private static final Comparator<Envelope> HEIGHT_ASC =
                Comparator.comparingInt(Envelope::height);

        public boolean canHold(Envelope other) {
            Objects.requireNonNull(other);
            return width > other.width && height > other.height;
        }
    }

    private static Envelope[] toEnvelopes(int[][] arr) {
        Envelope[] res = new Envelope[arr.length];

        for (int i = 0; i < arr.length; ++i) {
            res[i] = new Envelope(arr[i][0], arr[i][1]);
        }

        return res;
    }
}
