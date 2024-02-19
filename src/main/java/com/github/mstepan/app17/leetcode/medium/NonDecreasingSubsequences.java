package com.max.app17.leetcode.medium;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * <a href="https://leetcode.com/problems/non-decreasing-subsequences/">491. Non-decreasing
 * Subsequences</a>
 */
public class NonDecreasingSubsequences {

    public static void main(String[] args) throws Exception {

        int[] arr = {4, 6, 7, 7};

        var allSubsequences = new NonDecreasingSubsequences().findSubsequences(arr);

        for (List<Integer> singleSub : allSubsequences) {
            System.out.println(singleSub);
        }

        System.out.println("NonDecreasingSubsequences done...");
    }

    private static final int MAX_ALLOWED_ARR_LENGTH_FOR_SUBSEQUENCES = 15;

    /** N = arr.length time: O(2^N * N) space: O(2^N) */
    public List<List<Integer>> findSubsequences(int[] arr) {
        if (arr == null) {
            throw new IllegalArgumentException("null 'arr' detected");
        }

        if (arr.length > MAX_ALLOWED_ARR_LENGTH_FOR_SUBSEQUENCES) {
            throw new IllegalArgumentException(
                    "Can't generate all subsequences, 'arr' is too big. Expected max value %d but found %d"
                            .formatted(MAX_ALLOWED_ARR_LENGTH_FOR_SUBSEQUENCES, arr.length));
        }
        if (arr.length == 0) {
            return List.of();
        }

        final int boundary = 1 << arr.length;

        // use Set here to eliminate any duplicated sub-sequences
        Set<List<Integer>> allSubsequences = new HashSet<>();

        for (int mask = 0; mask < boundary; ++mask) {
            List<Integer> subsequence = fromMask(arr, mask);

            if (subsequence.size() >= 2 && isNonDecreased(subsequence)) {
                allSubsequences.add(subsequence);
            }
        }

        return new ArrayList<>(allSubsequences);
    }

    private boolean isNonDecreased(List<Integer> subsequence) {

        Integer prev = Integer.MIN_VALUE;

        for (Integer cur : subsequence) {
            if (cur < prev) {
                return false;
            }

            prev = cur;
        }

        return true;
    }

    private List<Integer> fromMask(int[] arr, int mask) {
        assert mask >= 0;
        assert arr != null;
        assert arr.length <= MAX_ALLOWED_ARR_LENGTH_FOR_SUBSEQUENCES;

        List<Integer> subsequence = new ArrayList<>();

        for (int i = 0; i < arr.length; ++i) {
            if ((mask & (1 << i)) != 0) {
                subsequence.add(arr[i]);
            }
        }

        return subsequence;
    }
}
