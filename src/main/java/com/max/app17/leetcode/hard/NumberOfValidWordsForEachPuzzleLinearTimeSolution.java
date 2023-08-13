package com.max.app17.leetcode.hard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 1178. Number of Valid Words for Each Puzzle
 * https://leetcode.com/problems/number-of-valid-words-for-each-puzzle/
 */
public class NumberOfValidWordsForEachPuzzleLinearTimeSolution {

    public static void main(String[] args) throws Exception {

        // [1,1,3,2,4,0]
        String[] words = {"aaaa", "asas", "able", "ability", "actt", "actor", "access"};
        String[] puzzles = {"aboveyz", "abrodyz", "abslute", "absoryz", "actresz", "gaswxyz"};

        //        [0,1,3,2,0]
        //        String[] words = {"apple", "pleas", "please"};
        //        String[] puzzles = {"aelwxyz", "aelpxyz", "aelpsxy", "saelpxy", "xaelpsy"};

        List<Integer> matchingByPuzzle = findNumOfValidWords(words, puzzles);

        System.out.println(matchingByPuzzle);

        System.out.println("NumberOfValidWordsForEachPuzzleLinearTimeSolution done...");
    }

    /** time: O(M*50 + N*7*2^6) ~ linear time space: O(M) */
    public static List<Integer> findNumOfValidWords(String[] words, String[] puzzles) {

        Map<Integer, Integer> wordsBitFreq = new HashMap<>();

        for (int i = 0; i < words.length; ++i) {
            int wordMask = createSubsetMask(words[i]);
            wordsBitFreq.compute(wordMask, (keyNotUsed, cnt) -> cnt == null ? 1 : cnt + 1);
        }

        List<Integer> puzzleMatches = new ArrayList<>();

        for (String singlePuzzle : puzzles) {

            int puzzleMask = createSubsetMask(singlePuzzle);
            int firstCharMask = maskForChar(singlePuzzle.charAt(0));

            int[] subsets = generateSubsets(puzzleMask ^ firstCharMask);

            int cnt = 0;
            for (int subsetMask : subsets) {
                int curMaskToCheck = subsetMask | firstCharMask;
                cnt += (wordsBitFreq.getOrDefault(curMaskToCheck, 0));
            }
            puzzleMatches.add(cnt);
        }

        return puzzleMatches;
    }

    private static int[] generateSubsets(int value) {
        int bitsCnt = countSetBits(value);

        assert bitsCnt >= 0 && bitsCnt < 7;

        final int subsetsCnt = 1 << bitsCnt;
        int[] subsets = new int[subsetsCnt];
        subsets[0] = 0;

        int[] bitsIndexes = findSetBitsIndexes(bitsCnt, value);

        for (int mask = 1; mask < subsetsCnt; ++mask) {

            subsets[mask] = createMask(bitsIndexes, mask);

            // a k s

            // 001 => s
            // 010 => k
            // 011 => ks
            // 100 => a
            // 101 => as
            // 110 => ak
            // 111 => aks
        }

        return subsets;
    }

    private static int createMask(int[] bitsIndexes, int mask) {

        int res = 0;
        for (int i = 0; i < bitsIndexes.length; ++i) {
            if ((mask & (1 << i)) != 0) {
                res |= (1 << bitsIndexes[i]);
            }
        }

        return res;
    }

    private static int[] findSetBitsIndexes(int setBitsCnt, int baseValue) {

        int[] res = new int[setBitsCnt];

        int value = baseValue;

        for (int i = 0; i < res.length; ++i) {
            int idx = value & ((~value) + 1);
            res[i] = log2(idx);
            value &= (value - 1);
        }

        return res;
    }

    private static int log2(double value) {
        return (int) (Math.log(value) / Math.log(2.0));
    }

    private static int countSetBits(int baseValue) {

        int val = baseValue;

        int cnt = 0;

        while (val != 0) {
            val &= (val - 1);
            ++cnt;
        }

        return cnt;
    }

    private static int maskForChar(char ch) {
        return (1 << (ch - 'a'));
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
