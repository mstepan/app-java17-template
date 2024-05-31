package com.github.mstepan.app17.leetcode.medium;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 3160. Find the Number of Distinct Colors Among the Balls
 *
 * <p>https://leetcode.com/problems/find-the-number-of-distinct-colors-among-the-balls/description/
 */
public class FindNumberOfDistinctColorsAmongBalls {

    public static void main(String[] args) {

        int limit = 4;
        int[][] queries = {{1, 4}, {2, 5}, {1, 3}, {3, 4}};

        // expected: [1, 2, 2, 3]
        // actual:   [1, 2, 2, 3]
        int[] res = new FindNumberOfDistinctColorsAmongBalls().queryResults(limit, queries);

        System.out.println(Arrays.toString(res));

        System.out.println("FindNumberOfDistinctColorsAmongBalls done...");
    }

    /**
     * time: O(N)
     *
     * <p>space: O(N)
     */
    public int[] queryResults(int limit, int[][] queries) {
        if (limit < 1) {
            throw new IllegalArgumentException("Limit value is incorrect: " + limit);
        }
        Objects.requireNonNull(queries);

        Map<Integer, Integer> indexToColor = new HashMap<>();

        Map<Integer, Integer> colorFreq = new HashMap<>();

        int[] res = new int[queries.length];

        for (int i = 0; i < queries.length; ++i) {
            int[] singleQuery = queries[i];
            Objects.requireNonNull(singleQuery);
            assert singleQuery.length == 2 : "query incorrect length: " + singleQuery.length;

            int index = singleQuery[0];
            int color = singleQuery[1];

            Integer prevColorAtIndex = indexToColor.put(index, color);

            if (prevColorAtIndex != null) {
                colorFreq.compute(
                        prevColorAtIndex,
                        (key, freq) -> {
                            assert freq != null;
                            freq -= 1;
                            return (freq == 0) ? null : freq;
                        });
            }

            colorFreq.compute(color, (key, freq) -> (freq == null) ? 1 : freq + 1);

            res[i] = colorFreq.size();
        }

        return res;
    }
}
