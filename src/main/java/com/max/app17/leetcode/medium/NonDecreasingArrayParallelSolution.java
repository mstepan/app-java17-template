package com.max.app17.leetcode.medium;


import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

/**
 * 665. Non-decreasing Array.
 *
 * https://leetcode.com/problems/non-decreasing-array/
 *
 * Solution that uses RecursiveTask and ForkJoin pool to speed up parallel solution.
 */
public class NonDecreasingArrayParallelSolution {

    public static void main(String[] args) throws Exception {

        int[] arr = {
                0, 1, 2, 3, 4, 5, 6,
                7, 8, 9, 10, 11, 12, 13,
                14, 15, 16, 17, 18, 19, 20, 21, 22, 23,
                24, 25, 26
        };

        boolean res = new NonDecreasingArrayParallelSolution().checkPossibility(arr);

        System.out.println(res);

        System.out.println("NonDecreasingArrayParallelSolution done...");
    }

    public boolean checkPossibility(int[] arr) {
        ForkJoinPool pool = new ForkJoinPool();

        try {
            PartialResult result =
                    pool.submit(new CheckPossibilityRecTask(arr, 0, arr.length - 1)).get();
            return result.possible();
        } catch (ExecutionException ex) {
            return false;
        } catch (InterruptedException interEx) {
            Thread.currentThread().interrupt();
            return true;
        } finally {
            pool.shutdownNow();
        }
    }

    private static final class CheckPossibilityRecTask extends RecursiveTask<PartialResult> {

        private final int[] arr;
        private final int from;
        private final int to;

        public CheckPossibilityRecTask(int[] arr, int from, int to) {
            this.arr = arr;
            this.from = from;
            this.to = to;
        }

        @Override
        protected PartialResult compute() {

            System.out.printf("thread-id: %d => range: [%d...%d] %n", Thread.currentThread().getId(), from, to);

            int elemsCnt = to - from + 1;

            if (elemsCnt <= 5) {
                return checkPossibilitySequential(arr, from, to);
            }

            int mid = from + (to - from) / 2;

            CheckPossibilityRecTask leftTask = new CheckPossibilityRecTask(arr, from, mid);
            leftTask.fork();

            CheckPossibilityRecTask rightTask = new CheckPossibilityRecTask(arr, mid + 1, to);

            PartialResult rightRes = rightTask.compute();

           PartialResult leftRes = leftTask.join();

            if (rightRes.possible() && leftRes.possible()) {

                int totalEditsCnt = leftRes.editsUsed() + rightRes.editsUsed();

                if (totalEditsCnt >= 2) {
                    return new PartialResult(false, totalEditsCnt);
                }

                // totalEditsCnt == 0 or totalEditsCnt == 1 below
                if( arr[mid] <= arr[mid+1]){
                    return new PartialResult(true, totalEditsCnt);
                }

                if (totalEditsCnt == 0) {
                    // prev  | cur | next
                    // mid-1 | mid | mid+1
                    int prev = arr[mid - 1];

                    if (prev <= arr[mid + 1]) {
                        arr[mid] = arr[mid + 1];
                    } else {
                        arr[mid + 1] = arr[mid];
                    }

                    return new PartialResult(true, 1);
                }
                // 'totalEditsCnt = 1' and we need 1 more, so return 'false'
                else {
                    return new PartialResult(false, totalEditsCnt);
                }
            }

            return new PartialResult(false, rightRes.editsUsed() + rightRes.editsUsed);
        }

        /** time: O(N) space: O(1) */
        private static PartialResult checkPossibilitySequential(int[] arr, int from, int to) {
            Objects.requireNonNull(arr, "null 'arr' detected");

            int editsLeft = 1;

            for (int i = from + 1; i <= to; ++i) {

                if (arr[i - 1] > arr[i]) {
                    if (editsLeft == 0) {
                        return new PartialResult(false, 1 - editsLeft);
                    }

                    int prev = (i - 2 >= 0) ? arr[i - 2] : Integer.MIN_VALUE;

                    if (prev <= arr[i]) {
                        arr[i - 1] = arr[i];
                    } else {
                        arr[i] = arr[i - 1];
                    }
                    --editsLeft;
                }
            }

            return new PartialResult(true, 1 - editsLeft);
        }

        @Override
        public String toString() {
            return "[%d...%d]".formatted(from, to);
        }
    }

    record PartialResult(boolean possible, int editsUsed) {}
}
