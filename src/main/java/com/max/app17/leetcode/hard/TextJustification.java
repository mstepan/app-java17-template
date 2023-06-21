package com.max.app17.leetcode.hard;

import java.util.ArrayList;
import java.util.List;

/**
 * 68. Text Justification
 * https://leetcode.com/problems/text-justification/
 */
public class TextJustification {

    public static void main(String[] args) throws Exception {

        final int maxWidth = 25;
        final String[] words = new String[]{"Give", "me", "my", "Romeo;", "and,", "when", "he", "shall", "die,",
                "Take", "him", "and", "cut", "him", "out", "in", "little", "stars,", "And", "he", "will", "make",
                "the", "face", "of", "heaven", "so", "fine", "That", "all", "the", "world", "will", "be", "in",
                "love", "with", "night", "And", "pay", "no", "worship", "to", "the", "garish", "sun."};

        List<String> lines = fullJustify(words, maxWidth);

        for (String singleLine : lines) {
            System.out.printf("'%s' length = %d\n", singleLine, singleLine.length());
        }

//        String line = buildLine(List.of("worship", "to", "the", "garish"), 25);
//
//        System.out.println(line);
//        System.out.println(line.length());

        System.out.println("TextJustification main done...");
    }

    public static List<String> fullJustify(String[] words, int maxWidth) {

        final List<String> formattedLines = new ArrayList<>();

        int curLength = 0;
        List<String> curLine = new ArrayList<>();

        int index = 0;

        while (index < words.length) {
            String singleWord = words[index];

            if (curLength == 0) {
                curLine.add(singleWord);
                curLength += singleWord.length();
            }
            else if (curLength + 1 + singleWord.length() <= maxWidth) {
                curLine.add(singleWord);
                curLength += (1 + singleWord.length());
            }
            else {
                formattedLines.add(buildLine(curLine, maxWidth));
                curLine.clear();
                curLine.add(singleWord);
                curLength = singleWord.length();
            }

            ++index;

        }

        formattedLines.add(buildLastLine(curLine, maxWidth));

        return formattedLines;
    }

    private static String buildLine(List<String> words, int maxWidth) {

        if (words.size() == 1) {
            String singleWord = words.get(0);
            return singleWord + " ".repeat(maxWidth - singleWord.length());
        }

        int curLength = words.stream().map(String::length).mapToInt(val -> val).sum() + words.size() - 1;

        final int spacesPositions = words.size() - 1;

        int spacesCnt = maxWidth - curLength;

        final int spacesPerPos = spacesCnt / spacesPositions;
        int prefixAdditionalSpaces = spacesCnt % spacesPositions;


        StringBuilder res = new StringBuilder(maxWidth);
        res.append(words.get(0));

        for (int i = 1; i < words.size(); ++i) {
            if (prefixAdditionalSpaces > 0) {
                res.append("  ");
                --prefixAdditionalSpaces;
            }
            else {
                res.append(" ");
            }
            addSpaces(res, spacesPerPos);
            res.append(words.get(i));
        }

        return res.toString();

    }

    private static String buildLastLine(List<String> curLine, int maxWidth) {
        StringBuilder res = new StringBuilder(maxWidth);

        for (int i = 0; i < curLine.size(); ++i) {
            String word = curLine.get(i);
            if (i == 0) {
                res.append(word);
            }
            else {
                res.append(" ").append(word);
            }
        }
        final int spacesToAdd = maxWidth - res.length();
        res.append(" ".repeat(spacesToAdd));

        return res.toString();
    }

    private static void addSpaces(StringBuilder res, int spacesToAdd) {
        res.append(" ".repeat(spacesToAdd));
    }


}



