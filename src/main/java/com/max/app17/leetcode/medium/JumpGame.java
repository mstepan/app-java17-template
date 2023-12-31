package com.max.app17.leetcode.medium;

import java.util.Objects;

/**
 * 55. Jump Game
 *
 * <p>https://leetcode.com/problems/jump-game/
 */
public class JumpGame {

    public static void main(String[] args) {
        int[] arr = {}; //{2, 3, 1, 1, 4};

        // int[] arr = {3, 2, 1, 0, 4};

        boolean canJump = new JumpGame().canJump(arr);

        System.out.printf("canJump: %b%n", canJump);
    }

    /** time: O(N) space: O(N) */
    public boolean canJump(int[] arr) {
        Objects.requireNonNull(arr);
        if( arr.length == 0 ){
            throw new IllegalArgumentException("arr.length is 0");
        }
        // 1
        if (arr.length < 2) {
            return true;
        }

        final int lastIndex = arr.length - 1;

        boolean[] jumpRes = new boolean[arr.length];
        jumpRes[lastIndex] = true;

        for (int i = jumpRes.length - 2; i >= 0; --i) {
            int jumpSize = arr[i];

            if (i + jumpSize >= lastIndex) {
                jumpRes[i] = true;
            } else {
                for (int offset = 1; offset <= jumpSize && !jumpRes[i]; ++offset) {
                    assert i + offset < jumpRes.length - 1;

                    jumpRes[i] = jumpRes[i] || jumpRes[i + offset];
                }
            }
        }

        return jumpRes[0];
    }
}
