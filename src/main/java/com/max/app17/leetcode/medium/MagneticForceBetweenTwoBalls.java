package com.max.app17.leetcode.medium;

import java.util.Arrays;
import java.util.Objects;

/**
 * 1552. Magnetic Force Between Two Balls
 *
 * <p>https://leetcode.com/problems/magnetic-force-between-two-balls/
 */
public class MagneticForceBetweenTwoBalls {

    public static void main(String[] args) throws Exception {

        int[] positions = {22, 57, 74, 79}; // {1, 5, 7, 8, 10, 12, 20, 27, 32, 45, 65, 70};

        // expected maxPossibleDistance: 12
        // int m = 5;
        int m = 4;

        int maxPossibleDistance = new MagneticForceBetweenTwoBalls().maxDistance(positions, m);

        System.out.printf("maxPossibleDistance: %d%n", maxPossibleDistance);

        System.out.println("MagneticForceBetweenTwoBalls done...");
    }

    /**
     * Binary search the answer.
     *
     * <p>time: O(lg10^9 * N) ~ O(30*N)
     *
     * <p>space: O(1)
     */
    public int maxDistance(int[] position, int m) {
        Objects.requireNonNull(position);
        if (position.length < 2) {
            throw new IllegalArgumentException(
                    "'position.length should be greater or equal to '2': %d"
                            .formatted(position.length));
        }
        if (m < 2) {
            throw new IllegalArgumentException("'m' should be greater than '2': %d".formatted(m));
        }

        if (m > position.length) {
            return -1;
        }

        Arrays.sort(position);

        int lo = minDiffBetweenSortedPairs(position);
        int hi = position[position.length - 1] - position[0];

        int sol = -1;

        while (lo <= hi) {
            int mid = lo + (hi - lo) / 2;

            if (hasSolution(position, m, mid)) {
                sol = mid;
                lo = mid + 1;
            } else {
                hi = mid - 1;
            }
        }

        return sol;
    }

    private int minDiffBetweenSortedPairs(int[] position) {
        assert position != null;
        assert position.length >= 2;

        int diff = Integer.MAX_VALUE;

        for (int i = 1; i < position.length; ++i) {
            diff = Math.min(diff, position[i] - position[i - 1]);
        }
        return diff;
    }

    private static boolean hasSolution(int[] position, int m, int minDistance) {
        m -= 1;
        int lastVal = position[0];

        for (int i = 1; i < position.length && m > 0; ++i) {

            int curDistance = position[i] - lastVal;

            if (curDistance >= minDistance) {
                --m;
                lastVal = position[i];
            }
        }

        return m == 0;
    }

    /**
     * Too slow, won;t work. Time limit exceeded. M = m
     *
     * <p>N = position.length
     *
     * <p>time: O(M*N*N)
     */
    public int maxDistanceDynamic(int[] position, int m) {
        Objects.requireNonNull(position);
        if (position.length < 2) {
            throw new IllegalArgumentException(
                    "'position.length should be greater or equal to '2': %d"
                            .formatted(position.length));
        }
        if (m < 2) {
            throw new IllegalArgumentException("'m' should be greater than '2': %d".formatted(m));
        }

        Arrays.sort(position);

        int[] curOpt = new int[position.length];
        curOpt[0] = Integer.MIN_VALUE;

        for (int i = 1; i < position.length; ++i) {
            curOpt[i] = (position[i] - position[0]);
        }

        int[] newOpt = new int[position.length];

        for (int curM = 3; curM <= m; ++curM) {

            Arrays.fill(newOpt, 0, curM - 1, Integer.MIN_VALUE);

            for (int i = curM - 1; i < newOpt.length; ++i) {

                int bestSol = Integer.MIN_VALUE;

                for (int j = i - 1; j >= 0 && curOpt[j] > bestSol; --j) {
                    bestSol = Math.max(bestSol, Math.min(curOpt[j], (position[i] - position[j])));
                }

                newOpt[i] = bestSol;
            }

            System.arraycopy(newOpt, 0, curOpt, 0, newOpt.length);

            // curOpt = newOpt;
        }

        return curOpt[curOpt.length - 1];
    }
}
