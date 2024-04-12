package com.github.mstepan.app17.leetcode.hard;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.PriorityQueue;

/**
 * 502. IPO
 *
 * <p>https://leetcode.com/problems/ipo/
 */
public class IPO {

    public static void main(String[] args) throws Exception {

        // expected = 4
        //        int k = 2;
        //        int w = 0;
        //        int[] profits = {1, 2, 3};
        //        int[] capital = {0, 1, 1};

        int k = 3;
        int w = 0;
        int[] profits = {1, 2, 3};
        int[] capital = {0, 1, 2};

        // expected = 6
        int res = new IPO().findMaximizedCapital(k, w, profits, capital);

        System.out.printf("res = %d%n", res);

        System.out.println("AllocateMailboxes done...");
    }

    /**
     * Use greedy-like approach.
     *
     * <p>time: O(N*lgN + K*lgN) ~ O(N*lgN)
     *
     * <p>space: O(N)
     */
    public int findMaximizedCapital(int k, int w, int[] profits, int[] capital) {

        assert profits != null;
        assert capital != null;

        assert profits.length == capital.length;
        assert k >= 0;
        assert w >= 0;

        Deal[] allDeals = new Deal[profits.length];

        for (int i = 0; i < profits.length; ++i) {
            allDeals[i] = new Deal(profits[i], capital[i]);
        }

        Arrays.sort(allDeals, Deal.CAPITAL_ASC);

        int totalProfit = w;

        PriorityQueue<Deal> maxProfitQueue = new PriorityQueue<>(Deal.PROFIT_ASC);

        Iterator<Deal> dealsByCapitalIt = Arrays.stream(allDeals).iterator();
        Deal lastDeal = null;

        while (dealsByCapitalIt.hasNext()) {

            lastDeal = dealsByCapitalIt.next();

            if (lastDeal.capital > totalProfit) {
                break;
            }

            maxProfitQueue.add(lastDeal);
            lastDeal = null;
        }

        for (int i = 0; i < k && !maxProfitQueue.isEmpty(); ++i) {
            Deal maxProfitDeal = maxProfitQueue.poll();
            totalProfit += maxProfitDeal.profit;

            while (lastDeal != null && totalProfit >= lastDeal.capital()) {
                maxProfitQueue.add(lastDeal);
                lastDeal = dealsByCapitalIt.hasNext() ? dealsByCapitalIt.next() : null;
            }
        }

        return totalProfit;
    }

    private static final class Deal {

        private static final Comparator<Deal> CAPITAL_ASC = Comparator.comparingInt(Deal::capital);

        private static final Comparator<Deal> PROFIT_ASC =
                Comparator.comparingInt(Deal::profit).reversed();

        final int profit;
        final int capital;

        public Deal(int profit, int capital) {
            this.profit = profit;
            this.capital = capital;
        }

        public int capital() {
            return capital;
        }

        public int profit() {
            return profit;
        }

        @Override
        public String toString() {
            return "profit: %d, capital: %d".formatted(profit, capital);
        }
    }
}
