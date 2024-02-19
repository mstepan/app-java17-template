package com.max.app17.leetcode.hard;

import java.util.regex.Pattern;

/** 65. Valid Number https://leetcode.com/problems/valid-number/ */
public class ValidNumber {

    private static final String INTEGER_REGEXP_STR = "[+-]?\\d+";

    private static final String FLOAT_REGEXP_STR = "[+-]?((\\d{0,}[.]\\d{1,})|(\\d{1,}[.]\\d{0,}))";

    private static final Pattern INTEGER_REGEXP = Pattern.compile(INTEGER_REGEXP_STR);

    private static final Pattern NUMBER_REGEXP =
            Pattern.compile("(%s)|(%s)".formatted(INTEGER_REGEXP_STR, FLOAT_REGEXP_STR));

    public static boolean isNumber(String str) {

        String lower = str.toLowerCase();

        int eIndex = lower.indexOf('e');

        if (eIndex != -1) {
            String left = lower.substring(0, eIndex);
            String right = lower.substring(eIndex + 1);

            return NUMBER_REGEXP.matcher(left).matches() && INTEGER_REGEXP.matcher(right).matches();
        }

        return NUMBER_REGEXP.matcher(lower).matches();
    }
}
