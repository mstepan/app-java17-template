package com.github.mstepan.app17.leetcode.medium;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 39. Combination Sum
 *
 * <p>https://leetcode.com/problems/combination-sum/description/
 *
 * <p>N = candidates.length (max = 40) M = target (max = 40)
 *
 * <p>K = 150 (The test cases are generated such that the number of unique combinations that sum up
 * to target is less than 150 combinations for the given input.)
 *
 * <p>time: O(N*M*K) space: O(M*K)
 */
public class CombinationSum {

    public static void main(String[] args) throws Exception {

        int[] candidates = {2, 3, 5};
        int target = 8;

        List<List<Integer>> result = combinationSum(candidates, target);

        System.out.println("result: " + result);

        System.out.println("CombinationSum done...");
    }

    public static List<List<Integer>> combinationSum(int[] candidates, int target) {
        PartialSolution[] solutions = new PartialSolution[target + 1];
        solutions[0] = PartialSolution.empty();

        for (int cur = 1; cur < solutions.length; ++cur) {

            PartialSolution curSolution = new PartialSolution();

            for (int singleCandidate : candidates) {
                if (singleCandidate <= cur) {
                    PartialSolution prev = solutions[cur - singleCandidate];
                    curSolution.add(prev.combine(singleCandidate));
                }
            }

            curSolution.removeDuplicatesInPlace();

            solutions[cur] = curSolution;
        }

        PartialSolution finalRes = solutions[solutions.length - 1];
        return finalRes.toList();
    }

    private static class PartialSolution {
        List<int[]> solution;

        public PartialSolution() {
            solution = new ArrayList<>();
        }

        public static PartialSolution empty() {
            PartialSolution value = new PartialSolution();
            value.solution = List.of(new int[0]);
            return value;
        }

        public List<int[]> combine(int singleCandidate) {
            List<int[]> res = new ArrayList<>();

            for (int[] arr : solution) {
                int[] newArr = new int[arr.length + 1];
                System.arraycopy(arr, 0, newArr, 0, arr.length);
                newArr[newArr.length - 1] = singleCandidate;
                Arrays.sort(newArr);
                res.add(newArr);
            }

            return res;
        }

        public void add(List<int[]> valuesToAdd) {
            solution.addAll(valuesToAdd);
        }

        public void removeDuplicatesInPlace() {
            solution.sort(Arrays::compare);

            //  {1, 4}, {1,4},, {2, 3},  {2, 3}, {2,3}, {5}

            Iterator<int[]> it = solution.iterator();

            if (!it.hasNext()) {
                return;
            }

            int[] prev = it.next();

            while (it.hasNext()) {
                int[] cur = it.next();

                if (Arrays.equals(prev, cur)) {
                    it.remove();
                }

                prev = cur;
            }
        }

        @Override
        public String toString() {

            StringBuilder buf = new StringBuilder();

            for (int[] singleSolution : solution) {
                buf.append(Arrays.toString(singleSolution)).append(", ");
            }

            return buf.toString();
        }

        public List<List<Integer>> toList() {
            List<List<Integer>> result = new ArrayList<>();

            for (int[] singleArr : solution) {
                result.add(Arrays.stream(singleArr).boxed().collect(Collectors.toList()));
            }

            return result;
        }
    }
}
