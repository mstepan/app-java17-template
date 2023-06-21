package com.max.app17.leetcode.hard;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 10. Regular Expression Matching
 * https://leetcode.com/problems/regular-expression-matching/
 */
public class RegularExpressionMatching {

    public static void main(String[] args) throws Exception {

        RegularExpressionMatching main = new RegularExpressionMatching();

        final String text = "aaaaaaaaaaaaaaaaaaaa";
        final String pattern = "a*a*a*a*a*a*a*a*a*a*b";

        Pattern regexp = Pattern.compile(pattern);

        Matcher matcher = regexp.matcher(text);
        boolean fullyMatched = matcher.matches();

        System.out.printf("java regexp: %b\n", fullyMatched);

        System.out.printf("my regexp: %b\n", main.isMatch(text, pattern));

        System.out.println("Main done...");
    }


    public boolean isMatch(String text, String pattern) {
        return isMatchedIterative(text.toCharArray(), pattern.toCharArray());
    }

    /**
     * We will use Thomson regexp evaluation algorithm to reduce abnormal exponential time in worst case.
     */
    private boolean isMatchedIterative(char[] text, char[] pattern) {

        Set<State> curLevel = new LinkedHashSet<>();
        curLevel.add(new State(0, 0));

        while (!curLevel.isEmpty()) {

            Set<State> nextLevel = new LinkedHashSet<>();

            for (State curState : curLevel) {

                if (isTerminatedState(text, pattern, curState)) {
                    boolean isMatched = evalTerminationState(text, pattern, curState);
                    if (isMatched) {
                        return true;
                    }
                }
                else {
                    addPossibleTransitions(nextLevel, text, pattern, curState);
                }
            }

            curLevel = nextLevel;
        }

        return false;
    }

    static final class State {
        final int textIndex;
        final int patternIndex;

        State(int textIndex, int patternIndex) {
            this.textIndex = textIndex;
            this.patternIndex = patternIndex;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            State state = (State) o;
            return textIndex == state.textIndex && patternIndex == state.patternIndex;
        }

        @Override
        public int hashCode() {
            return Objects.hash(textIndex, patternIndex);
        }
    }

    private void addPossibleTransitions(Set<State> nextLevel, char[] text, char[] pattern, State state) {

        final int textIndex = state.textIndex;
        final int patternIndex = state.patternIndex;

        boolean hasNextWildcard = (patternIndex + 1 < pattern.length && pattern[patternIndex + 1] == '*');

        if (hasNextWildcard) {
            if (isMatchedChars(text[textIndex], pattern[patternIndex])) {
                // 1.....Infinity match
                nextLevel.add(new State(textIndex + 1, patternIndex));

                // 1 match
                nextLevel.add(new State(textIndex + 1, patternIndex + 2));
            }

            // 0 match
            nextLevel.add(new State(textIndex, patternIndex + 2));
        }
        else {
            if (isMatchedChars(text[textIndex], pattern[patternIndex])) {
                nextLevel.add(new State(textIndex + 1, patternIndex + 1));
            }
        }


    }

    private boolean isTerminatedState(char[] text, char[] pattern, State state) {
        return text.length == state.textIndex || pattern.length == state.patternIndex;
    }

    private boolean evalTerminationState(char[] text, char[] pattern, State curState) {
        return (text.length == curState.textIndex &&
                (pattern.length == curState.patternIndex || hasWildcardsSuffixOnly(pattern, curState.patternIndex)));
    }

    private boolean hasWildcardsSuffixOnly(char[] pattern, int j) {

        int elemsLeft = pattern.length - j;

        if (isOdd(elemsLeft)) {
            return false;
        }

        for (int index = j + 1; index < pattern.length; index += 2) {
            if (pattern[index] != '*') {
                return false;
            }
        }

        return true;
    }

    private boolean isOdd(int value) {
        return (value & 1) != 0;
    }

    private boolean isMatchedChars(char textCh, char patternCh) {
        return patternCh == '.' || textCh == patternCh;
    }

}



