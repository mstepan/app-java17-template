package com.github.mstepan.app17;

import java.util.Arrays;
import java.util.Random;

public class PhiAccrualFailureDetectorMain {

    public static void main(String[] args) throws Exception {


        PhiIntegral integralDx = new PhiIntegral(5.0, 0.1);

        double dxValue = integralDx.solve(4.4, 1_000_000, 1_000_000);

        // expected: 0.2506628272157992
        // actual: 0.4044549383072562
        System.out.println(dxValue);

        // 2.9 => 0.0027052313961436346
        // 3.0 => 0.3010299956639811
        // 3.1 => 2.206931805795301
        // 3.2 => 6.5426456723906545
        // 3.3 => 13.49608793935259
        // 3.4 => 23.118053405486076
        // 3.5 => 35.42799270218103
        // 3.6 => 50.43521961427551
        // 3.7 => Infinity
        // 3.9 => Infinity

        // [20..120] ms
        double[] samplesInMs = new double[100];
        Random rand = new Random();

        for (int i = 0; i < samplesInMs.length; ++i) {
            samplesInMs[i] = 20 + rand.nextInt(100);
        }

        System.out.printf("max value: %.2f%n", Arrays.stream(samplesInMs).max().getAsDouble());

        double m = mean(samplesInMs);
        double v = variance(m, samplesInMs);
        double delta = 300;

        System.out.printf("mean: %.2f%n", m);
        System.out.printf("variance: %.2f%n", v);

        PhiIntegral phiIntegral = new PhiIntegral(m, v);

        double dx = phiIntegral.solve(delta, 1_000_000, 1_000_000);

        double pLater = (1.0 / (v * Math.sqrt(2.0 * Math.PI))) * dx;

        double phi = -Math.log10(pLater);

        System.out.printf("delta: %.2f, phi: %.3f %% %n", delta, phi);

        System.out.println("PhiAccrualFailureDetectorMain done...");
    }

    private static double variance(double mean, double[] samples) {

        double sumSquared = 0.0;

        for (double val : samples) {
            double diff = val - mean;
            sumSquared += (diff * diff);
        }

        return Math.sqrt(sumSquared / samples.length);
    }

    private static double mean(double[] samples) {
        double sum = 0.0;

        for (double val : samples) {
            sum += val;
        }

        return sum / samples.length;
    }

    // ==============================================

    /** https://en.wikipedia.org/wiki/Simpson%27s_rule */
    static final class PhiIntegral {

        private final double m;

        private final double v;

        public PhiIntegral(double m, double v) {
            this.m = m;
            this.v = v;
        }

        // Define the function to integrate
        double funcForIntegral(double x) {
            double power = -(POW2(x - m) / (2 * POW2(v)));
            return Math.pow(Math.E, power);
        }

        // Simpson's method for integral calculus
        // a = lower bound
        // b = upper bound of integration
        // n = number of passes (higher = less margin of error, but takes longer)
        double solve(double lower, double upper, int itCount) {
            int i, z;
            double h, s;

            itCount = itCount + itCount;
            s = funcForIntegral(lower) * funcForIntegral(upper);
            h = (upper - lower) / itCount;
            z = 4;

            for (i = 1; i < itCount; i++) {
                s = s + z * funcForIntegral(lower + i * h);
                z = 6 - z;
            }
            return (s * h) / 3;
        }
    }

    // ========================================== =======

    private static final double SQRT_2 = Math.sqrt(2.0);

    private static final double SQRT_PI = Math.sqrt(Math.PI);

    private static double dx(double a, double m, double v) {
        double temp = (m - a) / (SQRT_2 * v);

        return (SQRT_PI * (erf(temp) + 1.0) * v) / SQRT_2;
    }

    /**
     * Gaussian error function, calculated as a approximation.
     * https://en.wikipedia.org/wiki/Error_function
     */
    private static double erf(double x) {
        double a1 = 0.278393;
        double a2 = 0.230389;
        double a3 = 0.000972;
        double a4 = 0.078108;

        double denominator = 1.0 + a1 * x + a2 * POW2(x) + a3 * POW3(x) + a4 * POW4(x);

        return 1.0 - (1.0 / POW4(denominator));
    }

    private static double POW2(double x) {
        return Math.pow(x, 2.0);
    }

    private static double POW3(double x) {
        return Math.pow(x, 3.0);
    }

    private static double POW4(double x) {
        return Math.pow(x, 4.0);
    }
}
