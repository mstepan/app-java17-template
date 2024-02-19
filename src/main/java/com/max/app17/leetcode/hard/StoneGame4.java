package com.max.app17.leetcode.hard;

/** 1510. Stone Game IV https://leetcode.com/problems/stone-game-iv/ */
public class StoneGame4 {

    public static void main(String[] args) throws Exception {
        int n = 10;
        boolean res = new StoneGame4().winnerSquareGame(n);

        System.out.printf("res: %b%n", res);

        System.out.println("StoneGame4 done...");
    }

    /** time: O(N*sqrt(n)) space: O(N) */
    public boolean winnerSquareGame(int n) {
        assert n >= 1 : "n < 1";

        if (n == 1) {
            return true;
        }

        boolean[] sol = new boolean[n + 1];
        sol[0] = false;
        sol[1] = true;

        for (int i = 2; i < sol.length; ++i) {
            boolean curSol = true;
            for (int k = 1; k * k <= i && curSol; ++k) {
                curSol = sol[i - k * k];
            }

            sol[i] = !curSol;
        }

        return sol[sol.length - 1];
    }
}
