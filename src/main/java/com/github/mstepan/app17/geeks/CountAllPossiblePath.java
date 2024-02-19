package com.github.mstepan.app17.geeks;

import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Queue;
import java.util.Set;

/**
 * Count all Possible Path
 *
 * <p>https://www.geeksforgeeks.org/problems/castle-run3644/1?utm_source=geeksforgeeks&utm_medium=newui_home&utm_campaign=potd
 */
public class CountAllPossiblePath {

    public static void main(String[] args) throws Exception {

        // res = 0
        int[][] adjMatrix =
                new int[][] {
                    {0, 1, 1, 0},
                    {1, 0, 1, 1},
                    {1, 1, 0, 0},
                    {0, 1, 0, 0}
                };

        // res = 1
        //        int[][] adjMatrix =
        //                new int[][] {
        //                    {0, 1, 1, 1, 1},
        //                    {1, 0, 0, 1, 0},
        //                    {1, 0, 0, 1, 0},
        //                    {1, 1, 1, 0, 1},
        //                    {1, 0, 0, 1, 0},
        //                };

        int res = isPossible(adjMatrix);

        System.out.printf("isPossible: %d%n", res);

        System.out.println("Main done...");
    }

    /**
     * According to wikipedia. https://en.wikipedia.org/wiki/Eulerian_path
     *
     * <p>An undirected graph has an Eulerian cycle if and only if every vertex has even degree, and
     * all of its vertices with nonzero degree belong to a single connected component.
     */
    public static int isPossible(int[][] adjMatrix) {
        return (isAllVertexesHaveEvenDegree(adjMatrix) && isFullyConnected(adjMatrix)) ? 1 : 0;
    }

    /**
     * time: O(N*N)
     *
     * <p>space:O(1)
     */
    private static boolean isAllVertexesHaveEvenDegree(int[][] adjMatrix) {
        assert adjMatrix != null;

        for (int[] row : adjMatrix) {
            assert row != null;

            int onesCount = 0;

            for (int col = 0; col < row.length; ++col) {
                assert row[col] == 0 || row[col] == 1;

                onesCount += row[col];
            }

            if ((onesCount % 2) != 0) {
                return false;
            }
        }

        return true;
    }

    /**
     * Do BFS to find vertexes count that belongs to current connected component. time: O(N*N)
     *
     * <p>space:O(N)
     */
    private static boolean isFullyConnected(int[][] adjMatrix) {

        final int startVertex = 0;

        Queue<Integer> queue = new ArrayDeque<>();
        queue.add(startVertex);

        Set<Integer> marked = new HashSet<>();
        marked.add(startVertex);

        while (!queue.isEmpty()) {
            int curVer = queue.poll();

            for (int adjVer = 0; adjVer < adjMatrix.length; ++adjVer) {
                if (adjMatrix[curVer][adjVer] == 1 && !marked.contains(adjVer)) {
                    queue.add(adjVer);
                    marked.add(adjVer);
                }
            }
        }

        return marked.size() == adjMatrix.length;
    }
}
