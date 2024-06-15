package com.github.mstepan.app17.leetcode.medium;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class MinimizeHammingDistanceAfterSwapOperationsTest {

    @Test
    void hammingDistanceCase1() {
        int[] source = {11, 44, 22, 88, 22};
        int[] target = {22, 44, 22, 88, 55};
        int[][] allowedSwaps = {{2, 4}, {0, 2}};

        int hammingDistance =
                new MinimizeHammingDistanceAfterSwapOperations()
                        .minimumHammingDistance(source, target, allowedSwaps);

        assertEquals(1, hammingDistance);
    }

    @Test
    void hammingDistanceCase2() {
        int[] source = {1, 2, 3, 4};
        int[] target = {1, 3, 2, 4};
        int[][] allowedSwaps = {};

        int hammingDistance =
                new MinimizeHammingDistanceAfterSwapOperations()
                        .minimumHammingDistance(source, target, allowedSwaps);

        assertEquals(2, hammingDistance);
    }

    @Test
    void hammingDistanceCase3() {
        int[] source = {5, 1, 2, 4, 3};
        int[] target = {1, 5, 4, 2, 3};
        int[][] allowedSwaps = {{0, 4}, {4, 2}, {1, 3}, {1, 4}};

        int hammingDistance =
                new MinimizeHammingDistanceAfterSwapOperations()
                        .minimumHammingDistance(source, target, allowedSwaps);

        assertEquals(0, hammingDistance);
    }

    @Test
    void hammingDistanceCase4() {
        int[] source = {1, 2, 3, 4};
        int[] target = {2, 1, 4, 5};
        int[][] allowedSwaps = {{0, 1}, {2, 3}};

        int hammingDistance =
                new MinimizeHammingDistanceAfterSwapOperations()
                        .minimumHammingDistance(source, target, allowedSwaps);

        assertEquals(1, hammingDistance);
    }

    @Test
    void hammingDistanceCase5() {
        int[] source = {18, 67, 10, 36, 17, 62, 38, 78, 52};
        int[] target = {3, 4, 99, 36, 26, 58, 29, 33, 74};
        int[][] allowedSwaps = {
            {4, 7}, {3, 1}, {8, 4}, {5, 6}, {2, 8}, {0, 7}, {1, 6}, {3, 7}, {2, 5}, {3, 0}, {8, 5},
            {2, 1}, {6, 7}, {5, 1}, {3, 6}, {4, 0}, {7, 2}, {2, 6}, {4, 1}, {3, 2}, {8, 6}, {8, 0},
            {5, 3}, {1, 0}, {4, 6}, {8, 7}, {5, 7}, {3, 8}, {6, 0}, {8, 1}, {7, 1}, {5, 0}, {4, 3},
            {0, 2}
        };

        int hammingDistance =
                new MinimizeHammingDistanceAfterSwapOperations()
                        .minimumHammingDistance(source, target, allowedSwaps);

        assertEquals(8, hammingDistance);
    }
}
