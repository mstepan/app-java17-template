package com.max.app17.leetcode.hard;

/**
 * 154. Find Minimum in Rotated Sorted Array II
 * <p>
 * https://leetcode.com/problems/find-minimum-in-rotated-sorted-array-ii/
 */
public class FindMinimumInRotatedSortedArray2 {

    public static void main(String[] args) throws Exception {

        int[] arr = {3, 3, 1, 3};

        System.out.println(new FindMinimumInRotatedSortedArray2().findMin(arr));
        System.out.println("FindMinimumInRotatedSortedArray2 done...");
    }

    /**
     * time: O(N), we can't do better with duplicates
     * space: O(1)
     */
    public int findMin(int[] arr) {

        int minValue = arr[0];

        for(int i =1; i < arr.length; ++i){
            minValue = Math.min(minValue, arr[i]);
        }

        return minValue;
    }

    /*
    public int findMin(int[] arr) {
        return findMinRec(arr, 0, arr.length-1);
    }

    private int findMinRec(int[] arr, int from, int to) {

        if (from - to + 1 < 10) {
            int minVal = arr[from];
            for (int i = from + 1; i <= to; ++i) {
                minVal = Math.min(minVal, arr[i]);
            }
            return minVal;
        }

        int lo = from;
        int hi = to;

        while (lo < hi) {
            int mid = lo + (hi - lo) / 2;

            if (arr[hi] == arr[mid]) {
                return Math.min(findMinRec(arr, mid + 1, hi), findMinRec(arr, lo, mid));
            }
            else if (arr[hi] > arr[mid]) {
                hi = mid;
            }
            else {
                lo = mid + 1;
            }
        }
        return arr[lo];
    }
     */

}
