package com.github.mstepan.app17;


public class Main {

    public static void main(String[] args) throws Exception {

        char[] arr = "catsanddog".toCharArray();

        int left = 0;
        int right = 2;

        String sub = new String(arr, left, right - left + 1);

        System.out.println(sub);

        System.out.println("Main done...");
    }
}
