package com.github.mstepan.app17;

public class PhiAccrualFailureDetectorMain {

    public static void main(String[] args) throws Exception {

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

        double delta = 3.3; // T_now - T_last
        double m = 3.0; // mean
        double v = 0.04; // variance (https://en.wikibooks.org/wiki/Statistics/Summary/Variance)

        double dx = 3.199351715441227E-15;
        //        double dx = dx(delta, m, v);

        double pLater = (1.0 / (v * Math.sqrt(2.0 * Math.PI))) * dx;

        double phi = -Math.log10(pLater);

        System.out.printf("phi: %.3f %% %n", phi);

        System.out.println("PhiAccrualFailureDetectorMain done...");
    }

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
