package com.github.mstepan.app17.concurrency.locks;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.github.mstepan.app17.numbers.IntCounter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Stream;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;

public class GenericLockTest {

    private static final class LocksArgumentsProvider implements ArgumentsProvider {

        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
            return Stream.of(
                    Arguments.of(new SpinLock()),
                    Arguments.of(new BackoffLock()),
                    Arguments.of(new ArrayLock()),
                    Arguments.of(new LinkedNodeLock()),
                    Arguments.of(new QueueLock()));
        }
    }

    @ParameterizedTest
    @ArgumentsSource(LocksArgumentsProvider.class)
    void criticalSection(Lock mutex) throws Exception {
        final int threadsCount = 40;
        final int iterationsCount = 100;
        final IntCounter counter1 = new IntCounter();
        final IntCounter counter2 = new IntCounter();

        List<Callable<Void>> tasks = new ArrayList<>();

        for (int i = 0; i < threadsCount; ++i) {
            tasks.add(
                    () -> {
                        for (int it = 0;
                                it < iterationsCount && !Thread.currentThread().isInterrupted();
                                ++it) {

                            mutex.lock();

                            try {
                                counter1.inc();
                                counter2.inc();
                            } finally {
                                mutex.unlock();
                            }
                        }
                        return null;
                    });
        }

        ExecutorService pool = Executors.newCachedThreadPool();

        List<Future<Void>> futures = pool.invokeAll(tasks);

        for (Future<Void> singleFuture : futures) {
            singleFuture.get();
        }

        pool.shutdownNow();

        assertEquals(counter1.value(), counter2.value());
        assertEquals(threadsCount * iterationsCount, counter1.value());
    }
}
