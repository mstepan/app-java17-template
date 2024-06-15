package com.github.mstepan.app17.leetcode.medium;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 1722. Minimize Hamming Distance After Swap Operations
 *
 * <p>https://leetcode.com/problems/minimize-hamming-distance-after-swap-operations/description/
 */
public class MinimizeHammingDistanceAfterSwapOperations {

    public int minimumHammingDistance(int[] source, int[] target, int[][] allowedSwaps) {
        Objects.requireNonNull(source);
        Objects.requireNonNull(target);
        Objects.requireNonNull(allowedSwaps);
        assert source.length == target.length;

        DisjointSet set = new DisjointSet();

        for (int i = 0; i < source.length; ++i) {
            set.add(i, source[i]);
        }

        for (int[] singleSwap : allowedSwaps) {
            set.join(singleSwap[0], singleSwap[1]);
        }

        int distance = 0;

        for (int targetIdx = 0; targetIdx < target.length; ++targetIdx) {
            int targetValue = target[targetIdx];

            Node representative = set.findRepresentative(targetIdx);

            if (representative.hasNonZeroCount(targetValue)) {
                representative.decreaseCount(targetValue);
            } else {
                ++distance;
            }
        }

        return distance;
    }

    static class DisjointSet {

        final Map<Integer, Node> indexToNode = new HashMap<>();

        public void add(int index, int value) {
            assert !indexToNode.containsKey(index);
            indexToNode.put(index, new Node(index, value));
        }

        public void join(int idx1, int idx2) {
            Node bigger = findRepresentative(idx1);
            assert bigger != null;

            Node smaller = findRepresentative(idx2);
            assert smaller != null;

            if (bigger == smaller) {
                return;
            }

            // 'parent' should be node with bigger subtree
            if (smaller.size() > bigger.size()) {
                Node temp = bigger;
                bigger = smaller;
                smaller = temp;
            }

            bigger.addChild(smaller);
        }

        public Node findRepresentative(int idx) {

            Node mainNode = indexToNode.get(idx);

            if (mainNode == null) {
                return null;
            }

            while (mainNode.parent != null) {
                mainNode = mainNode.parent;
            }

            return mainNode;
        }
    }

    static class Node {
        final int index;

        int size;

        Node parent;

        final Map<Integer, Integer> freq = new HashMap<>();

        Node(int index, int value) {
            this.index = index;
            freq.put(value, 1);
            size = 1;
        }

        void addChild(Node child) {
            size += child.size;
            child.parent = this;

            // merge two 'freq' HashMaps into parent
            for (Map.Entry<Integer, Integer> childEntry : child.freq.entrySet()) {

                final int childKey = childEntry.getKey();
                final int childCnt = childEntry.getValue();

                freq.compute(
                        childKey,
                        (key, cnt) -> {
                            if (cnt == null) {
                                return childCnt;
                            }

                            cnt += childCnt;
                            return cnt;
                        });
            }
        }

        @Override
        public String toString() {
            return "index: " + index + ", freq = " + freq;
        }

        public boolean hasNonZeroCount(int value) {
            Integer count = freq.get(value);
            return count != null && count > 0;
        }

        public void decreaseCount(int value) {
            freq.compute(
                    value,
                    (key, cnt) -> {
                        assert cnt != null;
                        return cnt - 1;
                    });
        }

        public int size() {
            return size;
        }
    }
}
