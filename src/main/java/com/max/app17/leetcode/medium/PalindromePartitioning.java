package com.max.app17.leetcode.medium;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/*

131. Palindrome Partitioning


https://leetcode.com/problems/palindrome-partitioning/
 */
public class PalindromePartitioning {

    public static void main(String[] args) throws Exception {

        String str = "aab";

        List<List<String>> allPartitions = new PalindromePartitioning().partition(str);

        for (List<String> partition : allPartitions) {
            System.out.println(partition);
        }

        System.out.println("PalindromePartitioning done...");
    }

    /**
     * time: O(2^N)
     *
     * <p>space: O(2^N)
     *
     * <p>N <= 16, N^16 = 65_536
     */
    public List<List<String>> partition(String str) {
        Objects.requireNonNull(str);

        char[] arr = str.toCharArray();

        List<StringPartition>[] allPartitions = new List[arr.length];

        for (int to = 0; to < arr.length; ++to) {

            List<StringPartition> curRes = new ArrayList<>();

            for (int from = to; from >= 0; --from) {
                if (isPalindrome(arr, from, to)) {

                    String rightSub = str.substring(from, to + 1);

                    if (from == 0) {
                        curRes.add(new StringPartition(List.of(rightSub)));
                    } else {
                        curRes.addAll(combineAll(allPartitions[from - 1], rightSub));
                    }
                }
            }

            allPartitions[to] = curRes;
        }

        return allPartitions[allPartitions.length - 1].stream()
                .map(StringPartition::parts)
                .toList();
    }

    private List<StringPartition> combineAll(
            List<StringPartition> leftPartitions, String rightSub) {

        if (leftPartitions == null) {
            return null;
        }

        List<StringPartition> combinedRes = new ArrayList<>();

        for (StringPartition singlePartition : leftPartitions) {
            List<String> newPartition = new ArrayList<>(singlePartition.parts);
            newPartition.add(rightSub);
            combinedRes.add(new StringPartition(newPartition));
        }

        return combinedRes;
    }

    private boolean isPalindrome(char[] arr, int from, int to) {

        int left = from;
        int right = to;

        while (left < right) {
            if (arr[left] != arr[right]) {
                return false;
            }
            ++left;
            --right;
        }

        return true;
    }

    // Examples: ["a", "a", "b"] or ["aa", 'b']
    record StringPartition(List<String> parts) {}
}
