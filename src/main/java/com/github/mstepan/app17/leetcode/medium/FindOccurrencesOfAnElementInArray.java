package com.github.mstepan.app17.leetcode.medium;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * 3159. Find Occurrences of an Element in an Array
 *
 * <p>https://leetcode.com/problems/find-occurrences-of-an-element-in-an-array/description/
 */
public class FindOccurrencesOfAnElementInArray {

    public static void main(String[] args) {

        int[] nums = {1, 3, 1, 7};
        int[] queries = {1, 3, 2, 4};
        int val = 1;

        // expected: [0, -1, 2, -1]
        // actual:   [0, -1, 2, -1]
        int[] res =
                new FindOccurrencesOfAnElementInArray().occurrencesOfElement(nums, queries, val);

        System.out.println(Arrays.toString(res));

        System.out.println("FindOccurrencesOfAnElementInArray done...");
    }

    /**
     * time: O(N)
     *
     * <p>spaceL O(N)
     */
    public int[] occurrencesOfElement(int[] nums, int[] queries, int searchVal) {
        Objects.requireNonNull(nums);
        Objects.requireNonNull(queries);

        List<Integer> indexesOfVal = new ArrayList<>();
        for (int i = 0; i < nums.length; ++i) {
            if (nums[i] == searchVal) {
                indexesOfVal.add(i);
            }
        }

        int[] res = new int[queries.length];
        for (int i = 0; i < queries.length; ++i) {

            int valueOccIdx = queries[i];

            assert valueOccIdx > 0 : "Incorrect query index detected: " + valueOccIdx;

            if (valueOccIdx > indexesOfVal.size()) {
                res[i] = -1;
            } else {
                res[i] = indexesOfVal.get(valueOccIdx - 1);
            }
        }

        return res;
    }
}
