package com.max.app17.leetcode.medium;

/**
 * 12. Integer to Roman
 *
 * <p>https://leetcode.com/problems/integer-to-roman/
 */
public class IntegerToRoman {

    public static void main(String[] args) throws Exception {

        String romanNum = new IntegerToRoman().intToRoman(1994);
        System.out.println(romanNum);

        System.out.println("IntegerToRoman done...");
    }

    private static final int MIN_ROMAN_NUM = 1;
    private static final int MAX_ROMAN_NUM = 3999;

    public String intToRoman(int num) {
        if (num < MIN_ROMAN_NUM || num > MAX_ROMAN_NUM) {
            throw new IllegalArgumentException(
                    "Number is out of range should be in range [%d...%d]"
                            .formatted(MIN_ROMAN_NUM, MAX_ROMAN_NUM));
        }
        StringBuilder res = new StringBuilder();

        int val = num;

        res.append("M".repeat(val / 1000));
        val %= 1000;

        val = appendRomanDigitsForLevel(val, res, 100, "C", "D", "M");

        val = appendRomanDigitsForLevel(val, res, 10, "X", "L", "C");

        val = appendRomanDigitsForLevel(val, res, 1, "I", "V", "X");

        assert val == 0 : "val != 0";

        return res.toString();
    }

    private static int appendRomanDigitsForLevel(
            int val,
            StringBuilder res,
            int order,
            String firstChar,
            String midChar,
            String nextChar) {

        int cnt = val / order;

        if (cnt == 4) {
            res.append(firstChar).append(midChar);
        } else if (cnt == 9) {
            res.append(firstChar).append(nextChar);
        } else if (cnt < 5) {
            res.append(firstChar.repeat(cnt));
        } else {
            res.append(midChar);
            res.append(firstChar.repeat(cnt - 5));
        }

        return val % order;
    }
}
