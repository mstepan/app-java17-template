package com.github.mstepan.app17.leetcode.medium;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * 40. Combination Sum II
 *
 * <p>https://leetcode.com/problems/combination-sum-ii/description/
 */
public class CombinationSum2 {

    public static void main(String[] args) {

        int[] candidates = {10, 1, 2, 7, 6, 1, 5};
        int target = 8;

        List<List<Integer>> allCombinations =
                new CombinationSum2().combinationSum2(candidates, target);

        /*
        Expected:
        [
        [1,1,6],
        [1,2,5],
        [1,7],
        [2,6]
        ]
        */

        for (List<Integer> singleSol : allCombinations) {
            System.out.println(singleSol);
        }

        System.out.println("CombinationSum2 done ...");
    }

    /**
     * N = candidates.length
     *
     * <p>M = target
     *
     * <p>time: O(N*M*N) ~ O(N^2*M)
     *
     * <p>space: O(N*M*N) ~ O(N^2*M)
     */
    public List<List<Integer>> combinationSum2(int[] candidates, int target) {
        Objects.requireNonNull(candidates);
        if (target <= 0) {
            throw new IllegalArgumentException(
                    "target should be positive value, but found " + target);
        }

        final int rows = candidates.length + 1;
        final int cols = target + 1;

        CombinedResult[][] combinedResults = new CombinedResult[rows][cols];

        for (int row = 0; row < rows; ++row) {
            combinedResults[row][0] = CombinedResult.empty();
        }

        for (int row = 1; row < rows; ++row) {

            for (int col = 1; col < cols; ++col) {

                final int curCandidate = candidates[row - 1];
                final int curTarget = col;

                if (curCandidate > curTarget) {
                    combinedResults[row][col] = combinedResults[row - 1][curTarget];
                    continue;
                }

                List<List<Integer>> combined = new ArrayList<>();

                CombinedResult solWithoutCur = combinedResults[row - 1][curTarget];
                if (solWithoutCur != null) {
                    combined.addAll(solWithoutCur.data);
                }

                CombinedResult solUsingCur = combinedResults[row - 1][curTarget - curCandidate];

                if (solUsingCur != null) {
                    for (List<Integer> singleSol : solUsingCur.data) {
                        List<Integer> copy = new ArrayList<>(singleSol);
                        copy.add(curCandidate);
                        Collections.sort(copy);
                        combined.add(copy);
                    }
                }

                if (!combined.isEmpty()) {
                    combinedResults[row][col] = new CombinedResult(removeDuplicates(combined));
                }
            }
        }

        if (combinedResults[rows - 1][cols - 1] == null) {
            return List.of();
        }

        return combinedResults[rows - 1][cols - 1].toLists();
    }

    private List<List<Integer>> removeDuplicates(List<List<Integer>> combined) {
        Set<List<Integer>> unique = new HashSet<>(combined);
        return new ArrayList<>(unique);
    }

    static class CombinedResult {
        final List<List<Integer>> data;

        CombinedResult(List<List<Integer>> data) {
            this.data = data;
        }

        static CombinedResult empty() {
            return new CombinedResult(List.of(List.of()));
        }

        List<List<Integer>> toLists() {
            return data;
        }

        @Override
        public String toString() {

            StringBuilder buf = new StringBuilder();

            buf.append("[");

            for (List<Integer> singleCombination : data) {
                buf.append(singleCombination.toString()).append(", ");
            }

            buf.append("]");

            return buf.toString();
        }
    }
}
