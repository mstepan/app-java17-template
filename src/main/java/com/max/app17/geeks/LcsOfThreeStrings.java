package com.max.app17.geeks;

/**
 * LCS of three strings
 *
 * <p>https://www.geeksforgeeks.org/problems/lcs-of-three-strings0028/1
 *
 * <p>Given 3 strings A, B and C, the task is to find the length of the longest sub-sequence that is
 * common in all the three given strings.
 */
public class LcsOfThreeStrings {

    public static void main(String[] args) throws Exception {

        // "uuutu" "tuu" "ttttu" => 2

        String a = "uuutu";
        String b = "tuu";
        String c = "ttttu";

        int lcsLength = LCSof3(a, b, c, a.length(), b.length(), c.length());

        System.out.println(lcsLength);

        System.out.println("LcsOfThreeStrings done...");
    }

    static int LCSof3(String A, String B, String C, int n1, int n2, int n3) {
        return lcs(A, B, C);
    }

    /**
     * N = firstStr.length()
     *
     * <p>M = secondStr.length()
     *
     * <p>K = thirdStr.length()
     *
     * <p>time: O (N*M*K) space: O(N*M*K)
     */
    private static int lcs(String firstStr, String secondStr, String thirdStr) {
        char[] first = firstStr.toCharArray();
        char[] second = secondStr.toCharArray();
        char[] third = thirdStr.toCharArray();

        int rows = first.length + 1;
        int cols = second.length + 1;
        int dims = third.length + 1;

        int[][][] sol = new int[rows][cols][dims];

        for (int row = 1; row < rows; ++row) {
            for (int col = 1; col < cols; ++col) {

                for (int dim = 1; dim < dims; ++dim) {

                    char ch1 = first[row - 1];
                    char ch2 = second[col - 1];
                    char ch3 = third[dim - 1];

                    if (ch1 == ch2 && ch2 == ch3) {
                        sol[row][col][dim] = 1 + sol[row - 1][col - 1][dim - 1];
                    } else if (ch1 == ch2) {
                        sol[row][col][dim] =
                                Math.max(sol[row][col][dim - 1], sol[row - 1][col - 1][dim]);

                    } else if (ch1 == ch3) {
                        sol[row][col][dim] =
                                Math.max(sol[row][col - 1][dim], sol[row - 1][col][dim - 1]);
                    } else if (ch2 == ch3) {
                        sol[row][col][dim] =
                                Math.max(sol[row - 1][col][dim], sol[row][col - 1][dim - 1]);
                    } else {
                        sol[row][col][dim] =
                                Math.max(
                                        Math.max(sol[row - 1][col][dim], sol[row][col - 1][dim]),
                                        sol[row][col][dim - 1]);
                    }
                }
            }
        }

        return sol[rows - 1][cols - 1][dims - 1];
    }
}
