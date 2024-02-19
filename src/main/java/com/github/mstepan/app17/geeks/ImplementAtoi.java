package com.github.mstepan.app17.geeks;

public class ImplementAtoi {

    public static void main(String[] args) throws Exception {

        String str = "-123";

        System.out.println(atoi(str));

        System.out.println("ImplementAtoi done...");
    }

    /**
     * Implement Atoi
     *
     * <p>https://www.geeksforgeeks.org/problems/implement-atoi/1?utm_source=geeksforgeeks&utm_medium=newui_home&utm_campaign=potd
     *
     * <p>Below function may overflow when str = "9_999_999_999" but that's expected
     *
     * <p>Constraints: 1 ≤ |s| ≤ 10
     */
    static int atoi(String initialStr) {
        if (initialStr == null) {
            throw new IllegalArgumentException("'initialStr' is null");
        }

        String str = initialStr.trim();

        if (str.isEmpty()) {
            return -1;
        }

        if ("-".equals(str)) {
            return -1;
        }

        final int sign = (str.charAt(0) == '-') ? -1 : 1;

        int val = 0;

        final int startIdx = (sign == -1) ? 1 : 0;

        for (int i = startIdx; i < str.length(); ++i) {
            char ch = str.charAt(i);

            if (ch < '0' || ch > '9') {
                return -1;
            }

            val = val * 10 + (ch - '0');
        }

        return sign * val;
    }
}
