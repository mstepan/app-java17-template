package com.max.app17.leetcode.medium;

import java.util.Arrays;
import java.util.Objects;

/**
 * 238. Product of Array Except Self
 *
 * <p>https://leetcode.com/problems/product-of-array-except-self/
 */
public class ProductOfArrayExceptSelf {

    public static void main(String[] args) throws Exception {

        int[] arr = {1, 2, 3, 4};

        // expected = [24, 12, 8, 6]
        int[] res = new ProductOfArrayExceptSelf().productExceptSelf(arr);

        System.out.println(Arrays.toString(res));

        System.out.println("ProductOfArrayExceptSelf done...");
    }

    /** time: O(N) pace: O(N) */
    public int[] productExceptSelf(int[] arr) {
        Objects.requireNonNull(arr);
        if (arr.length == 0) {
            return new int[] {};
        }

        if (arr.length == 1) {
            return new int[] {0};
        }

        int[] prefix = calculateProductPrefix(arr);
        int[] suffix = calculateProductSuffix(arr);

        int[] product = new int[arr.length];

        product[0] = suffix[1];
        for (int i = 1; i < product.length - 1; ++i) {
            product[i] = prefix[i - 1] * suffix[i + 1];
        }
        product[product.length - 1] = prefix[prefix.length - 2];

        return product;
    }

    private int[] calculateProductPrefix(int[] arr) {
        int[] prefix = new int[arr.length];

        for (int i = 0; i < prefix.length; ++i) {
            prefix[i] = (i > 0 ? prefix[i - 1] : 1) * arr[i];
        }

        return prefix;
    }

    private int[] calculateProductSuffix(int[] arr) {
        int[] suffix = new int[arr.length];

        for (int i = suffix.length - 1; i >= 0; --i) {
            suffix[i] = (i < arr.length - 1 ? suffix[i + 1] : 1) * arr[i];
        }

        return suffix;
    }
}
