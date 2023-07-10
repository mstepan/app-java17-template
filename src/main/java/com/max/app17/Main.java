package com.max.app17;

import java.math.BigInteger;
import java.util.stream.Stream;

public class Main {

    public static void main(String[] args) throws Exception {

        LargeFibonacciProvider fibProvider = new LargeFibonacciProvider();

        Stream.generate(fibProvider::next).
                limit(200_000).
                forEach(System.out::println);

        System.out.println("Main done...");
    }

    private static class LargeFibonacciProvider {
        BigInteger first = BigInteger.ZERO;
        BigInteger second = BigInteger.ONE;

        public BigInteger next(){
            BigInteger cur = first;

            first = second;

            second = second.add(cur);
            return cur;
        }
    }

    private static class IntFibonacciProvider {
        int first = 0;
        int second = 1;

        public int next() {
            int cur = first;

            if( cur < 0 ){
                throw new IllegalStateException("Fibonacci int overflow detected");
            }

            first = second;
            second += cur;
            return cur;
        }
    }

}
