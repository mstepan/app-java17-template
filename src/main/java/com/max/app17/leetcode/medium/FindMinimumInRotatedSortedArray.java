package com.max.app17.leetcode.medium;

/**
 * 153. Find Minimum in Rotated Sorted Array
 *
 * https://leetcode.com/problems/find-minimum-in-rotated-sorted-array/
 */
public class FindMinimumInRotatedSortedArray {

    public static void main(String[] args) throws Exception {

        int[] arr = {44, 56, 77, 5, 7, 8, 12, 33};

        System.out.println(new FindMinimumInRotatedSortedArray().findMin(arr));
        System.out.println("FindMinimumInRotatedSortedArray2 done...");
    }

    /**
     * Time: O(lgN)
     * Space: O(1)
     */
    public int findMin(int[] arr) {
        int lo = 0;
        int hi = arr.length - 1;

        while (lo < hi) {
            int mid = lo + (hi - lo) / 2;

            if (arr[hi] >= arr[mid]) {
                hi = mid;
            }
            else {
                lo = mid + 1;
            }
        }
        return arr[lo];
    }
}
