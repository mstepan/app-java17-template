package com.github.mstepan.app17;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Main {

    public static void main(String[] args) throws Exception {
        
        try (AutoCloseablePool pool1 = AutoCloseablePool.newInstance()) {
            try (AutoCloseablePool pool2 = AutoCloseablePool.newInstance()) {

                CompletableFuture<String> future1 =
                        CompletableFuture.supplyAsync(
                                () -> {
                                    RemoteCall remoteCall = new RemoteCall("hello");
                                    try {
                                        return remoteCall.call();
                                    } catch (Exception ex) {
                                        throw new RuntimeException(ex);
                                    }
                                },
                                pool1.originalPool);

                CompletableFuture<String> future2 =
                        CompletableFuture.supplyAsync(
                                () -> {
                                    RemoteCall remoteCall = new RemoteCall("world");
                                    try {
                                        return remoteCall.call();
                                    } catch (Exception ex) {
                                        throw new RuntimeException(ex);
                                    }
                                },
                                pool2.originalPool);

                String result =
                        CompletableFuture.allOf(future1, future2)
                                .thenApply(
                                        ignored ->
                                                "%s, %s!!!"
                                                        .formatted(future1.join(), future2.join()))
                                .join();

                System.out.printf("result: %s%n", result);
            }
        }

        System.out.println("Main done...");
    }

    private record RemoteCall(String message) implements Callable<String> {

        @Override
            public String call() throws Exception {

                System.out.printf("Remote call (%s) started%n", message);

                long randomDelayInMs = 1000 + ThreadLocalRandom.current().nextInt(10_000);

                TimeUnit.MILLISECONDS.sleep(randomDelayInMs);

                System.out.printf("Remote call (%s) completed%n", message);

                return message;
            }
        }

    private record AutoCloseablePool(ExecutorService originalPool) implements AutoCloseable {

        private static AutoCloseablePool newInstance() {
                BlockingQueue<Runnable> queue = new ArrayBlockingQueue<>(256);

                ThreadPoolExecutor executor =
                        new ThreadPoolExecutor(
                                2,
                                2,
                                60L,
                                TimeUnit.SECONDS,
                                queue,
                                new ThreadPoolExecutor.CallerRunsPolicy());

                executor.allowCoreThreadTimeOut(true);

                return new AutoCloseablePool(executor);
            }

            @Override
            public void close() throws Exception {
                originalPool.shutdownNow();
                System.out.printf("Pool with id %d closed%n", System.identityHashCode(originalPool));
            }
        }
}
