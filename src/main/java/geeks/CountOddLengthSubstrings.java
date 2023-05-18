package geeks;

import java.util.Objects;

public class CountOddLengthSubstrings {

    public static void main(String[] args) {

        String str = "ecadgg";
        int k = 4;

        int cnt = countOddSubstringsWithMedian(str, k);

        System.out.println(cnt);

        System.out.printf("Main done... java version: %s%n", System.getProperty("java.version"));
    }

    /**
     * Count odd length Substrings with median same as Kth character of String.
     *
     * https://www.geeksforgeeks.org/count-odd-length-substrings-with-median-same-as-kth-character-of-string/
     *
     * time: O(N^2)
     * space: O(N), but cen be reduced to O(1)
     */
    public static int countOddSubstringsWithMedian(String str, int k) {

        Objects.requireNonNull(str, "null 'str' detected");
        Objects.checkIndex(k, str.length());

        final char[] arr = str.toCharArray();
        final char medCh = arr[k - 1];

        int resCnt = 0;

        for (int i = 0; i < arr.length; ++i) {

            int less = 0;
            int eq = 0;
            int greater = 0;

            for (int j = i; j < arr.length; ++j) {
                char ch = arr[j];

                if (ch == medCh) {
                    ++eq;
                }
                else if (ch > medCh) {
                    ++greater;
                }
                else {
                    ++less;
                }

                if (isMedianForSubstring(less, eq, greater) && isOdd(less + eq + greater)) {
                    ++resCnt;
                    System.out.printf("found: %s%n", str.substring(i, j + 1));
                }
            }
        }

        return resCnt;
    }

    private static boolean isMedianForSubstring(int less, int eq, int greater) {
        return eq != 0 && less == greater;
    }

    private static boolean isOdd(int value) {
        return (value & 1) != 0;
    }


}

