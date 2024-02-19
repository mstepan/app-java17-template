package com.max.app17.leetcode.hard;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/*
710. Random Pick with Blacklist
https://leetcode.com/problems/random-pick-with-blacklist/
*/
public class RandomPickWithBlacklist {

    public static void main(String[] args) throws Exception {

        final int n = 1_000_000_000;
        final int[] blacklist = {640_145_908};
        RandomPickWithBlacklist.Solution sol =
                new RandomPickWithBlacklist().new Solution(n, blacklist);

        for (int it = 0; it < 10; ++it) {
            System.out.printf("rand value: %d%n", sol.pick());
        }

        System.out.println("RandomPickWithBlacklist done...");
    }

    class Solution {

        private static final Random RAND = ThreadLocalRandom.current();

        private final Solution.Node root;

        private final int[] blacklist;

        /** time: O(lgN) space: O(N) */
        public Solution(int n, int[] blacklist) {
            this.blacklist = blacklist;
            this.root = Node.createRecursively(0, n - 1, blacklist, 0, blacklist.length - 1);
        }

        /** time: O(lgN) */
        public int pick() {

            Solution.Node cur = root;

            while (true) {
                // If we found leaf element just pick random value from leaf
                if (cur.isLeaf()) {
                    return cur.randValue(blacklist);
                }

                // if left is NULL, go right
                if (cur.left == null) {
                    cur = cur.right;
                }

                // if right is NULL, go left
                if (cur.right == null) {
                    cur = cur.left;
                }

                // both left and right are NOT NULL, so choose which one to follow using random
                // boolean value
                cur = RAND.nextBoolean() ? cur.left : cur.right;
            }
        }

        static class Node {
            int lo;
            int hi;

            int[] availableValues;

            int blFrom;
            int blTo;

            Node left;
            Node right;

            static Node createRecursively(int lo, int hi, int[] blacklist, int blFrom, int bltTo) {

                Arrays.sort(blacklist);

                int availableCount = availableCount(lo, hi, blFrom, bltTo);

                /*
                case-1: If we have only 10 GOOD values or less we can just generate all good values in a range
                and choose at random.
                 */
                if (availableCount <= 10) {
                    Solution.Node leaf =
                            new Solution.Node(
                                    lo,
                                    hi,
                                    generateAvailable(lo, hi, blacklist, blFrom, bltTo),
                                    blFrom,
                                    bltTo);
                    return leaf;
                }

                int badSlotsCount = bltTo - blFrom + 1;

                /*
                case-2: we heave at least 50% of good values in a range, so we can just generate at random few values till
                we will find a good one.
                The probability of bad value at N-th iteration is: 1/(2)^n
                So at 10th iteration we have probability of only picking bad values = 1/(2^10) = 9.765625E-4
                */
                if (availableCount > badSlotsCount) {
                    return new Solution.Node(lo, hi, new int[] {}, blFrom, bltTo);
                }

                int mid = lo + (hi - lo) / 2;

                int blIndex = binarySearchSmallestOrEqual(mid, blacklist, blFrom, bltTo);

                /*
                case-3: generic case, we just split the range into 2 sub-ranges using middle element as delimiter and
                construct left and right subtrees recursively.
                */
                Solution.Node left = createRecursively(lo, mid, blacklist, blFrom, blIndex);
                Solution.Node right = createRecursively(mid + 1, hi, blacklist, blIndex + 1, bltTo);

                return new Solution.Node(left, right);
            }

            private static int binarySearchSmallestOrEqual(
                    int valueToSearch, int[] arr, int from, int to) {

                int lo = from;
                int hi = to;

                int foundIdx = -1;

                while (lo <= hi) {
                    int mid = lo + (hi - lo) / 2;

                    if (arr[mid] <= valueToSearch) {
                        foundIdx = mid;
                        lo = mid + 1;
                    } else {
                        hi = mid - 1;
                    }
                }

                return foundIdx;
            }

            private static int[] generateAvailable(
                    int lo, int hi, int[] blacklist, int blFrom, int blTo) {

                int blIndex = blFrom;

                int[] elems = new int[availableCount(lo, hi, blFrom, blTo)];
                int elemsIdx = 0;

                for (int cur = lo; cur <= hi; ++cur) {
                    if (blIndex <= blTo && blacklist[blIndex] == cur) {
                        ++blIndex;
                    } else {
                        elems[elemsIdx] = cur;
                        ++elemsIdx;
                    }
                }

                return elems;
            }

            private static int availableCount(int lo, int hi, int blFrom, int blTo) {
                //                assert lo <= hi : "lo > hi";
                //                assert blFrom <= blTo : "blFrom > blTo";

                return (hi - lo + 1) - (blTo - blFrom + 1);
            }

            private Node(int lo, int hi, int[] availableValues, int blFrom, int blTo) {
                this.lo = lo;
                this.hi = hi;
                this.availableValues = availableValues;
                this.blFrom = blFrom;
                this.blTo = blTo;
            }

            private Node(Node left, Node right) {
                this.left = left;
                this.right = right;
            }

            public boolean isLeaf() {
                return availableValues != null;
            }

            public int randValue(int[] blacklist) {
                if (availableValues.length > 0) {
                    int idx = RAND.nextInt(availableValues.length);
                    return availableValues[idx];
                }

                while (true) {
                    int rand = lo + RAND.nextInt(hi - lo + 1);

                    int badSlotIdx = binarySearchSmallestOrEqual(rand, blacklist, blFrom, blTo);

                    if (badSlotIdx == -1 || blacklist[badSlotIdx] != rand) {
                        return rand;
                    }
                }
            }
        }
    }
}
