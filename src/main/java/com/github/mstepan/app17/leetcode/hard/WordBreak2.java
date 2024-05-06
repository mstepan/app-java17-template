package com.github.mstepan.app17.leetcode.hard;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Queue;
import java.util.Set;

/**
 * 140. Word Break II
 *
 * <p>https://leetcode.com/problems/word-break-ii/description/
 */
public class WordBreak2 {

    public static void main(String[] args) {

        String str = "catsanddog";
        List<String> dic = List.of("cat", "cats", "and", "sand", "dog");

        List<String> allSentences = new WordBreak2().wordBreak(str, dic);

        for (String singleSentence : allSentences) {
            System.out.printf("%s%n", singleSentence);
        }

        System.out.println("WordBreak2 completed...");
    }

    public List<String> wordBreak(String str, List<String> wordsDic) {
        Objects.requireNonNull(str, "null 'str' detected");
        Objects.requireNonNull(wordsDic, "null 'wordsDic' detected");

        Set<String> dic = new HashSet<>(wordsDic);

        char[] arr = str.toCharArray();

        List<List<Integer>> sol = buildSolutions(arr, dic);

        return buildPossibleSentences(arr, sol);
    }

    /**
     * time: O(N^2)
     *
     * <p>space: O(N)
     */
    private List<List<Integer>> buildSolutions(char[] arr, Set<String> dic) {

        List<List<Integer>> sol = new ArrayList<>(arr.length);
        for (int i = 0; i < arr.length; ++i) {
            sol.add(new ArrayList<>());
        }

        for (int right = 0; right < arr.length; ++right) {

            for (int left = right; left >= 0; --left) {
                String curWord = new String(arr, left, right - left + 1);

                if (dic.contains(curWord) && (left == 0 || !sol.get(left - 1).isEmpty())) {
                    sol.get(right).add(left);
                }
            }
        }

        return sol;
    }

    private List<String> buildPossibleSentences(char[] arr, List<List<Integer>> sol) {

        List<Integer> lastPart = sol.get(sol.size() - 1);

        if (lastPart == null) {
            return List.of();
        }

        Queue<PartialResult> queue = new ArrayDeque<>();

        int rightOffset = sol.size() - 1;

        for (int leftOffset : lastPart) {
            List<String> curSentence = new ArrayList<>();
            curSentence.add(new String(arr, leftOffset, rightOffset - leftOffset + 1));
            queue.add(new PartialResult(curSentence, leftOffset - 1));
        }

        List<String> allSentences = new ArrayList<>();

        while (!queue.isEmpty()) {
            PartialResult curRes = queue.poll();

            // last term found
            if (curRes.idx == -1) {
                allSentences.add(curRes.toSentence());
            } else {

                List<Integer> nextLeftOffsets = sol.get(curRes.idx);

                for (int left : nextLeftOffsets) {

                    int right = curRes.idx;

                    List<String> sentenceCopy = new ArrayList<>(curRes.sentence);
                    sentenceCopy.add(new String(arr, left, right - left + 1));

                    PartialResult newRes = new PartialResult(sentenceCopy, left - 1);

                    queue.add(newRes);
                }
            }
        }
        return allSentences;
    }

    static class PartialResult {
        final List<String> sentence;
        final int idx;

        PartialResult(List<String> sentence, int idx) {
            this.sentence = sentence;
            this.idx = idx;
        }

        String toSentence() {
            List<String> temp = new ArrayList<>(sentence);
            Collections.reverse(temp);

            StringBuilder buf = new StringBuilder();
            for (String word : temp) {
                buf.append(word).append(" ");
            }

            return buf.toString().trim();
        }
    }
}
