package com.max.app17.leetcode.medium;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/** 494. Target Sum https://leetcode.com/problems/target-sum/ */
public class TargetSum {

    public static void main(String[] args) throws Exception {

        int countWays = new TargetSum().findTargetSumWays(new int[] {1, 1, 1, 1, 1}, 3);

        System.out.println(countWays);

        System.out.println("TargetSum done...");
    }

    /**
     * N = arr.length K = 2 * sum(arr)
     *
     * <p>time: O(N * K) space: O(K)
     */
    public int findTargetSumWays(int[] arr, int target) {
        Objects.requireNonNull(arr, "null 'arr' value detected");

        Map<Integer, Integer> curLevel = Map.of(0, 1);

        for (int val : arr) {
            Map<Integer, Integer> nextLevel = new LinkedHashMap<>();

            for (Map.Entry<Integer, Integer> entry : curLevel.entrySet()) {
                addKeyWithCounter(entry, val, nextLevel);
                addKeyWithCounter(entry, -val, nextLevel);
            }

            curLevel = nextLevel;
        }

        return curLevel.getOrDefault(target, 0);
    }

    private static void addKeyWithCounter(
            Map.Entry<Integer, Integer> curEntry, int val, Map<Integer, Integer> nextLevel) {

        int nextKey = curEntry.getKey() + val;
        int nextCount = curEntry.getValue();

        nextLevel.compute(
                nextKey,
                (notUsed, curCount) -> curCount == null ? nextCount : curCount + nextCount);
    }
}
