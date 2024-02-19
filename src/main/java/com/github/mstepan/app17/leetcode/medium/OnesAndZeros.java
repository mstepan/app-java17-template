package com.max.app17.leetcode.medium;

import java.util.HashMap;
import java.util.Map;

/*
474. Ones and Zeroes

https://leetcode.com/problems/ones-and-zeroes/
 */
public class OnesAndZeros {

    public static void main(String[] args) throws Exception {

        int m = 5;
        int n = 3;

        String[] arr = {"10", "0001", "111001", "1", "0"};

        int res = new OnesAndZeros().findMaxForm(arr, m, n);

        System.out.println(res);

        System.out.println("OnesAndZeros done...");
    }

    public int findMaxForm(String[] arr, int m, int n) {
        return optRec(arr, m, n, arr.length - 1);
    }

    private final Map<PartialSolution, Integer> cached = new HashMap<>();

    private int optRec(String[] arr, int zeros, int ones, int index) {
        if (index < 0) {
            return 0;
        }

        PartialSolution curSol = new PartialSolution(zeros, ones, index);

        if (cached.containsKey(curSol)) {
            return cached.get(curSol);
        }

        int bestSoFar = optRec(arr, zeros, ones, index - 1); // skip current element

        int curZeros = countZeros(arr[index]);
        int curOnes = countOnes(arr[index]);

        if (zeros >= curZeros && ones >= curOnes) {
            bestSoFar =
                    Math.max(
                            bestSoFar,
                            1 + optRec(arr, zeros - curZeros, ones - curOnes, index - 1));
        }

        cached.put(curSol, bestSoFar);

        return bestSoFar;
    }

    private int countZeros(String str) {
        return countChars(str, '0');
    }

    private int countOnes(String str) {
        return countChars(str, '1');
    }

    private int countChars(String str, char ch) {

        int cnt = 0;
        for (int i = 0; i < str.length(); ++i) {
            if (str.charAt(i) == ch) {
                ++cnt;
            }
        }

        return cnt;
    }

    static class PartialSolution {
        final int zeros;
        final int ones;
        final int index;

        PartialSolution(int zeros, int ones, int index) {
            this.zeros = zeros;
            this.ones = ones;
            this.index = index;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            PartialSolution partialSolution = (PartialSolution) o;

            if (zeros != partialSolution.zeros) return false;
            if (ones != partialSolution.ones) return false;
            return index == partialSolution.index;
        }

        @Override
        public int hashCode() {
            int result = zeros;
            result = 31 * result + ones;
            result = 31 * result + index;
            return result;
        }
    }
}
