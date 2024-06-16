package com.github.mstepan.app17.leetcode.medium;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LargestMagicSquareTest {

    @Test
    void gridNormalCase() {
        int[][] grid = {
            {7, 1, 4, 5, 6},
            {2, 5, 1, 6, 4},
            {1, 5, 4, 3, 2},
            {1, 2, 7, 3, 4}
        };
        assertEquals(3, new LargestMagicSquare().largestMagicSquare(grid));
    }

    @Test
    void emptyGrid() {
        assertEquals(0, new LargestMagicSquare().largestMagicSquare(new int[][] {}));
        assertEquals(0, new LargestMagicSquare().largestMagicSquare(new int[][] {new int[0]}));
    }
}
