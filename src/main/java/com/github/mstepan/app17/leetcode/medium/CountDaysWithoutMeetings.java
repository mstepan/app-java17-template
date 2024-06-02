package com.github.mstepan.app17.leetcode.medium;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Objects;

/**
 * 3169. Count Days Without Meetings
 *
 * <p>https://leetcode.com/problems/count-days-without-meetings/description/
 */
public class CountDaysWithoutMeetings {

    public static void main(String[] args) {

        //        int days = 10;
        //        int[][] meetings = {{5, 7}, {1, 3}, {9, 10}};
        //
        //        // expected: 2
        //        int daysWithoutMeetings = new CountDaysWithoutMeetings().countDays(days,
        // meetings);

        //        int days = 5;
        //        int[][] meetings = {{2, 4}, {1, 3}};
        //        // expected: 1
        //        int daysWithoutMeetings = new CountDaysWithoutMeetings().countDays(days,
        // meetings);

        int days = 57;
        int[][] meetings = {
            {3, 49}, {23, 44}, {21, 56}, {26, 55}, {23, 52}, {2, 9}, {1, 48}, {3, 31}
        };
        // expected: 1
        int daysWithoutMeetings = new CountDaysWithoutMeetings().countDays(days, meetings);

        System.out.printf("daysWithoutMeetings: %d%n", daysWithoutMeetings);

        System.out.println("CountDaysWithoutMeetings done...");
    }

    /**
     * time: O(N*lgN)
     *
     * <p>space: O(1)
     */
    public int countDays(int days, int[][] meetings) {
        if (days < 1) {
            throw new IllegalArgumentException("Incorrect 'days' provided: " + days);
        }
        Objects.requireNonNull(meetings);

        Arrays.sort(meetings, Comparator.comparingInt(interval1 -> interval1[0]));

        int freeDaysCnt = 0;
        int last = 0;

        for (int[] singleInterval : meetings) {

            int start = singleInterval[0];
            int end = singleInterval[1];

            if (last < start) {
                freeDaysCnt += (start - last - 1);
            }

            last = Math.max(last, end);
        }

        freeDaysCnt += (days - last);

        return freeDaysCnt;
    }
}
