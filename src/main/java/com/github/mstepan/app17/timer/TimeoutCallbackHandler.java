package com.github.mstepan.app17.timer;

import java.time.Instant;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

final class TimeoutCallbackHandler implements Runnable {

    private final HashedHierarchicalTimingWheels inst;

    TimeoutCallbackHandler(HashedHierarchicalTimingWheels inst) {
        this.inst = inst;
    }

    @Override
    public void run() {

        System.out.println("Timeout callback handler started");

        while (!Thread.currentThread().isInterrupted()) {
            try {

                Instant curTime = Instant.now();

                BucketsIndexes bucketsIndexes = BucketsIndexes.of(curTime);

                HashedHierarchicalTimingWheels.TimeBucket callbacksBucket =
                        inst.getCallbacksBucket(bucketsIndexes);

                if (callbacksBucket != null) {

                    Iterator<Runnable> callbacksIt = callbacksBucket.callbacks.iterator();

                    while (callbacksIt.hasNext()) {
                        Runnable singleCallback = callbacksIt.next();
                        try {
                            // we should carefully handle all exceptions that callback may throw
                            singleCallback.run();
                        } catch (Exception ex) {
                            System.err.printf("Callback failed with '%s'%n", ex.getMessage());
                        }
                        callbacksIt.remove();
                    }
                }

                TimeUnit.MILLISECONDS.sleep(500L);
            } catch (InterruptedException interEx) {
                break;
            }
        }

        System.out.println("Timeout callback handler completed");
    }
}
