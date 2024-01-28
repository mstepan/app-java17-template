package com.max.app17.leetcode.medium;


/**
 * 2443. Sum of Number and Its Reverse
 * 
 * https://leetcode.com/problems/sum-of-number-and-its-reverse/
 */
public class SumOfNumberAndItsReverse {

    public static void main(String[] args) throws Exception {

        int val = 181;

        boolean hasSolution = new SumOfNumberAndItsReverse().sumOfNumberAndReverse(val);
        System.out.printf("hasSolution(%d): %b%n", val, hasSolution);

        System.out.println("SumOfNumberAndItsReverse done...");
    }

    /**
     * N = num
     * time: O(N)
     * space: O(1)
     */
    public boolean sumOfNumberAndReverse(int num) {
        if (num < 0) {
            throw new IllegalArgumentException("num can't be negative: %d".formatted(num));
        }

        /*
        We should iterate here 0...num, b/c of corner cases such as 22 = 20 + 02
         */
        for (int left = 0; left <= num; ++left) {

            int right = reverse(left);

            if (left + right == num) {
                //System.out.printf("%d + %d = %d%n", left, right, num);
                return true;
            }
        }

        return false;
    }

    private static int reverse(int initialValue) {
        assert initialValue >= 0;

        int val = initialValue;
        int res = 0;

        while (val != 0) {
            res = res * 10 + (val % 10);
            val /= 10;
        }

        return res;
    }
    
}
