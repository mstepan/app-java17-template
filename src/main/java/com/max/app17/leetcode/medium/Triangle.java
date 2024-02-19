package com.max.app17.leetcode.medium;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * 120. Triangle
 *
 * <p>https://leetcode.com/problems/triangle/
 */
public final class Triangle {

    public static void main(String[] args) throws Exception {

        List<List<Integer>> triangle =
                List.of(List.of(2), List.of(3, 4), List.of(6, 5, 7), List.of(4, 1, 8, 3));

        //        List<List<Integer>> triangle = List.of(List.of(-10));

        //        List<List<Integer>> triangle = List.of(List.of(1), List.of(2, 3));

        int minPathValue = new Triangle().minimumTotal(triangle);

        System.out.printf("minPathValue: %d%n", minPathValue);

        System.out.println("Triangle done...");
    }

    /**
     * N = rows count
     *
     * <p>K = cols count
     *
     * <p>time: O(N*K)
     *
     * <p>space: O(K)
     */
    public int minimumTotal(List<List<Integer>> triangle) {
        Objects.requireNonNull(triangle);

        if (triangle.isEmpty()) {
            return 0;
        }

        int[] cur = new int[triangle.get(triangle.size() - 1).size() + 1];

        int expectedRowSize = 1;
        for (List<Integer> row : triangle) {

            if (row.size() != expectedRowSize) {
                throw new IllegalArgumentException("Array is not a triangle");
            }

            int[] next = maxIntsArray(cur.length);

            for (int col = 0; col < row.size(); ++col) {
                int val = row.get(col);

                next[col] = Math.min(val + cur[col], next[col]);
                next[col + 1] = Math.min(val + cur[col], next[col + 1]);
            }

            cur = next;
            ++expectedRowSize;
        }

        return Arrays.stream(cur).min().getAsInt();
    }

    private static int[] maxIntsArray(int length) {
        int[] newArr = new int[length];
        Arrays.fill(newArr, Integer.MAX_VALUE);
        return newArr;
    }
}
