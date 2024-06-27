package com.github.mstepan.app17.leetcode.medium;

import java.util.Map;
import java.util.Objects;

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

        MAIN:
        while (idx < arr.length) {
            char ch = arr[idx];

            if (ch == SPECIAL_CHAR_START) {
                for (int cnt = 0, to = idx + 1;
                        cnt < MAX_SPECIAL_CHAR_LENGTH - 1 && to < arr.length;
                        ++cnt, ++to) {
                    if (arr[to] == SPECIAL_CHAR_END) {
                        String possibleSpecialSymbolKey = new String(arr, idx, (to - idx + 1));

                        String replacementSymbol = SPECIAL.get(possibleSpecialSymbolKey);

                        if (replacementSymbol == null) {
                            break;
                        } else {
                            res.append(replacementSymbol);
                            idx = to + 1;
                            continue MAIN;
                        }
                    }
                }
                res.append(ch);
            } else {
                res.append(ch);
            }

            ++idx;
        }

        return res.toString();
    }
}
