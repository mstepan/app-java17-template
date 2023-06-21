package com.max.app17.leetcode.hard;

import java.util.PriorityQueue;

/**
 * 798. Smallest Rotation with Highest Score
 * <p>
 * https://leetcode.com/problems/smallest-rotation-with-highest-score/
 */
public class SmallestRotationWithHighestScore {

    public static void main(String[] args) throws Exception {

        int[] arr = {2, 3, 1, 4, 0};

        System.out.println(new SmallestRotationWithHighestScore().bestRotation(arr));

        System.out.println("SmallestRotationWithHighestScore done...");
    }

    /**
     * time: O(N*lgN)
     * space: O(N)
     */
    public int bestRotation(int[] arr) {

        assert arr != null : "null 'arr' detected";

        PriorityQueue<Integer> minHeap = new PriorityQueue<>();

        for (int i = 0; i < arr.length; ++i) {
            int diff = i - arr[i];
            if (diff >= 0) {
                minHeap.add(diff);
            }
        }

        int offset = 0;
        int bestScore = minHeap.size();

        for (int k = 1; k < arr.length; ++k) {
            while (!minHeap.isEmpty() && minHeap.peek() < k) {
                minHeap.poll();
            }

            int movedElemScore = (arr.length - 1) - arr[k - 1] + k;

            if (movedElemScore >= k) {
                minHeap.add(movedElemScore);
            }

            if (minHeap.size() > bestScore) {
                bestScore = minHeap.size();
                offset = k;
            }
        }

        return offset;
    }


}
