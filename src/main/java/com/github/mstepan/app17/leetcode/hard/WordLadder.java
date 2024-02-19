package com.max.app17.leetcode.hard;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Queue;
import java.util.Set;

/*
127. Word Ladder
https://leetcode.com/problems/word-ladder/
 */
public class WordLadder {

    public static void main(String[] args) throws Exception {

        // expected = 5
        int shortestPath =
                new WordLadder()
                        .ladderLength(
                                "hit", "cog", List.of("hot", "dot", "dog", "lot", "log", "cog"));

        System.out.printf("shortestPath: %d%n", shortestPath);

        System.out.println("WordLadder done...");
    }

    /*
    Build undirected graph. There is and edge between vertexes only if difference between words is 0 or 1.
    Use Breadth-first search to find the shortest path.

    N = wordList.size(), max = 5000
    K = average word length, max = 10
    time: O(N^2*K), max = 5000*5000*10 = 250_000_000 = 250M
    space: O(N)
    */
    public int ladderLength(String beginWord, String endWord, List<String> wordList) {
        Objects.requireNonNull(beginWord);
        Objects.requireNonNull(endWord);
        Objects.requireNonNull(wordList);

        if (!wordList.contains(endWord)) {
            return 0;
        }

        String[] arr = wordList.toArray(new String[] {});

        Graph g = new Graph();

        addVertexesToGraph(g, arr);
        addEdgesToGraph(g, arr);

        Queue<VertexAndPathLength> queue = new ArrayDeque<>();
        Set<String> alreadyInQueue = new HashSet<>();
        alreadyInQueue.add(beginWord);

        for (String word : arr) {
            if (distance(beginWord, word) < 2) {
                queue.add(new VertexAndPathLength(word, 2));
                alreadyInQueue.add(word);
            }
        }

        while (!queue.isEmpty()) {
            VertexAndPathLength cur = queue.poll();

            if (cur.ver.equals(endWord)) {
                return cur.length;
            }

            for (String adjVer : g.adjacent(cur.ver)) {
                if (!alreadyInQueue.contains(adjVer)) {
                    queue.add(new VertexAndPathLength(adjVer, cur.length + 1));
                    alreadyInQueue.add(adjVer);
                }
            }
        }

        return 0;
    }

    static class Graph {

        final Map<String, Set<String>> edges = new HashMap<>();

        void addVertex(String ver) {
            edges.put(ver, new HashSet<>());
        }

        void addEdge(String ver1, String ver2) {
            assert edges.containsKey(ver1);
            assert edges.containsKey(ver2);

            edges.get(ver1).add(ver2);
            edges.get(ver2).add(ver1);
        }

        Set<String> adjacent(String ver) {
            assert edges.containsKey(ver);
            return edges.get(ver);
        }
    }

    record VertexAndPathLength(String ver, int length) {}

    private void addVertexesToGraph(Graph g, String[] arr) {
        for (String word : arr) {
            g.addVertex(word);
        }
    }

    private void addEdgesToGraph(Graph g, String[] arr) {
        for (int i = 0; i < arr.length - 1; ++i) {
            String ver1 = arr[i];

            for (int j = i + 1; j < arr.length; ++j) {
                String ver2 = arr[j];
                if (distance(ver1, ver2) < 2) {
                    g.addEdge(ver1, ver2);
                }
            }
        }
    }

    private int distance(String str1, String str2) {
        assert str1.length() == str2.length();

        int diffCnt = 0;

        for (int i = 0; i < str1.length(); ++i) {
            diffCnt += str1.charAt(i) == str2.charAt(i) ? 0 : 1;
        }

        return diffCnt;
    }
}
