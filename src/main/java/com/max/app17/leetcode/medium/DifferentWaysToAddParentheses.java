package com.max.app17.leetcode.medium;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 241. Different Ways to Add Parentheses
 *
 * <p>https://leetcode.com/problems/different-ways-to-add-parentheses/
 */
public class DifferentWaysToAddParentheses {

    public static void main(String[] args) {
        List<Integer> result = new DifferentWaysToAddParentheses().diffWaysToCompute("2*3-4*5");

        System.out.println(result);

        System.out.println("DifferentWaysToAddParentheses done...");
    }

    /** Time: O(C*n), where C = n-th Catalan number Space: O(N) */
    public List<Integer> diffWaysToCompute(String expression) {
        Objects.requireNonNull(expression);

        Token[] tokens = parse(expression);

        List<Integer> results = eval(tokens, 0, tokens.length - 1);

        return results;
    }

    private List<Integer> eval(Token[] tokens, int start, int end) {
        assert start >= end : "start < end";

        if (start == end) {
            return List.of(((NumberToken) tokens[start]).val());
        }

        List<Integer> results = new ArrayList<>();

        for (int mid = start; mid < end; mid += 2) {
            List<Integer> left = eval(tokens, start, mid);
            assert left != null : "'left' is null";

            List<Integer> right = eval(tokens, mid + 2, end);
            assert right != null : "'right' is null";

            assert tokens[mid + 1] instanceof OperationToken : "Incorrect token type";
            OperationToken op = (OperationToken) tokens[mid + 1];

            for (int leftVal : left) {
                for (int rightVal : right) {
                    int curRes = op.apply(leftVal, rightVal);
                    results.add(curRes);
                }
            }
        }

        return results;
    }

    private Token[] parse(String expression) {
        List<Token> tokens = new ArrayList<>();

        int pos = 0;

        while (pos < expression.length()) {

            char ch = expression.charAt(pos);

            if (Character.isDigit(ch)) {

                int number = ch - '0';
                ++pos;

                while (pos < expression.length() && Character.isDigit(expression.charAt(pos))) {
                    number = 10 * number + (expression.charAt(pos) - '0');
                    assert number < 100 : "Number is bigger than 100";
                    ++pos;
                }

                assert number >= 0 && number < 100
                        : "Incorrect number, should be in range [0; 99], but found " + number;

                tokens.add(new NumberToken(number));
            } else {
                assert ch == '+' || ch == '-' || ch == '*'
                        : "Incorrect operation symbol detected: " + ch;
                tokens.add(new OperationToken(ch));
                ++pos;
            }
        }

        return tokens.toArray(new Token[] {});
    }

    sealed interface Token permits OperationToken, NumberToken {}

    record OperationToken(char opSymbol) implements Token {

        OperationToken {
            if (opSymbol != '+' && opSymbol != '-' && opSymbol != '*') {
                throw new IllegalArgumentException(
                        "Incorrect operation symbol detected: '%c'".formatted(opSymbol));
            }
        }

        public int apply(int leftVal, int rightVal) {
            return switch (opSymbol) {
                case '+' -> leftVal + rightVal;
                case '-' -> leftVal - rightVal;
                case '*' -> leftVal * rightVal;
                default -> throw new IllegalStateException(
                        "Undefined operation: %c".formatted(opSymbol));
            };
        }
    }

    record NumberToken(int val) implements Token {}
}
