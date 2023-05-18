package retry;

import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;

/**
 * java -XX:+UnlockDiagnosticVMOptions -XX:CompileCommand=print,Main.sum Main
 */
public class RetryCallableMain {

    public static void main(String[] args) {

        ExecutorService pool = Executors.newCachedThreadPool();

        try {
            Callable<Void> c1 = () -> {
                Random rand = ThreadLocalRandom.current();

                // 30/70
                if (rand.nextInt(100) < 30) {
                    return null;
                }
                else {
                    throw new IllegalStateException("Emulated exception");
                }
            };
            CompletableFuture<Void> cf1 = CompletableFuture.runAsync(() -> new RetryCallable<>(c1, 10, 5), pool);

            Callable<Void> c2 = () -> {
                Random rand = ThreadLocalRandom.current();

                // 30/70
                if (rand.nextInt(100) < 30) {
                    return null;
                }
                else {
                    throw new IllegalStateException("Emulated exception");
                }
            };
            CompletableFuture<Void> cf2 = CompletableFuture.runAsync(()-> new RetryCallable<>(c2, 10, 5).call(), pool);

            CompletableFuture.allOf(cf1, cf2).join();
        }
        finally {
            pool.shutdownNow();
        }

        System.out.println("Main done...");
    }


}

