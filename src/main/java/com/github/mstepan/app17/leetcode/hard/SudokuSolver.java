package com.max.app17.leetcode.hard;

import java.util.Arrays;
import java.util.BitSet;

/**
 * 37. Sudoku Solver https://leetcode.com/problems/sudoku-solver/ Uses recursive backtracking
 * technique.
 */
public class SudokuSolver {

    public static void main(String[] args) throws Exception {

        char[][] board = {
            {'.', '.', '9', '7', '4', '8', '.', '.', '.'},
            {'7', '.', '.', '.', '.', '.', '.', '.', '.'},
            {'.', '2', '.', '1', '.', '9', '.', '.', '.'},
            {'.', '.', '7', '.', '.', '.', '2', '4', '.'},
            {'.', '6', '4', '.', '1', '.', '5', '9', '.'},
            {'.', '9', '8', '.', '.', '.', '3', '.', '.'},
            {'.', '.', '.', '8', '.', '3', '.', '2', '.'},
            {'.', '.', '.', '.', '.', '.', '.', '.', '6'},
            {'.', '.', '.', '2', '7', '5', '9', '.', '.'}
        };

        /*
        [
        ["5","1","9","7","4","8","6","3","2"],
        ["7","8","3","6","5","2","4","1","9"],
        ["4","2","6","1","3","9","8","7","5"],
        ["3","5","7","9","8","6","2","4","1"],
        ["2","6","4","3","1","7","5","9","8"],
        ["1","9","8","5","2","4","3","6","7"],
        ["9","7","5","8","6","3","1","2","4"],
        ["8","3","2","4","9","1","7","5","6"],
        ["6","4","1","2","7","5","9","8","3"]
        ]
         */

        solveSudoku(board);

        printBoard(board);

        System.out.println("SudokuSolver done...");
    }

    private static void printBoard(char[][] board) {
        StringBuilder buf = new StringBuilder();

        for (char[] singleRow : board) {
            buf.append(Arrays.toString(singleRow)).append("\n");
        }

        System.out.print(buf);
    }

    // ==== solution ====

    public static void solveSudoku(char[][] board) {

        final int boardSize = 9;

        final BitSet[] elemsInRow = createForDigits(boardSize);
        final BitSet[] elemsInCol = createForDigits(boardSize);
        final BitSet[] elemsInSquare = createForDigits(boardSize);

        int emptyCells = 0;

        for (int row = 0; row < boardSize; ++row) {
            for (int col = 0; col < boardSize; ++col) {
                char ch = board[row][col];

                if (ch == '.') {
                    ++emptyCells;
                } else {
                    int digit = ch - '0';
                    elemsInRow[row].set(digit);
                    elemsInCol[col].set(digit);
                    elemsInSquare[squareIndex(row, col)].set(digit);
                }
            }
        }

        solveRecursively(
                emptyCells,
                board,
                0,
                0,
                elemsInRow,
                elemsInCol,
                elemsInSquare,
                new boolean[] {false});
    }

    private static int squareIndex(int row, int col) {
        return 3 * (row / 3) + col / 3;
    }

    private static void solveRecursively(
            int emptyCells,
            char[][] board,
            int row,
            int col,
            BitSet[] elemsInRow,
            BitSet[] elemsInCol,
            BitSet[] elemsInSquare,
            boolean[] isFullySolved) {

        if (isFullySolved[0]) {
            return;
        }

        if (emptyCells == 0) {
            isFullySolved[0] = true;
            return;
        }

        if (row >= 9) {
            return;
        }

        final char ch = board[row][col];
        final int nextRow = row + ((col + 1) / 9);
        final int nextCol = (col + 1) % 9;

        if (ch == '.') {

            final int squareIndex = squareIndex(row, col);

            for (int candidateValue = 1;
                    candidateValue <= 9 && !isFullySolved[0];
                    ++candidateValue) {
                if (isClear(elemsInRow[row], candidateValue)
                        && isClear(elemsInCol[col], candidateValue)
                        && isClear(elemsInSquare[squareIndex], candidateValue)) {

                    board[row][col] = (char) ('0' + candidateValue);
                    elemsInRow[row].set(candidateValue);
                    elemsInCol[col].set(candidateValue);
                    elemsInSquare[squareIndex].set(candidateValue);

                    solveRecursively(
                            emptyCells - 1,
                            board,
                            nextRow,
                            nextCol,
                            elemsInRow,
                            elemsInCol,
                            elemsInSquare,
                            isFullySolved);

                    elemsInRow[row].clear(candidateValue);
                    elemsInCol[col].clear(candidateValue);
                    elemsInSquare[squareIndex].clear(candidateValue);
                    if (!isFullySolved[0]) {
                        board[row][col] = '.';
                    }
                }
            }
        } else {
            solveRecursively(
                    emptyCells,
                    board,
                    nextRow,
                    nextCol,
                    elemsInRow,
                    elemsInCol,
                    elemsInSquare,
                    isFullySolved);
        }
    }

    private static boolean isClear(BitSet set, int index) {
        return !set.get(index);
    }

    private static BitSet[] createForDigits(int size) {
        BitSet[] res = new BitSet[size];

        for (int i = 0; i < res.length; ++i) {
            res[i] = new BitSet(10);
        }
        return res;
    }
}
