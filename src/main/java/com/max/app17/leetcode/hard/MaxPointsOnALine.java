package com.max.app17.leetcode.hard;

import java.util.Arrays;
import java.util.Comparator;

/*
149. Max Points on a Line

https://leetcode.com/problems/max-points-on-a-line/

The main algorithm uses all possible pairs of points O(N^2) to generate all possible lines.
For each line we just check how many other points related to this line equation, this also requires single O(N)
iteration per line.
The total time complexity is O(N^3) which is acceptable b/c max N value = 300, so 300^3 = 27M
The most difficult part of this solution is to recognize the fact that you need implement Fractional Numbers yourself
to calculate slope and line equation.

The time complexity can be reduced to O(N^2), but in this case we need to save (using hashtable) all N^2 lines equations,
so space complexity will be increased up to N^2. Time-space tradeoff.
 */
public class MaxPointsOnALine {

    public static void main(String[] args) throws Exception {

        // expected = 4
        //        int[][] points = {{1, 1}, {3, 2}, {5, 3}, {4, 1}, {2, 3}, {1, 4}};

        // expected = 11
        int[][] points = {
            {-184, -551},
            {-105, -467},
            {-90, -394},
            {-60, -248},
            {115, 359},
            {138, 429},
            {60, 336},
            {150, 774},
            {207, 639},
            {-150, -686},
            {-135, -613},
            {92, 289},
            {23, 79},
            {135, 701},
            {0, 9},
            {-230, -691},
            {-115, -341},
            {-161, -481},
            {230, 709},
            {-30, -102}
        };

        int maxPointsCnt = new MaxPointsOnALine().maxPoints(points);

        System.out.printf("maxPointsCnt: %d%n", maxPointsCnt);

        System.out.println("MaxPointsOnALine done...");
    }

    /*
    N = rawPoints.length, maxValue = 300
    time: O(N^3), maxValue = 300^3 = 9M
    space: O(1)
    */
    public int maxPoints(int[][] rawPoints) {

        XYPoint[] points = new XYPoint[rawPoints.length];

        for (int i = 0; i < points.length; ++i) {
            int[] singleRawPoint = rawPoints[i];
            points[i] = new XYPoint(singleRawPoint[0], singleRawPoint[1]);
        }

        // 0, 1, 2
        if (points.length < 3) {
            return points.length;
        }

        Arrays.sort(points, XYPoint.X_THEN_Y_ASC);

        int maxCnt = 0;

        for (int i = 0; i < points.length - 1; ++i) {

            for (int j = i + 1; j < points.length; ++j) {
                XYPoint p1 = points[i];
                XYPoint p2 = points[j];

                int curCount = 2;
                Line line = new Line(p1, p2);

                for (int k = j + 1; k < points.length; ++k) {

                    XYPoint pointToCheck = points[k];

                    if (line.hasPoint(pointToCheck)) {
                        ++curCount;
                    }
                }

                maxCnt = Math.max(maxCnt, curCount);
            }
        }

        return maxCnt;
    }

    record FractionalNum(int num, int denom) {

        FractionalNum {
            if (num == 0) {
                denom = 0;
            } else {

                // negative sign should be added to 'num' part only
                if (num < 0 && denom < 0) {
                    num = Math.abs(num);
                    denom = Math.abs(denom);
                } else if (num > 0 && denom < 0) {
                    num = -num;
                    denom = Math.abs(denom);
                }

                // normalize value
                int gdcVal = gcd(Math.abs(num), Math.abs(denom));

                num = num / gdcVal;
                denom = denom / gdcVal;
            }
        }

        private static int gcd(int first, int second) {

            int x = Math.abs(first);
            int y = Math.abs(second);

            while (y != 0) {
                int rem = x % y;
                x = y;
                y = rem;
            }

            return x;
        }

        public boolean isZero() {
            return num == 0;
        }

        public FractionalNum mul(int val) {
            return new FractionalNum(num * val, denom);
        }

        public FractionalNum sub(FractionalNum second) {
            return add(second.negate());
        }

        private FractionalNum negate() {
            return new FractionalNum(-num, denom);
        }

        public FractionalNum add(FractionalNum second) {

            if (second.isZero()) {
                return this;
            }

            if (isZero()) {
                return second;
            }

            int numRes = (num * second.denom + second.num * denom);
            int denomRes = denom * second.denom;

            return new FractionalNum(numRes, denomRes);
        }
    }

    record XYPoint(int x, int y) {

        private static final Comparator<XYPoint> X_THEN_Y_ASC =
                Comparator.comparingInt(XYPoint::x).thenComparingInt(XYPoint::y);

        @Override
        public String toString() {
            return "(%d; %d)".formatted(x, y);
        }
    }

    record Line(XYPoint p1, XYPoint p2) {

        public boolean hasPoint(XYPoint p) {

            if (p1.x() == p2.x()) {
                return p.x() == p1.x();
            }

            // (y1 - y2) / (x1 - x2)
            FractionalNum slope = new FractionalNum(p1.y() - p2.y(), p1.x() - p2.x());

            // slope * p.x() - slope * p1.x() + p1.y();
            FractionalNum first = slope.mul(p.x());

            FractionalNum second = slope.mul(p1.x());

            FractionalNum expectedY = first.sub(second).add(new FractionalNum(p1.y(), 1));

            return expectedY.equals(new FractionalNum(p.y(), 1));

            //            double slope = ((double) p1.y() - p2.y()) / ((double) p1.x() - p2.x());
            //            double expectedY = slope * p.x() - slope * p1.x() + p1.y();
            //            return Double.compare(expectedY, p.y()) == 0;
        }

        @Override
        public String toString() {
            return "%s - %s".formatted(p1, p2);
        }
    }
}
