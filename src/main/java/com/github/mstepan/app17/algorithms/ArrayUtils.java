package com.github.mstepan.app17.algorithms;

import static com.github.mstepan.app17.utils.Preconditions.checkArguments;
import static com.github.mstepan.app17.utils.Preconditions.checkInRange;
import static com.github.mstepan.app17.utils.Preconditions.checkNotNull;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public final class ArrayUtils {

    private ArrayUtils() {
        throw new AssertionError("Can't instantiate utility-only class");
    }

    public static void swap(int[] arr, int from, int to) {
        checkNotNull(arr, "null 'arr' value detected");
        checkInRange(from, 0, arr.length - 1, "'from' is out of bounds");
        checkInRange(to, 0, arr.length - 1, "'to' is out of bounds");

        int temp = arr[from];
        arr[from] = arr[to];
        arr[to] = temp;
    }

    /**
     * Generate array with random values.
     *
     * @param length - the expected length of a generated array.
     * @return array with random values.
     */
    public static int[] generateRandom(int length) {
        checkArguments(length >= 0, "negative 'length' detected, length = %d".formatted(length));
        int[] arr = new int[length];

        Random rand = ThreadLocalRandom.current();

        for (int i = 0; i < arr.length; ++i) {
            arr[i] = rand.nextInt();
        }

        return arr;
    }
}
