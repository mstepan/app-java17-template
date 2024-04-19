package com.github.mstepan.app17.leetcode.hard;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/*
403. Frog Jump
https://leetcode.com/problems/frog-jump/description/
 */
public class FrogJump {

    public static void main(String[] args) throws Exception {

        System.out.printf(
                "canCross : %b%n", new FrogJump().canCross(new int[] {0, 1, 3, 5, 6, 8, 12, 17}));

        System.out.printf(
                "canCross : %b%n", new FrogJump().canCross(new int[] {0, 1, 2, 3, 4, 8, 9, 11}));

        System.out.printf("canCross : %b%n", new FrogJump().canCross(new int[] {0, 2, 3, 4, 5}));

        System.out.println("FrogJump done...");
    }

    /** time: O(N^2 * 3 * lgN), when N = 2000, max ~ 120M space: O(N^2) */
    public boolean canCross(int[] stones) {
        Objects.requireNonNull(stones);
        if (stones.length < 2) {
            throw new IllegalArgumentException("Can't determine jump for array with length < 2");
        }
        if (stones[0] != 0) {
            throw new IllegalArgumentException("stones[0] != 0");
        }

        if (stones[1] != 1) {
            return false;
        }

        Set<Integer>[] solutions = new Set[stones.length];
        solutions[0] = Set.of(0);
        solutions[1] = Set.of(1);

        for (int i = 2; i < solutions.length; ++i) {
            solutions[i] = new HashSet<>();
        }

        for (int i = 1; i < stones.length; ++i) {
            Set<Integer> curSol = solutions[i];

            for (int jump : curSol) {
                for (int offset = -1; offset < 2; ++offset) {
                    int val = stones[i] + (jump + offset);

                    int index = Arrays.binarySearch(stones, i + 1, stones.length, val);

                    if (index >= 0) {
                        assert index < solutions.length;
                        assert solutions[index] != null;

                        solutions[index].add(jump + offset);
                    }
                }
            }
        }

        return !solutions[solutions.length - 1].isEmpty();
    }
}
