package com.max.app17.geeks;

import java.util.Arrays;
import java.util.Objects;

/**
 * <a href="https://www.geeksforgeeks.org/rearrange-array-such-that-adjacent-difference-is-odd/">rearrange-array-such-that-adjacent-difference-is-odd</a>
 *
 * Given an arr[] containing distinct positive integers of length N(2 ≤ N), the task is to rearrange the array elements in such
 * a way that each element has an odd absolute difference between adjacent elements.
 */
public final class RearrangeArrayWithOddAdjacentDifference {

    private RearrangeArrayWithOddAdjacentDifference() {
        throw new AssertionError("Can't instantiate utility-only class");
    }

    public static void main(String[] args) {
        int[] arr = {3, 2, 4, 5, 7, 6, 9};
        if( rearrange(arr) ){
            System.out.println(Arrays.toString(arr));
        }
    }

    /**
     * Rearrange array elements in place, so that absolute difference between adjacent elements is ODD.
     * time: O(N)
     * space: O(1), in-place
     */
    public static boolean rearrange(int[] arr) {

        Objects.requireNonNull(arr, "null 'arr' detected");

        // 0 or 1 element array holds mentioned above property
        if (arr.length < 2) {
            return true;
        }

        int boundary = rearrangeOddThenEven(arr);

        final int oddCount = boundary + 1;
        final int evenCount = arr.length - (boundary + 1);

        if (Math.abs(oddCount - evenCount) > 1) {
            return false;
        }

        int left = (oddCount > evenCount) ? 1 : 0;
        int right = (evenCount > oddCount) ? arr.length - 2 : arr.length - 1;

        while (left < right) {
            swap(arr, left, right);
            left += 2;
            right -= 2;
        }

        return true;
    }

    /**
     * In one pass rearrange array elements, so that all odd elements appear before even.
     * Use quicksort-like single pass.
     * time: O(N), space O(1)
     */
    private static int rearrangeOddThenEven(int[] arr) {
        assert arr != null;

        int lastOddIndex = -1;

        for (int i = 0; i < arr.length; ++i) {
            if (isOdd(arr[i])) {
                swap(arr, i, lastOddIndex + 1);
                ++lastOddIndex;
            }
        }

        return lastOddIndex;
    }

    /**
     * Check if value is odd.
     */
    static boolean isOdd(int value) {
        return (value & 1) != 0;
    }

    /**
     * Swap two elements of an array.
     */
    static void swap(int[] arr, int from, int to) {
        assert arr != null;
        assert from >= 0 && from < arr.length;
        assert to >= 0 && to < arr.length;

        if (from == to) {
            return;
        }
        int temp = arr[from];
        arr[from] = arr[to];
        arr[to] = temp;
    }

}
