package com.github.mstepan.app17.leetcode.medium;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

public class ConstructProductMatrixTest {

    @Test
    void constructProductMatrix1() {
        int[][] grid = {
            {1, 2},
            {3, 4}
        };

        int[][] expectedRes =
                new int[][] {
                    {24, 12},
                    {8, 6}
                };

        assertArrayEquals(expectedRes, new ConstructProductMatrix().constructProductMatrix(grid));
    }

    @Test
    void constructProductMatrix2() {
        int[][] grid = {{12345}, {2}, {1}};

        int[][] expectedRes = new int[][] {{2}, {0}, {0}};

        assertArrayEquals(expectedRes, new ConstructProductMatrix().constructProductMatrix(grid));
    }

    @Test
    void constructProductMatrixWithIntOverflow() {
        int[][] grid = {{414_750_857}, {449_145_368}, {767_292_749}};

        int[][] expectedRes = new int[][] {{1462}, {3103}, {9436}};

        assertArrayEquals(expectedRes, new ConstructProductMatrix().constructProductMatrix(grid));
    }
}
