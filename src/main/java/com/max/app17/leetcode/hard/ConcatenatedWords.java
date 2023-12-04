package com.max.app17.leetcode.hard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class ConcatenatedWords {

    public static void main(String[] args) throws Exception {

        String[] words = {
            "cat",
            "cats",
            "catsdogcats",
            "dog",
            "dogcatsdog",
            "hippopotamuses",
            "rat",
            "ratcatdogcat"
        };

        // String[] words = {"cat", "dog", "catdog"};

        List<String> concatenatedWords = new ConcatenatedWords().findAllConcatenatedWordsInADict(words);

        System.out.println(concatenatedWords);

        System.out.println("ConcatenatedWords done...");
    }

    /*
   N = words.length
   K = average words[i].length
   time: O (N*lgN + N*K^2)
   space: O(N*K)
   N = 10^4, K = 30
   N * K^2 = 10^4 * 900 = 9*10^6 = 9M
    */
    public List<String> findAllConcatenatedWordsInADict(String[] words) {
        Objects.requireNonNull(words);

        // sort words by length, so that we can process in left to right order
        // time: O(N*lgN)
        Arrays.sort(words, Comparator.comparingInt(String::length));

        Set<String> processedWords = new HashSet<>(2 * words.length);

        List<String> concatenated = new ArrayList<>();

        // time: O(N)
        for (String curWord : words) {

            // time: O(K^2)
            if (isConcatenated(curWord, processedWords)) {
                concatenated.add(curWord);
            }
            processedWords.add(curWord);
        }

        return concatenated;
    }

    private boolean isConcatenated(String word, Set<String> existingWords) {
        assert word != null;
        assert word.length() > 0;
        assert existingWords != null;

        boolean[] sol = new boolean[word.length()];

        for (int i = 0; i < sol.length; ++i) {
            for (int j = i; j >= 0; --j) {
                String subWord = word.substring(j, i + 1);

                if (existingWords.contains(subWord) && (j == 0 || sol[j - 1])) {
                    sol[i] = true;
                    break;
                }
            }
        }

        return sol[sol.length - 1];
    }

}
