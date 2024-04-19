package com.github.mstepan.app17;

import java.util.Objects;

public class Main {

    public static void main(String[] args) throws Exception {

        int[] arr = {4, 5, 8, 7, 6, 3, 4, 10000};

        double mean = mean(arr);
        System.out.printf("mean: %.1f%n", mean);

        double harmonicMean = harmonicMean(arr);
        System.out.printf("harmonicMean: %.1f%n", harmonicMean);

        System.out.println("Main done...");
    }

    private static double harmonicMean(int[] arr) {
        Objects.requireNonNull(arr, "null 'arr' detected");

        double inverseSum = 0.0;

        for (int val : arr) {
            inverseSum += 1.0 / val;
        }

        double n = arr.length;

        return n / inverseSum;
    }

    public static double mean(int[] arr) {
        Objects.requireNonNull(arr, "null 'arr' detected");

        double sum = 0.0;

        for (int val : arr) {
            sum += val;
        }

        return sum / arr.length;
    }
}
