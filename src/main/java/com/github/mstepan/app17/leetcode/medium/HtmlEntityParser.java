package com.github.mstepan.app17.leetcode.medium;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * 1410. HTML Entity Parser
 *
 * <p>https://leetcode.com/problems/html-entity-parser/
 */
public class HtmlEntityParser {

    private static final Map<String, String> SPECIAL =
            Map.of(
                    "&quot;", "\"",
                    "&apos;", "'",
                    "&amp;", "&",
                    "&gt;", ">",
                    "&lt;", "<",
                    "&frasl;", "/");

    private static final int MAX_SPECIAL_CHAR_LENGTH =
            SPECIAL.keySet().stream().mapToInt(String::length).max().getAsInt();

    private static final char SPECIAL_CHAR_START = '&';
    private static final char SPECIAL_CHAR_END = ';';

    /** Time: O(N) Space: O(N) */
    public String entityParser(String text) {

        Objects.requireNonNull(text);

        char[] arr = text.toCharArray();

        StringBuilder res = new StringBuilder(arr.length);

        int idx = 0;

        while (idx < arr.length) {
            char ch = arr[idx];

            if (ch == SPECIAL_CHAR_START) {
                Optional<SpecialToken> maybeSpecialSymbol = parseSpecialSymbol(arr, idx);

                if (maybeSpecialSymbol.isPresent()) {
                    SpecialToken token = maybeSpecialSymbol.get();
                    res.append(token.symbol);
                    idx = token.to + 1;
                } else {
                    res.append(ch);
                    ++idx;
                }
            } else {
                res.append(ch);
                ++idx;
            }
        }

        return res.toString();
    }

    private static final class SpecialToken {
        final String symbol;
        final int from;
        final int to;

        SpecialToken(String symbol, int from, int to) {
            this.symbol = symbol;
            this.from = from;
            this.to = to;
        }
    }

    private static Optional<SpecialToken> parseSpecialSymbol(char[] arr, int from) {
        for (int cnt = 0, to = from;
                cnt < MAX_SPECIAL_CHAR_LENGTH && to < arr.length;
                ++cnt, ++to) {
            if (arr[to] == SPECIAL_CHAR_END) {
                String possibleSpecialSymbolKey = new String(arr, from, (to - from + 1));

                String replacementSymbol = SPECIAL.get(possibleSpecialSymbolKey);

                if (replacementSymbol == null) {
                    return Optional.empty();
                } else {
                    return Optional.of(new SpecialToken(replacementSymbol, from, to));
                }
            }
        }
        return Optional.empty();
    }
}
