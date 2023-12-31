package com.max.app17.leetcode.medium;

import java.util.Arrays;
import java.util.Objects;

/**
 * 75. Sort Colors
 *
 * <p>https://leetcode.com/problems/sort-colors/
 */
public class SortColors {

    public static void main(String[] args) {

        int[] colors = {2, 0, 1, 0, 1, 1, 0, 2, 0};

        System.out.printf("Before: %s%n", Arrays.toString(colors));

        new SortColors().sortColors(colors);

        System.out.printf("After %s%n", Arrays.toString(colors));

        System.out.println("SortColors done");
    }


    /**
     *
     * Use counting sort to sort array in place in linear time.
     *
     * time: O(N)
     * space: O(1)
     */
    public void sortColors(int[] arr) {
        Objects.requireNonNull(arr);

        int[] colorsFreq = new int[3];

        for (int val : arr) {
            if (val < 0 || val >= colorsFreq.length) {
                throw new IllegalArgumentException("Invalid color detected: %d".formatted(val));
            }

            colorsFreq[val] += 1;
        }

        for(int color = 0, idx = 0; color < colorsFreq.length; ++color){
            int cnt = colorsFreq[color];

            for(int i = 0; i < cnt; ++i){
                arr[idx] = color;
                ++idx;
            }
        }
    }
}
