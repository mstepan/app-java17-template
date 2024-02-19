package com.max.app17.leetcode.hard;

import java.util.Arrays;
import java.util.List;

/*
315. Count of Smaller Numbers After Self
https://leetcode.com/problems/count-of-smaller-numbers-after-self/
 */
public class CountOfSmallerNumbersAfterSelf {

    public static void main(String[] args) throws Exception {

        int[] arr = {3, 8, 5, 3, 6, 1, 5, 2, 8, 7, 2, 6};

        List<Integer> counts = new CountOfSmallerNumbersAfterSelf().countSmaller(arr);

        System.out.printf("counts: %s%n", counts);

        System.out.println("CountOfSmallerNumbersAfterSelf done...");
    }

    /*
    We will use algorithm similar to merge-sort.
    time: O(N*lgN)
    space: O(N)
    */
    public List<Integer> countSmaller(int[] nums) {
        ValueAndCount[] arr = new ValueAndCount[nums.length];

        for (int i = 0; i < nums.length; ++i) {
            arr[i] = new ValueAndCount(nums[i], i, 0);
        }

        countSmallerRecursively(arr, 0, arr.length - 1);

        return toList(arr);
    }

    static final class ValueAndCount {
        private final int value;
        private final int index;

        private int count;

        ValueAndCount(int value, int index, int count) {
            this.value = value;
            this.index = index;
            this.count = count;
        }

        void incCount() {
            count += 1;
        }

        int count() {
            return count;
        }

        public void addCount(int value) {
            assert value >= 0;
            count += value;
        }

        @Override
        public String toString() {
            return "value: %d, count: %d, index: %d".formatted(value, count, index);
        }
    }

    private void countSmallerRecursively(ValueAndCount[] arr, int from, int to) {

        final int elemsCount = to - from + 1;

        // use brute-force algorithm
        if (elemsCount < 3) {
            // calculate inversions count using brute-force algorithm with two nested loops
            for (int i = from; i < to; ++i) {
                for (int j = i + 1; j <= to; ++j) {
                    if (arr[i].value > arr[j].value) {
                        arr[i].incCount();
                    }
                }
            }

            // insertion-like sort
            for (int i = from + 1; i <= to; ++i) {
                ValueAndCount temp = arr[i];

                int j = i - 1;

                while (j >= from && arr[j].value > temp.value) {
                    arr[j + 1] = arr[j];
                    --j;
                }

                arr[j + 1] = temp;
            }
            return;
        }

        // use divide-and-conquer for bigger chunks

        int mid = from + (to - from) / 2;

        // solve left side
        countSmallerRecursively(arr, from, mid);

        // solve right side
        countSmallerRecursively(arr, mid + 1, to);

        // merge sorted left and right sides
        calculateCounterInPlace(arr, from, mid, to);

        ValueAndCount[] sortedChunk = new ValueAndCount[to - from + 1];

        int left = from;
        int right = mid + 1;

        for (int i = 0; i < sortedChunk.length; ++i) {

            // left part is empty
            if (left > mid) {
                sortedChunk[i] = arr[right];
                ++right;
            }
            // right part is empty
            else if (right > to) {
                sortedChunk[i] = arr[left];
                ++left;
            }
            // both part are not empty
            else {
                if (arr[left].value <= arr[right].value) {
                    sortedChunk[i] = arr[left];
                    ++left;
                } else {
                    sortedChunk[i] = arr[right];
                    ++right;
                }
            }
        }

        System.arraycopy(sortedChunk, 0, arr, from, sortedChunk.length);
    }

    private void calculateCounterInPlace(ValueAndCount[] arr, int from, int mid, int to) {
        int left = from;
        int right = mid + 1;
        final int boundary = mid + 1;

        while (left <= mid) {

            while (right <= to && arr[right].value < arr[left].value) {
                ++right;
            }

            arr[left].addCount(right - boundary);

            ++left;
        }
    }

    private List<Integer> toList(ValueAndCount[] arr) {

        int[] counters = new int[arr.length];

        for (ValueAndCount valueAndCount : arr) {
            counters[valueAndCount.index] = valueAndCount.count;
        }

        return Arrays.stream(counters).boxed().toList();
    }
}
