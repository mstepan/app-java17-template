package com.max.app17.leetcode;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/** <a href="https://leetcode.com/problems/contain-virus/">749. Contain Virus</a> */
public class ContainVirus {

    public static void main(String[] args) {

        // expected = 10;
        //        int[][] board = {
        //            {0, 1, 0, 0, 0, 0, 0, 1},
        //            {0, 1, 0, 0, 0, 0, 0, 1},
        //            {0, 0, 0, 0, 0, 0, 0, 1},
        //            {0, 0, 0, 0, 0, 0, 0, 0}
        //        };

        // expected = 4
        //        int[][] board = {{1, 1, 1}, {1, 0, 1}, {1, 1, 1}};

        // expected = 13
        int[][] board = {
            {1, 1, 1, 0, 0, 0, 0, 0, 0}, {1, 0, 1, 0, 1, 1, 1, 1, 1}, {1, 1, 1, 0, 0, 0, 0, 0, 0}
        };

        int wallsCount = new ContainVirus().containVirus(board);

        System.out.printf("wallsCount: %d%n", wallsCount);

        System.out.println("ContainVirus main done...");
    }

    public int containVirus(int[][] board) {
        BoardState boardState = BoardState.fromArray(board);
        return calculateWallsCount(board, boardState, new HashMap<>());
    }

    public int calculateWallsCount(
            int[][] board, BoardState state, Map<BoardState, Integer> cache) {
        if (cache.containsKey(state)) {
            return cache.get(state);
        }

        if (state.isEmpty()) {
            return 0;
        }

        int minWallsCount = Integer.MAX_VALUE;

        final int lastRow = board.length - 1;
        final int lastCol = board[0].length - 1;

        for (InfectedRegion curRegion : state.regions()) {

            int curWallsCount = curRegion.countWallsRequired(board);
            int curLeftRegionsCount =
                    calculateWallsCount(board, state.nextState(curRegion, lastRow, lastCol), cache);

            int wallsForCurrentRegion = curWallsCount + curLeftRegionsCount;
            minWallsCount = Math.min(minWallsCount, wallsForCurrentRegion);
        }

        cache.put(state, minWallsCount);
        return minWallsCount;
    }

    private record BoardState(Set<InfectedRegion> regions) {
        static BoardState fromArray(int[][] board) {

            Set<Cell> visited = new HashSet<>();

            Set<InfectedRegion> allRegions = new HashSet<>();

            for (int row = 0; row < board.length; ++row) {
                for (int col = 0; col < board[row].length; ++col) {
                    if (board[row][col] == 1) {
                        Cell curCell = new Cell(row, col);
                        if (!visited.contains(curCell)) {
                            InfectedRegion region = buildRegion(board, curCell, visited);
                            allRegions.add(region);
                        }
                    }
                }
            }

            return new BoardState(allRegions);
        }

        private static InfectedRegion buildRegion(
                int[][] board, Cell startCell, Set<Cell> visited) {

            Set<Cell> curRegionCells = new HashSet<>();
            curRegionCells.add(startCell);
            visited.add(startCell);

            Deque<Cell> queue = new ArrayDeque<>();
            queue.add(startCell);

            final int lastRow = board.length - 1;
            final int lastCol = board[0].length - 1;

            while (!queue.isEmpty()) {
                Cell cur = queue.poll();

                for (Cell neighbour : cur.neighbourCells()) {
                    if (neighbour.isInRange(0, 0, lastRow, lastCol)
                            && board[neighbour.row][neighbour.col] == 1
                            && !visited.contains(neighbour)) {

                        curRegionCells.add(neighbour);
                        visited.add(neighbour);

                        queue.add(neighbour);
                    }
                }
            }

            return new InfectedRegion(curRegionCells);
        }

        public BoardState nextState(InfectedRegion regionToSkip, int lastRow, int lastCol) {
            Set<InfectedRegion> newRegions = new HashSet<>();

            for (InfectedRegion curRegion : regions) {
                if (!curRegion.equals(regionToSkip)) {
                    newRegions.add(curRegion.spreadInfection(lastRow, lastCol));
                }
            }

            Set<InfectedRegion> newMergedRegions = mergeRegionsThatIntersects(newRegions);

            return new BoardState(newMergedRegions);
        }

        private Set<InfectedRegion> mergeRegionsThatIntersects(Set<InfectedRegion> newRegions) {
            // TODO:


            return newRegions;
        }

        public boolean isEmpty() {
            return regions.isEmpty();
        }
    }

    private record InfectedRegion(Set<Cell> infectedCells) {
        InfectedRegion {
            if (infectedCells == null) {
                throw new IllegalArgumentException("null 'infectedCells' value detected");
            }
        }

        @SuppressWarnings("unused")
        boolean intersectsWith(InfectedRegion other) {
            for (Cell curCell : infectedCells) {
                for (Cell otherCell : other.infectedCells) {
                    if (curCell.equals(otherCell)) {
                        return true;
                    }
                }
            }

            return false;
        }

        InfectedRegion spreadInfection(int lastRow, int lastCol) {
            Set<Cell> newArea = new HashSet<>();

            for (Cell originalCell : infectedCells) {
                newArea.add(originalCell);

                for (Cell neighbour : originalCell.neighbourCells()) {
                    if (neighbour.isInRange(0, 0, lastRow, lastCol)) {
                        newArea.add(neighbour);
                    }
                }
            }

            return new InfectedRegion(newArea);
        }

        public int countWallsRequired(int[][] board) {

            int wallsCount = 0;
            for (Cell cell : infectedCells) {
                for (Cell neighbour : cell.neighbourCells()) {
                    if (neighbour.isInRange(0, 0, board.length - 1, board[0].length - 1)
                            && board[neighbour.row][neighbour.col] == 0
                            && !infectedCells.contains(neighbour)) {
                        ++wallsCount;
                    }
                }
            }

            return wallsCount;
        }
    }

    private record Cell(int row, int col) {
        Cell {
            if (row < 0 || row > 50) {
                throw new IllegalArgumentException("invalid 'row' " + row);
            }
            if (col < 0 || col > 50) {
                throw new IllegalArgumentException("invalid 'col' " + col);
            }
        }

        List<Cell> neighbourCells() {

            List<Cell> neighbours = new ArrayList<>();

            // bottom
            neighbours.add(new Cell(row + 1, col));

            // right
            neighbours.add(new Cell(row, col + 1));

            // top
            if (row != 0) {
                neighbours.add(new Cell(row - 1, col));
            }

            // left
            if (col != 0) {
                neighbours.add(new Cell(row, col - 1));
            }

            return neighbours;
        }

        public boolean isInRange(int fromRow, int fromCol, int toRow, int toCol) {
            return row >= fromRow && row <= toRow && col >= fromCol && col <= toCol;
        }

        @Override
        public String toString() {
            return "(%d; %d)".formatted(row, col);
        }
    }
}
