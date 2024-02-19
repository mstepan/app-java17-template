package com.max.app17.leetcode.hard;

import java.util.ArrayList;
import java.util.List;

/**
 * 1178. Number of Valid Words for Each Puzzle
 * https://leetcode.com/problems/number-of-valid-words-for-each-puzzle/
 */
public class NumberOfValidWordsForEachPuzzle {

    public static void main(String[] args) throws Exception {

        // [1,1,3,2,4,0]
        //        String[] words = {"aaaa", "asas", "able", "ability", "actt", "actor", "access"};
        //        String[] puzzles = {"aboveyz", "abrodyz", "abslute", "absoryz", "actresz",
        // "gaswxyz"};

        //        [0,1,3,2,0]
        String[] words = {"apple", "pleas", "please"};
        String[] puzzles = {"aelwxyz", "aelpxyz", "aelpsxy", "saelpxy", "xaelpsy"};

        List<Integer> matchingByPuzzle = findNumOfValidWords(words, puzzles);

        System.out.println(matchingByPuzzle);

        System.out.println("NumberOfValidWordsForEachPuzzle done...");
    }

    /** time: O(N*M) space: O(N+M) */
    public static List<Integer> findNumOfValidWords(String[] words, String[] puzzles) {

        int[] wordsBits = new int[words.length];

        for (int i = 0; i < wordsBits.length; ++i) {
            wordsBits[i] = createSubsetMask(words[i]);
        }

        List<Integer> puzzleMatches = new ArrayList<>();

        for (String singlePuzzle : puzzles) {
            int puzzleMask = createSubsetMask(singlePuzzle);

            int firstCharMask = maskForChar(singlePuzzle.charAt(0));

            int cnt = 0;
            for (int i = 0; i < wordsBits.length; ++i) {

                int wordMask = wordsBits[i];

                if (isBitSet(wordMask, firstCharMask) && isSubset(puzzleMask, wordMask)) {
                    ++cnt;
                }
            }

            puzzleMatches.add(cnt);
        }

        return puzzleMatches;
    }

    private static boolean isBitSet(int baseMask, int bitMask) {
        return (baseMask & bitMask) != 0;
    }

    private static int maskForChar(char ch) {
        return (1 << (ch - 'a'));
    }

    private static boolean isSubset(int mainSet, int subset) {
        return (mainSet | subset) == mainSet;
    }

    private static int createSubsetMask(String str) {
        assert str != null : "null 'str' detected";

        int mask = 0;

        for (int i = 0; i < str.length(); ++i) {
            char ch = str.charAt(i);
            assert ch >= 'a' && ch <= 'z' : "not a lower case english letter";

            int offset = ch - 'a';

            assert offset >= 0 && offset < Integer.SIZE;

            mask |= (1 << offset);
        }

        return mask;
    }
}
