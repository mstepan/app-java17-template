package com.max.app17;

import java.util.HashMap;
import java.util.Map;

public class Main {

    public static void main(String[] args) throws Exception {

        final int k = 2;
        final int[] arr = {1, 2, 1, 2, 1, 3, 3};
        int res = new Main().minCost(arr, k);

        System.out.printf("minCost: %d%n", res);

        System.out.println("Main done...");
    }

    public int minCost(int[] arr, int k) {

        int[] opt = new int[arr.length];
        opt[0] = k;

        for (int i = 1; i < opt.length; ++i) {

            int curMin = Integer.MAX_VALUE;

            CostTracker costTracker = new CostTracker(k);
            costTracker.update(arr[i]);

            for (int j = i - 1; j >= 0; --j) {
                curMin = Math.min(curMin, costTracker.cost() + opt[j]);
                costTracker.update(arr[j]);
            }

            curMin = Math.min(curMin, costTracker.cost());

            opt[i] = curMin;
        }

        return opt[opt.length - 1];
    }

    private static class CostTracker {

        final Map<Integer, Integer> freq = new HashMap<>();

        int k;
        int cost;

        public CostTracker(int k) {
            this.k = k;
            this.cost = k;
        }

        void update(int value) {

            Integer prevFreq = freq.get(value);

            if (prevFreq == null) {
                freq.put(value, 1);
            } else {
                if (prevFreq == 1) {
                    cost += 2;
                    freq.put(value, 2);
                } else {
                    freq.compute(value, (key, freq) -> freq + 1);
                    cost += 1;
                }
            }
        }

        int cost() {
            return cost;
        }
    }


}
