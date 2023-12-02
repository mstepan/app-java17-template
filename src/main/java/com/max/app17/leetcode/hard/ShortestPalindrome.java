package com.max.app17.leetcode.hard;

import java.util.Objects;

/*
214. Shortest Palindrome
https://leetcode.com/problems/shortest-palindrome/
 */
public class ShortestPalindrome {

    public static void main(String[] args) throws Exception {

        String str = "abcd";

        String shortestPalindrome = new ShortestPalindrome().shortestPalindrome(str);

        System.out.printf("shortestPalindrome: %s%n", shortestPalindrome);

        System.out.println("ShortestPalindrome done...");
    }


    /*
     time: O(N)
     space: O(N)
     1. Construct 'combined' string as str + '?' + reverse(str)
     2. Find the longest prefix that is also a suffix of 'combined' string (using Knuth-Morris-Pratt like algorithm).
     3. Shortest palindrome can be constructed the following way:
        a. str = [prefix palindrome] + [not palindrome part]
        b. shorted palindrome =  [not palindrome part in reverse order] + [prefix palindrome] + [not palindrome part]
     */
    public String shortestPalindrome(String str) {
        Objects.requireNonNull(str);

        char[] combined = new char[2 * str.length() + 1];

        for (int i = 0; i < str.length(); ++i) {
            combined[i] = str.charAt(i);
        }

        combined[str.length()] = '?';

        for (int i = str.length() - 1, offset = str.length() + 1; i >= 0; --i, ++offset) {
            combined[offset] = str.charAt(i);
        }

        int[] lps = longestPrefixSuffix(combined);

        return createShortestPalindrome(str, lps[lps.length - 1]);
    }

    private String createShortestPalindrome(String str, int prefixPalindromeLength) {

        StringBuilder res = new StringBuilder(2 * str.length());

        for (int i = str.length() - 1; i >= prefixPalindromeLength; --i) {
            res.append(str.charAt(i));
        }

        res.append(str);

        return res.toString();
    }

    private static int[] longestPrefixSuffix(char[] arr) {
        assert arr != null;

        int[] lps = new int[arr.length];

        for (int i = 1; i < arr.length; ++i) {

            int idx = lps[i - 1];

            while (idx >= 0) {
                if (arr[idx] == arr[i]) {
                    break;
                }

                idx -= 1;

                if (idx >= 0) {
                    idx = lps[idx];
                }
            }

            lps[i] = idx + 1;
        }

        return lps;
    }

}
