package com.max.app17.leetcode.hard;

import java.util.Objects;

/*
1392. Longest Happy Prefix
https://leetcode.com/problems/longest-happy-prefix/
 */
public class LongestHappyPrefix {
    public static void main(String[] args) throws Exception {
        String str = "ababab";

        System.out.println(longestPrefix(str));

        System.out.println("LongestHappyPrefix done...");
    }

    public static String longestPrefix(String s) {
        int[] prefixTable = calculatePrefixTable(s.toCharArray());

        return s.substring(0, prefixTable[prefixTable.length - 1]);
    }

    /*
    Knuth-Morris-Pratt algorithm for prefix table calculation.
    time: O(N)
    space: O(N)
    */
    private static int[] calculatePrefixTable(char[] arr) {
        Objects.requireNonNull(arr, "");

        if (arr.length == 0) {
            return new int[] {};
        }

        int[] prefix = new int[arr.length];
        prefix[0] = 0;

        for (int i = 1; i < prefix.length; ++i) {
            int prevPrefix = prefix[i - 1];

            if (arr[prevPrefix] == arr[i]) {
                prefix[i] = prevPrefix + 1;
            } else {
                while (prevPrefix != 0) {
                    prevPrefix = prefix[prevPrefix - 1];

                    if (arr[prevPrefix] == arr[i]) {
                        prefix[i] = prevPrefix + 1;
                        break;
                    }
                }
            }
        }

        return prefix;
    }
}
