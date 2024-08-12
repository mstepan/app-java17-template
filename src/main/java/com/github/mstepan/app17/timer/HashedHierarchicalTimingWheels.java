package com.github.mstepan.app17.timer;

import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicReferenceArray;

/** Non-blocking Hashed Hierarchical Timing Wheels timer. */
public final class HashedHierarchicalTimingWheels {

     static final int HOURS_PER_DAY = 24;

    private static final int MINUTES_PER_HOUR = 60;

    private static final int SECONDS_PER_MINUTE = 60;

    // We should store hours buckets as a root of a wheel.
    private final AtomicReferenceArray<TimeBucket> hoursBuckets =
            new AtomicReferenceArray<>(HOURS_PER_DAY);

    public static HashedHierarchicalTimingWheels newInstance() {
        HashedHierarchicalTimingWheels inst = new HashedHierarchicalTimingWheels();
        inst.startCallbackHandlerThread(inst);
        inst.startBucketsGCThread(inst);
        return inst;
    }

    private void startCallbackHandlerThread(HashedHierarchicalTimingWheels inst) {
        Thread thread = new Thread(new TimeoutCallbackHandler(inst));
        thread.setName("TimeoutCallbackHandler");
        thread.setDaemon(true);
        thread.start();
    }

    private void startBucketsGCThread(HashedHierarchicalTimingWheels inst) {
        Thread thread = new Thread(new BucketsGCHandler(inst));
        thread.setName("BucketsGCHandler");
        thread.setDaemon(true);
        thread.start();
    }

    public void addCallback(Instant timeUtc, Runnable curCallback) {

        BucketsIndexes buckets = BucketsIndexes.of(timeUtc);

        TimeBucket callbacksBucket = getAndCreateBucketsIfNeeded(buckets);

        assert callbacksBucket.isCallbacks();

        callbacksBucket.callbacks.add(curCallback);
    }

    TimeBucket getCallbacksBucket(BucketsIndexes bucketsIndexes) {
        final int hoursIdx = bucketsIndexes.hour();
        final int minutesIdx = bucketsIndexes.minute();
        final int secondIdx = bucketsIndexes.second();
        if (hoursBuckets.get(hoursIdx) == null
                || hoursBuckets.get(hoursIdx).children.get(minutesIdx) == null
                || hoursBuckets.get(hoursIdx).children.get(minutesIdx).children.get(secondIdx)
                        == null) {
            return null;
        }

        return hoursBuckets.get(hoursIdx).children.get(minutesIdx).children.get(secondIdx);
    }

    private TimeBucket getAndCreateBucketsIfNeeded(BucketsIndexes bucketsIndexes) {
        TimeBucket minutesBucket = minutesBucket(bucketsIndexes.hour());
        TimeBucket secondsBucket = secondsBucket(minutesBucket, bucketsIndexes.minute());
        return callbacksBucket(secondsBucket, bucketsIndexes.second());
    }

    private TimeBucket minutesBucket(int hoursIdx) {
        if (hoursBuckets.get(hoursIdx) == null) {
            TimeBucket minutesBucket = new TimeBucket("MINUTES", MINUTES_PER_HOUR);
            if (hoursBuckets.compareAndSet(hoursIdx, null, minutesBucket)) {
                return minutesBucket;
            }
        }

        return hoursBuckets.get(hoursIdx);
    }

    private TimeBucket secondsBucket(TimeBucket minutesBucket, int minutesIdx) {
        if (minutesBucket.children.get(minutesIdx) == null) {
            TimeBucket secondsBucket = new TimeBucket("SECONDS", SECONDS_PER_MINUTE);
            if (minutesBucket.children.compareAndSet(minutesIdx, null, secondsBucket)) {
                return secondsBucket;
            }
        }

        return minutesBucket.children.get(minutesIdx);
    }

    private TimeBucket callbacksBucket(TimeBucket secondsBucket, int secondsIdx) {
        if (secondsBucket.children.get(secondsIdx) == null) {
            TimeBucket callbacksBucket = new TimeBucket("CALLBACKS", 0);
            if (secondsBucket.children.compareAndSet(secondsIdx, null, callbacksBucket)) {
                return callbacksBucket;
            }
        }

        return secondsBucket.children.get(secondsIdx);
    }

    public void clearHourBucket(int hourIdx) {
        hoursBuckets.set(hourIdx, null);
    }

    static final class TimeBucket {

        private final String name;

        @NotNull
        private final AtomicReferenceArray<TimeBucket> children;

        @NotNull
        final Queue<Runnable> callbacks;

        public TimeBucket(String name, int childrenCount) {
            this.name = name;
            children = new AtomicReferenceArray<>(childrenCount);
            callbacks = new ConcurrentLinkedQueue<>();
        }

        boolean isCallbacks() {
            return children.length() == 0;
        }

        @Override
        public String toString() {
            return name;
        }
    }
}
