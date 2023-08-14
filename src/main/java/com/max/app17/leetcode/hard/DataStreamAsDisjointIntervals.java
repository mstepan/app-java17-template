package com.max.app17.leetcode.hard;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

/*
352. Data Stream as Disjoint Intervals
https://leetcode.com/problems/data-stream-as-disjoint-intervals/
 */
public class DataStreamAsDisjointIntervals {
    public static void main(String[] args) throws Exception {

        DataStreamAsDisjointIntervals.SummaryRanges ranges = new DataStreamAsDisjointIntervals().new SummaryRanges();

        ranges.addNum(1);
        printIntervals(ranges.getIntervals());
        System.out.println();

        ranges.addNum(3);
        printIntervals(ranges.getIntervals());
        System.out.println();

        ranges.addNum(7);
        printIntervals(ranges.getIntervals());
        System.out.println();

        ranges.addNum(2);
        printIntervals(ranges.getIntervals());
        System.out.println();

        System.out.println("DataStreamAsDisjointIntervals done...");
    }

    private static void printIntervals(int[][] intervals) {
        for (int[] singleInterval : intervals) {
            System.out.printf("[%d, %d]%n", singleInterval[0], singleInterval[1]);
        }
    }

    class SummaryRanges {

        // max value = 10^4, so we should use 10_001 as size
        private final BitSet bits = new BitSet(10_001);

        public SummaryRanges() {}

        public void addNum(int value) {
            bits.set(value);
        }

        public int[][] getIntervals() {
            List<int[]> mergedIntervals = new ArrayList<>();

            for (int i = bits.nextSetBit(0); i >= 0; i = bits.nextSetBit(i + 1)) {

                int lo = i;
                int hi = i;

                ++i;

                while ( i < 10_001 && bits.get(i)) {
                    hi = i;
                    ++i;
                }

                mergedIntervals.add(new int[] {lo, hi});
            }

            int[][] res = new int[mergedIntervals.size()][2];

            for (int i = 0; i < mergedIntervals.size(); ++i) {
                res[i] = mergedIntervals.get(i);
            }

            return res;
        }
    }
}
