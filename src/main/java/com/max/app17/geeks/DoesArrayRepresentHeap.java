package com.max.app17.geeks;

import java.util.Objects;

/**
 * Does array represent Heap.
 *
 * <p>https://www.geeksforgeeks.org/problems/does-array-represent-heap4345/1?utm_source=geeksforgeeks&utm_medium=newui_home&utm_campaign=potd
 */
public class DoesArrayRepresentHeap {

    public static void main(String[] args) throws Exception {

        // isMaxHeap => true
        long[] arr = {90, 15, 10, 7, 12, 2};

        // isMaxHeap => false
        //        long[] arr = {9, 15, 10, 7, 12, 11};
        boolean isMaxHeap = isMaxHeap(arr);

        System.out.printf("Array is max heap: %b %n", isMaxHeap);

        System.out.println("Main done...");
    }

    /** time: O(N) space: O(1) */
    public static boolean isMaxHeap(long arr[]) {
        Objects.requireNonNull(arr);

        for (int parent = 0; parent < arr.length / 2; ++parent) {
            int left = 2 * parent + 1;
            int right = 2 * parent + 2;

            assert left < arr.length;
            if (arr[left] > arr[parent]) {
                return false;
            }

            if (right < arr.length && arr[right] > arr[parent]) {
                return false;
            }
        }

        return true;
    }
}
