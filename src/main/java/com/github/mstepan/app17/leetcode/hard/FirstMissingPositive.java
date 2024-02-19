package com.max.app17.leetcode.hard;

/** 41. First Missing Positive https://leetcode.com/problems/first-missing-positive/ */
public class FirstMissingPositive {

    public static void main(String[] args) throws Exception {

        int[] arr = {-1, -5, 8, 10, 1, 3, 6, 4, 5, -18, 2};

        int smallestMissedValue = firstMissingPositive(arr);

        System.out.println(smallestMissedValue);

        System.out.println("FirstMissingPositive done...");
    }

    /** time: O(N) space: O(1) */
    public static int firstMissingPositive(int[] nums) {

        int positiveBoundary = partitionForPositiveAndNegative(nums);

        if (positiveBoundary == 0) {
            return 1;
        }

        // replace all negative values and zeros with just '1'
        for (int i = positiveBoundary; i < nums.length; ++i) {
            nums[i] = 1;
        }

        // handle only positive part
        for (int i = 0; i < positiveBoundary; ++i) {
            int index = Math.abs(nums[i]) - 1;

            if (index >= 0 && index < nums.length) {
                if (nums[index] > 0) {
                    nums[index] = -nums[index];
                }
            }
        }

        for (int i = 0; i < nums.length; ++i) {
            if (nums[i] > 0) {
                return i + 1;
            }
        }

        return nums.length + 1;
    }

    private static int partitionForPositiveAndNegative(int[] arr) {
        int greater = -1;

        for (int i = 0; i < arr.length; ++i) {
            if (arr[i] > 0) {
                swap(arr, greater + 1, i);
                ++greater;
            }
        }

        return greater + 1;
    }

    private static void swap(int[] arr, int from, int to) {
        int temp = arr[from];
        arr[from] = arr[to];
        arr[to] = temp;
    }
}
