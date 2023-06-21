package com.max.app17.facebook;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Deque;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

public class MergeOverlappingIntervals {

    public static void main(String[] args) throws Exception {
        Interval[] arr = new Interval[10];

        ThreadLocalRandom rand = ThreadLocalRandom.current();

        for (int i = 0; i < arr.length; ++i) {
            int first = rand.nextInt(100);
            int second = rand.nextInt(100);
            arr[i] = new Interval(Math.min(first, second), Math.max(first, second));
        }

        Arrays.sort(arr, Interval.START_ASC);

        Arrays.stream(arr).forEach(System.out::println);

        Interval[] merged = mergeOverlapped(arr);

        System.out.println("\nMerged: ");
        Arrays.stream(merged).forEach(System.out::println);


        System.out.println("MergeOverlappingIntervals done...");
    }

    /**
     * time: O(n*lgN)
     * space: O(N)
     */
    public static Interval[] mergeOverlapped(Interval[] arr) {
        Objects.requireNonNull(arr);

        if (arr.length < 2) {
            return Arrays.copyOf(arr, arr.length);
        }

        Arrays.sort(arr, Interval.START_ASC);

        Deque<Interval> res = new ArrayDeque<>();
        res.addLast(arr[0]);

        for (int i = 1; i < arr.length; ++i) {

            assert res.size() > 0 : "deque size is 0";

            Interval last = res.peekLast();
            Interval cur = arr[i];

            if (Interval.isOverlapped(last, cur)) {
                res.pollLast();
                res.addLast(Interval.merge(last, cur));
            }
            else {
                res.addLast(cur);
            }
        }

        return res.toArray(new Interval[0]);
    }


    record Interval(int start, int end) {

        public static Comparator<Interval> START_ASC = Comparator.comparing(Interval::start);

        public static boolean isOverlapped(Interval left, Interval right) {
            return left.end >= right.start;
        }

        public static Interval merge(Interval first, Interval second) {
            return new Interval(Math.min(first.start, second.start), Math.max(first.end, second.end));
        }

    }

}
