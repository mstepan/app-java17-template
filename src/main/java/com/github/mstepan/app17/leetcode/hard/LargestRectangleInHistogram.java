package com.max.app17.leetcode.hard;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Objects;

/*
84. Largest Rectangle in Histogram

https://leetcode.com/problems/largest-rectangle-in-histogram/
 */
public class LargestRectangleInHistogram {

    public static void main(String[] args) throws Exception {

        int[] heights = {2, 1, 5, 6, 2, 3};

        // int[] heights = {2, 4};

        int largestArea = new LargestRectangleInHistogram().largestRectangleArea(heights);

        System.out.printf("largestArea: %d%n", largestArea);

        System.out.println("LargestRectangleInHistogram done...");
    }

    /*
    time: O(2*N)
    space: O(2*N)
     */
    public int largestRectangleArea(int[] heights) {
        Objects.requireNonNull(heights);

        int[] prefixArea = prefixArea(heights);
        int[] suffixArea = suffixArea(heights);

        assert heights.length == prefixArea.length;
        assert heights.length == suffixArea.length;

        int maxArea = Integer.MIN_VALUE;

        for (int i = 0; i < prefixArea.length; ++i) {
            // calculate area for current value
            // the area is equals to sum of prefixArea and suffixArea minus current element
            // value, b/c it's already included 2 times (1 in prefix value another in suffix)
            int curArea = prefixArea[i] + suffixArea[i] - heights[i];
            maxArea = Math.max(maxArea, curArea);
        }

        return maxArea;
    }

    private static int[] prefixArea(int[] heights) {
        int[] prefixArea = new int[heights.length];
        prefixArea[0] = heights[0];

        Deque<ValueAndIndex> sortedValues = new ArrayDeque<>();
        sortedValues.addLast(new ValueAndIndex(heights[0], 0));

        for (int i = 1; i < heights.length; ++i) {
            int curVal = heights[i];

            int lastIdx = i;

            while (!sortedValues.isEmpty()) {
                ValueAndIndex last = sortedValues.peekLast();

                if (last.value >= curVal) {
                    lastIdx = last.index;
                    sortedValues.pollLast();
                } else {
                    break;
                }
            }

            prefixArea[i] = curVal * (i - lastIdx + 1);
            sortedValues.add(new ValueAndIndex(curVal, lastIdx));
        }

        return prefixArea;
    }

    private static int[] suffixArea(int[] heights) {
        int[] suffixArea = new int[heights.length];

        suffixArea[suffixArea.length - 1] = heights[suffixArea.length - 1];

        Deque<ValueAndIndex> sortedValues = new ArrayDeque<>();
        sortedValues.add(new ValueAndIndex(heights[heights.length - 1], heights.length - 1));

        for (int i = heights.length - 2; i >= 0; --i) {
            int curVal = heights[i];

            int lastIdx = i;

            while (!sortedValues.isEmpty()) {
                ValueAndIndex first = sortedValues.peekFirst();

                if (first.value >= curVal) {
                    lastIdx = first.index;
                    sortedValues.pollFirst();
                } else {
                    break;
                }
            }

            suffixArea[i] = curVal * (lastIdx - i + 1);
            sortedValues.addFirst(new ValueAndIndex(curVal, lastIdx));
        }

        return suffixArea;
    }

    record ValueAndIndex(int value, int index) {}
}
