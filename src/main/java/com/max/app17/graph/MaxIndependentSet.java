package com.max.app17.graph;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/**
 * JVM parameter to print memory when JVM process exited:
 * -XX:+UnlockDiagnosticVMOptions -XX:NativeMemoryTracking=summary -XX:+PrintNMTStatistics
 */

public class MaxIndependentSet {

    public static void main(String[] args) {

        CompactGraph graph = new CompactGraph(7);

        graph.addEdge(0, 1);
        graph.addEdge(0, 2);
        graph.addEdge(1, 4);
        graph.addEdge(1, 3);
        graph.addEdge(2, 3);

        graph.addEdge(3, 4);
        graph.addEdge(3, 5);

        graph.addEdge(4, 6);
        graph.addEdge(5, 6);

        System.out.println(maxIndependentSet(graph));

        System.out.printf("MaxIndependentSet done... java version: %s%n", System.getProperty("java.version"));
    }

    /**
     * Find max independent set for compact graph (vertexes count < 64).
     * Max independent set is NP-complete problem, so recursive backtracking will be used.
     */
    public static List<Integer> maxIndependentSet(CompactGraph g) {

        final List<Integer> maxSolution = new ArrayList<>(g.vertexesCount());
        final long allUsedMask = (1L << g.vertexesCount()) - 1L;

        maxIndependentSetRec(g, 0L, new ArrayDeque<>(g.vertexesCount()), maxSolution, allUsedMask);

        return maxSolution;
    }

    private static void maxIndependentSetRec(CompactGraph g, long used, Deque<Integer> curSol,
                                             List<Integer> maxSolution, long allUsedMask) {
        if (used == allUsedMask && curSol.size() > maxSolution.size()) {
            maxSolution.clear();
            maxSolution.addAll(curSol);
            return;
        }

        for (int i = 0; i < g.vertexesCount(); ++i) {
            if (isZeroBit(used, i)) {
                curSol.push(i);
                maxIndependentSetRec(g, markCurVerAndNeighbour(g, used, i), curSol, maxSolution, allUsedMask);
                curSol.pop();
            }
        }
    }

    private static boolean isZeroBit(long used, int i) {
        return (used & (1L << i)) == 0L;
    }

    private static long markCurVerAndNeighbour(CompactGraph g, long used, int curVer) {
        return used | (1L << curVer) | g.adjBitmask(curVer);
    }

}

