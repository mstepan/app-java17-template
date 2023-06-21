package com.max.app17.graph;

/**
 * Store undirected graph with 'vertexes count <= 62' in compact form, using long as a single row bitmask for adjacency matrix.
 */
public final class CompactGraph {


    // taking into account that long is 64 bits and 1 bit is used as a sign, we can use up to 63 bits,
    // so possible vertexes values should in range [0...62]
    private static final int MAX_VERTEX_VALUE = Long.SIZE - 2;

    private final int vertexesCount;
    private final long[] adjMatrix;

    public CompactGraph(int vertexesCount) {
        checkArgument(vertexesCount > 0 && vertexesCount <= MAX_VERTEX_VALUE,
                      String.format("Vertexes count is incorrect: %d, but expected [1; %d]", vertexesCount, MAX_VERTEX_VALUE));
        this.vertexesCount = vertexesCount;
        this.adjMatrix = new long[vertexesCount];
    }

    public void addEdge(int src, int dest) {
        checkVertexRange(src, "src");
        checkVertexRange(dest, "dest");

        adjMatrix[src] |= (1L << dest);
        adjMatrix[dest] |= (1L << src);
    }

    public long adjBitmask(int ver) {
        checkVertexRange(ver, "ver");
        return adjMatrix[ver];
    }

    public int vertexesCount() {
        return vertexesCount;
    }

    private void checkVertexRange(int curVer, String paramName) {
        checkArgument(curVer >= 0 && curVer < vertexesCount,
                      String.format("Incorrect vertex value,  %s = %d, expected in range [0;%d]",
                                    paramName, curVer, vertexesCount - 1));
    }

    private static void checkArgument(boolean predicate, String errorMsg) {
        if (!predicate) {
            throw new IllegalArgumentException(errorMsg);
        }
    }

}
