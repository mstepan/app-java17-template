package com.github.mstepan.app17.leetcode.medium;

import java.util.Arrays;
import java.util.Objects;

/**
 * 57. Insert Interval
 *
 * <p>https://leetcode.com/problems/insert-interval/description/
 */
public class InsertInterval {

    public static void main(String[] args) {

        //        int[][] intervals = {{1, 3}, {6, 9}};
        //        int[] newInterval = {2, 5};

        int[][] intervals = {{1, 2}, {3, 5}, {6, 7}, {8, 10}, {12, 16}};
        int[] newInterval = {4, 8};

        int[][] res = new InsertInterval().insert(intervals, newInterval);

        for (int[] cur : res) {
            System.out.println(Arrays.toString(cur));
        }

        System.out.println("InsertInterval done...");
    }

    /**
     * time: O(N)
     *
     * <p>space: O(N)
     */
    public int[][] insert(int[][] intervals, int[] newInterval) {
        Objects.requireNonNull(intervals);
        Objects.requireNonNull(newInterval);

        int overlapsCnt = countOverlaps(newInterval, intervals);
        int resLength = intervals.length - overlapsCnt + 1;

        int[][] res = new int[resLength][2];

        int idx = 0;
        int resIdx = 0;

        // copy all intervals before 'newInterval'
        while (idx < intervals.length && isBefore(intervals[idx], newInterval)) {
            res[resIdx][0] = intervals[idx][0];
            res[resIdx][1] = intervals[idx][1];
            ++idx;
            ++resIdx;
        }

        // merge all overlapping if any intervals with 'newInterval' intervals and add as one
        int[] merged = newInterval;
        while (idx < intervals.length && isOverlap(merged, intervals[idx])) {
            merged = merge(merged, intervals[idx]);
            ++idx;
        }

        res[resIdx] = merged;
        ++resIdx;

        // add all intervals after the 'newInterval'
        while (idx < intervals.length) {
            res[resIdx][0] = intervals[idx][0];
            res[resIdx][1] = intervals[idx][1];
            ++idx;
            ++resIdx;
        }

        return res;
    }

    private int countOverlaps(int[] cur, int[][] intervals) {
        int cnt = 0;

        for (int[] singleInterval : intervals) {
            if (isOverlap(cur, singleInterval)) {
                ++cnt;
            }
        }

        return cnt;
    }

    private boolean isBefore(int[] cur, int[] other) {
        return cur[1] < other[0];
    }

    private boolean isOverlap(int[] cur, int[] other) {
        int[] left = cur;
        int[] right = other;

        if (other[0] < cur[0]) {
            left = other;
            right = cur;
        }

        return left[1] >= right[0];
    }

    private int[] merge(int[] cur, int[] other) {
        return new int[] {
            Math.min(cur[0], other[0]), Math.max(cur[1], other[1]),
        };
    }
}
