package com.max.app17;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class Main {

    public static void main(String[] args) throws Exception {

        PrimeGeneratorProducer primeGeneratorProducer = new PrimeGeneratorProducer();
        Thread producerThread = new Thread(primeGeneratorProducer);

        PrimeGeneratorConsumer consumer = new PrimeGeneratorConsumer(primeGeneratorProducer);
        Thread consumerThread = new Thread(consumer);

        producerThread.start();
        consumerThread.start();

        try {
            TimeUnit.SECONDS.sleep(2L);
        } finally {
            producerThread.interrupt();
            consumerThread.interrupt();
        }

        System.out.println("Main done...");
    }

    private static class PrimeGeneratorProducer implements Runnable {

        private final BlockingQueue<BigInteger> primes = new ArrayBlockingQueue<>(100);

        @Override
        public void run() {
            BigInteger cur = BigInteger.ONE;

            try {
                while (!Thread.currentThread().isInterrupted()) {
                    cur = cur.nextProbablePrime();
                    try {
                        primes.put(cur);
                    } catch (InterruptedException interEx) {
                        System.out.println("Producer interrupted");
                        Thread.currentThread().interrupt();
                    }
                }
            } finally {
                System.out.println("Producer done");
            }
        }

        public BigInteger nextPrime() {
            return primes.poll();
        }
    }

    private static class PrimeGeneratorConsumer implements Runnable {

        private final PrimeGeneratorProducer primeProducer;

        public PrimeGeneratorConsumer(PrimeGeneratorProducer primeProducer) {
            this.primeProducer = primeProducer;
        }

        @Override
        public void run() {
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    System.out.println(primeProducer.nextPrime());
                    try {
                        TimeUnit.MILLISECONDS.sleep(100L);
                    } catch (InterruptedException interEx) {
                        System.out.println("Consumer interrupted");
                        Thread.currentThread().interrupt();
                    }
                }
            } finally {
                System.out.println("Consumer done");
            }
        }
    }
}
