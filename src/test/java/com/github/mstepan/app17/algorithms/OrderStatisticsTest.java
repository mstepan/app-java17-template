package com.github.mstepan.app17.algorithms;

import static com.github.mstepan.app17.algorithms.OrderStatistics.selectNthElement;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.util.Arrays;
import org.junit.jupiter.api.Test;

final class OrderStatisticsTest {

    @Test
    void selectNthElementNormalCases() {
        int[] arr = {12, 6, 5, 4, 8, 13, 10, 17, 1};

        assertThat(selectNthElement(arr, 0)).isEqualTo(1);
        assertThat(selectNthElement(arr, 1)).isEqualTo(4);
        assertThat(selectNthElement(arr, 2)).isEqualTo(5);

        assertThat(selectNthElement(arr, 3)).isEqualTo(6);
        assertThat(selectNthElement(arr, 4)).isEqualTo(8);
        assertThat(selectNthElement(arr, 5)).isEqualTo(10);

        assertThat(selectNthElement(arr, 6)).isEqualTo(12);
        assertThat(selectNthElement(arr, 7)).isEqualTo(13);
        assertThat(selectNthElement(arr, 8)).isEqualTo(17);
    }

    @Test
    void selectNthElementRandomArray() {
        int[] arr1 = ArrayUtils.generateRandom(100);
        int[] arr2 = Arrays.copyOf(arr1, arr1.length);

        Arrays.sort(arr1);

        for (int i = 0; i < arr1.length; ++i) {
            assertThat(arr1[i]).isEqualTo(selectNthElement(arr2, i));
        }
    }
}
