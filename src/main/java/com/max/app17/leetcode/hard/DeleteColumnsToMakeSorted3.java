package com.max.app17.leetcode.hard;

import com.max.app17.Main;

import java.util.Objects;

/**
 * 960. Delete Columns to Make Sorted III
 *
 * https://leetcode.com/problems/delete-columns-to-make-sorted-iii/
 */
public class DeleteColumnsToMakeSorted3 {

    public static void main(String[] args) throws Exception {

        String[] strs = {"babca", "bbazb"};

        int minDeletionCount = new DeleteColumnsToMakeSorted3().minDeletionSize(strs);

        System.out.printf("minDeletionCount: %d%n", minDeletionCount);

        System.out.println("DeleteColumnsToMakeSorted3 done...");
    }


    /**
     * Find longest increased subsequence among all string at the same time.
     *
     * <p>N = 'strs' length
     *
     * <p>M = single strs[i] length
     *
     * <p>time: O(N * N * M) = O(N^2*M), when N == M, O(N^3)
     * if N = 100 and M = 100, 100^3 = 10^6 = 1M
     *
     * <p>space: O(M)
     */
    public int minDeletionSize(String[] strs) {
        Objects.requireNonNull(strs, "null 'strs' detected");

        if(strs.length == 0){
            return 0;
        }

        checkAllNotNullAndHaveSameLength(strs);

        final int cols = strs[0].length();

        int[] lis = new int[cols];
        lis[0] = 1;

        int maxLis = 1;

        for (int i = 1; i < cols; ++i) {

            int lisCur = 1;

            for (int j = i - 1; j >= 0; --j) {
                if (isCharSmallerForAllStrings(strs, j, i)) {
                    lisCur = Math.max(lisCur, 1 + lis[j]);
                }
            }

            lis[i] = lisCur;
            maxLis = Math.max(maxLis, lisCur);
        }
        return cols - maxLis;
    }

    private void checkAllNotNullAndHaveSameLength(String[] strs) {

        int expectedLength = -1;

        for(String curStr : strs){
            if( curStr == null ){
                throw new IllegalArgumentException("null string detected inside 'strs' array");
            }

            if( expectedLength < 0 ){
                expectedLength = curStr.length();
            }

            if( curStr.length() != expectedLength){
                throw new IllegalArgumentException("not al strings inside 'strs' array have same length");
            }
        }
    }

    private boolean isCharSmallerForAllStrings(String[] strs, int left, int right) {
        assert strs != null;
        assert left <= right;

        for (String curStr : strs) {
            assert left >= 0 && left < curStr.length();
            assert right >= 0 && right < curStr.length();

            if (curStr.charAt(left) > curStr.charAt(right)) {
                return false;
            }
        }

        return true;
    }

}
