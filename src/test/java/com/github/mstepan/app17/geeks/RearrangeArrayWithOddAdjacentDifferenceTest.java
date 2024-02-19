package com.github.mstepan.app17.geeks;

import static com.github.mstepan.app17.geeks.RearrangeArrayWithOddAdjacentDifference.isOdd;
import static com.github.mstepan.app17.geeks.RearrangeArrayWithOddAdjacentDifference.rearrange;
import static com.github.mstepan.app17.geeks.RearrangeArrayWithOddAdjacentDifference.swap;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import org.junit.jupiter.api.Test;

public class RearrangeArrayWithOddAdjacentDifferenceTest {

    @Test
    public void checkWithEqualNumberOfOddAndEvenElements() {
        final int[] arr = new int[] {1, 3, 7, 2, 4, 6};
        randomShuffle(arr);
        assertTrue(rearrange(arr));
        assertTrue(hasOddDiffBetweenAdjElements(arr));
    }

    @Test
    public void checkWithOddElemsLarger() {
        final int[] arr = new int[] {1, 3, 7, 9, 2, 4, 6};
        randomShuffle(arr);
        assertTrue(rearrange(arr));
        assertTrue(hasOddDiffBetweenAdjElements(arr));
    }

    @Test
    public void checkWithEvenElemsLarger() {
        final int[] arr = new int[] {1, 3, 7, 2, 4, 6, 10};
        assertTrue(rearrange(arr));
        assertTrue(hasOddDiffBetweenAdjElements(arr));
    }

    @Test
    public void checkRandomGeneratedArrayShouldBeOk() {

        final Random rand = ThreadLocalRandom.current();

        final int length = rand.nextInt(1000);
        final int[] arr = new int[length];

        final int oddCount = (int) Math.floor((double) length / 2);
        final int evenCount = (int) Math.ceil((double) length / 2);

        int index = 0;

        for (int i = 0, val = 3; i < oddCount; ++i, val += 2, ++index) {
            arr[index] = val;
        }

        for (int i = 0, val = 2; i < evenCount; ++i, val += 2, ++index) {
            arr[index] = val;
        }

        randomShuffle(arr);

        assertTrue(rearrange(arr));
        assertTrue(hasOddDiffBetweenAdjElements(arr));
    }

    @Test
    public void emptyArrayShouldBeOk() {
        assertTrue(rearrange(new int[] {}));
    }

    @Test
    public void singleElementArrayShouldBeOk() {
        assertTrue(rearrange(new int[] {3}));
    }

    @Test
    public void oddsLongerByTwoCantRearrange() {
        final int[] arr = new int[] {1, 3, 7, 9, 11, 2, 4, 6};
        randomShuffle(arr);
        assertFalse(rearrange(arr));
    }

    @Test
    public void evensLongerByTwoCantRearrange() {
        final int[] arr = new int[] {1, 3, 7, 2, 4, 6, 10, 12};
        randomShuffle(arr);
        assertFalse(rearrange(arr));
    }

    @Test
    public void nullArrayShouldFail() {
        assertThrows(NullPointerException.class, () -> rearrange(null));
    }

    private static boolean hasOddDiffBetweenAdjElements(int[] arr) {
        for (int i = 1; i < arr.length; ++i) {
            if (!isOdd(Math.abs(arr[i - 1] - arr[i]))) {
                return false;
            }
        }
        return true;
    }

    private void randomShuffle(int[] arr) {
        final Random rand = ThreadLocalRandom.current();

        for (int i = 0; i < arr.length - 1; ++i) {
            int randIndex = i + rand.nextInt(arr.length - i);
            swap(arr, i, randIndex);
        }
    }
}
