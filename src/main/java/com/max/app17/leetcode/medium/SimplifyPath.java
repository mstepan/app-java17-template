package com.max.app17.leetcode.medium;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Objects;

/**
 * 71. Simplify Path
 *
 * <p>https://leetcode.com/problems/simplify-path/
 */
public class SimplifyPath {

    public static void main(String[] args) throws Exception {
        // String simplePath = new SimplifyPath().simplifyPath("/home/.//../etc/dev/.");
        // String simplePath = new SimplifyPath().simplifyPath("/../");
        String simplePath = new SimplifyPath().simplifyPath("/home/../////etc/.//dev/test//..//");

        System.out.printf("path: '%s'%n", simplePath);

        System.out.println("SimplifyPath done...");
    }

    /** time: O(N) space: O(N) */
    public String simplifyPath(String path) {
        Objects.requireNonNull(path);

        StringPathTokenizer tokenizer = new StringPathTokenizer(path.toCharArray());

        Deque<Token> stack = new ArrayDeque<>();

        for (Token curToken = tokenizer.next(); curToken != null; curToken = tokenizer.next()) {

            switch (curToken.type) {
                case PREV_DIR -> {
                    if (!stack.isEmpty()) {
                        Token prevToken = stack.pop();
                        assert prevToken.type == TokenType.DIR_NAME;
                    }
                }
                case DIR_NAME -> stack.push(curToken);
            }
        }

        return combinePath(stack);
    }

    private String combinePath(Deque<Token> stack) {

        if (stack.isEmpty()) {
            return "/";
        }

        StringBuilder res = new StringBuilder();

        while (!stack.isEmpty()) {
            res.append("/");
            res.append(stack.pollLast().strRepresentation());
        }

        return res.toString();
    }

    private static class StringPathTokenizer {

        final char[] path;
        int curIdx;

        StringPathTokenizer(char[] path) {
            this.path = path;
        }

        Token next() {
            if (curIdx >= path.length) {
                return null;
            }

            if (path[curIdx] == '/') {
                while (curIdx < path.length && path[curIdx] == '/') {
                    ++curIdx;
                }
                return new Token(TokenType.SLASH, null);
            } else if (isDirNameCh(path[curIdx])) {
                int from = curIdx;

                while (curIdx < path.length && isDirNameCh(path[curIdx])) {
                    ++curIdx;
                }

                String sub = String.valueOf(path, from, curIdx - from);

                if (".".equals(sub)) {
                    return new Token(TokenType.CUR_DIR, sub);
                } else if ("..".equals(sub)) {
                    return new Token(TokenType.PREV_DIR, sub);
                }

                return new Token(TokenType.DIR_NAME, sub);
            }

            return null;
        }

        private boolean isDirNameCh(char ch) {
            return Character.isAlphabetic(ch) || Character.isDigit(ch) || ch == '.' || ch == '_';
        }
    }

    private record Token(TokenType type, String value) {
        @Override
        public String toString() {
            if (value == null) {
                return "(%s)".formatted(type);
            }

            return "(%s; %s)".formatted(type, value);
        }

        public String strRepresentation() {
            if (type == TokenType.DIR_NAME) {
                return value;
            }

            if (type == TokenType.SLASH) {
                return "/";
            }

            throw new IllegalStateException(
                    "No string representation for token type %s".formatted(type));
        }
    }

    enum TokenType {
        DIR_NAME,
        CUR_DIR,
        SLASH,
        PREV_DIR;
    }
}
