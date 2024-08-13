package com.github.mstepan.app17.timer;

import java.time.Instant;
import java.util.Queue;
import java.util.concurrent.TimeUnit;

final class TimeoutCallbackHandler implements Runnable {

    public static final String NAME = "TimeoutCallbackHandler";
    private final HashedHierarchicalTimingWheels inst;

    TimeoutCallbackHandler(HashedHierarchicalTimingWheels inst) {
        this.inst = inst;
    }

    @Override
    public void run() {

        System.out.printf("Timeout callback handler started for Timing Wheel %d%n", System.identityHashCode(inst));

        BucketsIndexes prevBuckets = BucketsIndexes.of(Instant.now());

        while (true) {
            try {

                Instant curTime = Instant.now();

                BucketsIndexes curBuckets = BucketsIndexes.of(curTime);

                HashedHierarchicalTimingWheels.WheelBucket callbacksBucket =
                        inst.getCallbacksBucket(curBuckets);

                if (callbacksBucket != null) {
                    Queue<Runnable> callbacksCopy = callbacksBucket.drainCallbacks();

                    for (Runnable singleCallback : callbacksCopy) {
                        try {
                            // we should carefully handle all exceptions that callback may throw
                            singleCallback.run();
                        } catch (Exception ex) {
                            System.err.printf("Callback failed with '%s'%n", ex.getMessage());
                        }
                    }
                }

                if (prevBuckets.hour() != curBuckets.hour()) {
                    System.out.printf("Cleaning HOUR bucket: %d%n", prevBuckets.hour());
                    inst.clearHourBucket(prevBuckets.hour());
                }

                prevBuckets = curBuckets;

                TimeUnit.MILLISECONDS.sleep(500L);
            } catch (InterruptedException interEx) {
                break;
            }
        }

        System.out.printf("Timeout callback handler completed for Timing Wheel %d%n", System.identityHashCode(inst));
    }
}
