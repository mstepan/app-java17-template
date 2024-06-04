package com.github.mstepan.app17.leetcode.medium;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * 3152. Special Array II
 *
 * <p>https://leetcode.com/problems/special-array-ii/description/
 */
public class SpecialArray2 {

    public static void main(String[] args) {
        //        int[] nums = {4, 3, 1, 6};
        //        int[][] queries = {{0, 2}, {2, 3}};

        int[] nums = {2, 2};
        int[][] queries = {{0, 0}};

        boolean[] answers = new SpecialArray2().isArraySpecial(nums, queries);
        System.out.printf("answers: %s%n", Arrays.toString(answers));

        System.out.println("SpecialArray2 done...");
    }

    /**
     * N = nums.length
     *
     * <p>M = queries.length
     *
     * <p>time: O(M*lgN)
     *
     * <p>space: O(N)
     */
    public boolean[] isArraySpecial(int[] nums, int[][] queries) {
        Objects.requireNonNull(nums);
        Objects.requireNonNull(queries);

        if (nums.length == 1) {
            boolean[] answers = new boolean[queries.length];
            Arrays.fill(answers, true);
            return answers;
        }

        boolean[] answers = new boolean[queries.length];
        Interval[] goodIntervals = calculateGoodIntervals(nums);

        for (int i = 0; i < queries.length; ++i) {
            int[] singleQuery = queries[i];

            assert singleQuery != null && singleQuery.length == 2;
            Interval queryInterval = new Interval(singleQuery[0], singleQuery[1]);

            if (queryInterval.length() == 1) {
                answers[i] = true;
                continue;
            }

            Optional<Interval> maybeOverlapping =
                    binarySearchOverlapping(queryInterval, goodIntervals);

            if (maybeOverlapping.isEmpty()) {
                answers[i] = false;
            } else {
                answers[i] = maybeOverlapping.get().contains(queryInterval);
            }
        }

        return answers;
    }

    private Optional<Interval> binarySearchOverlapping(
            Interval searchInterval, Interval[] allIntervals) {
        assert searchInterval != null;
        assert allIntervals != null;

        int from = 0;
        int to = allIntervals.length - 1;

        while (from <= to) {
            int mid = from + (to - from) / 2;

            Interval middleInterval = allIntervals[mid];

            if (middleInterval.isOverlapping(searchInterval)) {
                return Optional.of(middleInterval);
            }

            if (searchInterval.from < middleInterval.from) {
                to = mid - 1;
            } else {
                from = mid + 1;
            }
        }

        return Optional.empty();
    }

    /**
     * time: O(N)
     *
     * <p>space: O(N)
     */
    private Interval[] calculateGoodIntervals(int[] arr) {

        int from = 0;
        int to = 1;

        List<Interval> goodIntervals = new ArrayList<>();

        for (; to < arr.length; ++to) {
            boolean prevOdd = isOdd(arr[to - 1]);
            boolean curOdd = isOdd(arr[to]);

            if (prevOdd == curOdd) {
                int intervalLength = (to - 1) - from + 1;

                if (intervalLength > 1) {
                    goodIntervals.add(new Interval(from, to - 1));
                }

                from = to;
            }
        }

        int intervalLength = to - from;
        if (intervalLength > 1) {
            goodIntervals.add(new Interval(from, to - 1));
        }

        return goodIntervals.toArray(new Interval[] {});
    }

    private static boolean isOdd(int value) {
        return (value & 1) != 0;
    }

    static class Interval {
        final int from;
        final int to;

        Interval(int from, int to) {
            assert from <= to;
            this.from = from;
            this.to = to;
        }

        @Override
        public String toString() {
            return "[" + from + ", " + to + "]";
        }

        boolean contains(Interval other) {
            assert other != null;
            return from <= other.from && to >= other.to;
        }

        boolean isOverlapping(Interval other) {
            assert other != null;

            Interval left = this;
            Interval right = other;

            if (right.from < left.from) {
                left = other;
                right = this;
            }

            return left.to >= right.from;
        }

        public int length() {
            return to - from + 1;
        }
    }
}
