package com.github.mstepan.app17.leetcode.medium;

import java.util.Objects;
import java.util.PriorityQueue;

/**
 * 786. K-th Smallest Prime Fraction
 *
 * <p>https://leetcode.com/problems/k-th-smallest-prime-fraction/
 */
public class KthSmallestPrimeFraction {

    public static void main(String[] args) {
        int[] arr = {1, 2, 3, 5};
        int k = 3;

        //        int[] arr = {1, 7};
        //        int k = 1;

        int[] res = new KthSmallestPrimeFraction().kthSmallestPrimeFraction(arr, k);

        assert res != null && res.length == 2;

        System.out.printf("res: %d/%d%n", res[0], res[1]);

        System.out.println("KthSmallestPrimeFraction done...");
    }

    /**
     * time: O(K*lgN)
     *
     * <p>space: O(N)
     */
    public int[] kthSmallestPrimeFraction(int[] arr, int k) {
        Objects.requireNonNull(arr);

        int kUpperBoundary = (arr.length * arr.length - 1) / 2;
        if (k <= 0 || k > kUpperBoundary) {
            throw new IllegalArgumentException(
                    "Invalid k boundary, should be in range [%d; %d]".formatted(1, kUpperBoundary));
        }

        PriorityQueue<FractionSequence> minHeap = new PriorityQueue<>();

        for (int numIdx = 0; numIdx < arr.length - 1; ++numIdx) {
            minHeap.add(new FractionSequence(numIdx, arr.length - 1, arr));
        }

        for (int it = 0; it < k - 1; ++it) {
            FractionSequence curSeq = minHeap.poll();

            assert curSeq != null;

            if (curSeq.moveNext()) {
                minHeap.add(curSeq);
            }
        }

        assert !minHeap.isEmpty();
        Fraction result = minHeap.poll().current;

        return new int[] {result.num, result.denom};
    }

    private static final class FractionSequence implements Comparable<FractionSequence> {

        final int numIdx;
        int denomIdx;

        Fraction current;

        final int[] arr;

        FractionSequence(int numIdx, int denomIdx, int[] arr) {
            assert numIdx != denomIdx;

            this.numIdx = numIdx;
            this.denomIdx = denomIdx;
            this.arr = arr;
            this.current = new Fraction(arr[numIdx], arr[denomIdx]);
        }

        boolean moveNext() {
            --denomIdx;

            if (numIdx == denomIdx) {
                return false;
            }

            current = new Fraction(arr[numIdx], arr[denomIdx]);

            return true;
        }

        @Override
        public int compareTo(FractionSequence other) {
            return current.compareTo(other.current);
        }

        @Override
        public String toString() {
            return "[%s]".formatted(current.toString());
        }
    }

    private static final class Fraction implements Comparable<Fraction> {
        final int num;
        final int denom;

        Fraction(int num, int denom) {
            this.num = num;
            this.denom = denom;
        }

        @Override
        public int compareTo(Fraction other) {
            int curNum = num * other.denom;
            int otherNum = other.num * denom;

            if (curNum == otherNum) {
                return 0;
            }

            return curNum > otherNum ? 1 : -1;
        }

        @Override
        public String toString() {
            return "%d/%d".formatted(num, denom);
        }
    }
}
