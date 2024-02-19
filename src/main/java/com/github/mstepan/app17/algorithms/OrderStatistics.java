package com.github.mstepan.app17.algorithms;

import static com.github.mstepan.app17.algorithms.ArrayUtils.swap;
import static com.github.mstepan.app17.utils.Preconditions.checkArguments;

import java.util.concurrent.ThreadLocalRandom;

public final class OrderStatistics {

    /**
     * Select n-th order statistic element from array. The array content will change.
     *
     * <p>time: O(n)
     *
     * <p>space: O(1)
     *
     * @param arr - initial array
     * @param rank - the rank of element that we need to find
     * @return n-th order statistic from array.
     */
    public static int selectNthElement(int[] arr, int rank) {
        checkArguments(rank >= 0 && rank < arr.length, "'rank' is out of array bounds");

        int from = 0;
        int to = arr.length - 1;

        while (from < to) {
            int curRank = randomPartition(arr, from, to);

            if (curRank == rank) {
                return arr[curRank];
            }

            if (curRank > rank) {
                to = curRank - 1;
            } else {
                from = curRank + 1;
            }
        }

        return arr[from];
    }

    /**
     * Partition 'arr' into two parts using quicksort-like single path algorithm.
     *
     * @param arr array to be partition. The content of an array will change.
     * @return index of an element that is placed in a final (sorted order) position.
     */
    static int randomPartition(int[] arr, int from, int to) {
        assert arr != null : "'arr' can't be null";
        assert from <= to : "from > to (from = %d, to = %d)".formatted(from, to);
        assert from >= 0;
        assert to < arr.length;

        if (from == to) {
            return from;
        }

        int idx = from + ThreadLocalRandom.current().nextInt(to - from + 1);

        swap(arr, idx, to);
        int pivot = arr[to];
        int less = from - 1;

        for (int i = from; i < to; ++i) {
            if (arr[i] <= pivot) {
                swap(arr, less + 1, i);
                ++less;
            }
        }

        swap(arr, less + 1, to);
        ++less;
        return less;
    }
}
