package com.github.mstepan.app17.timer;

import org.jetbrains.annotations.Nullable;

import java.time.Instant;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicReferenceArray;

/**
 * Non-blocking Hashed Hierarchical Timing Wheels timer. Can store up to 24 hours of callback
 * events. The callback handling granularity is 1 second.
 */
public final class HashedHierarchicalTimingWheels {

    static final int HOURS_PER_DAY = 24;

    private static final int MINUTES_PER_HOUR = 60;

    private static final int SECONDS_PER_MINUTE = 60;

    // We should store hours buckets as a root of a wheel.
    private final AtomicReferenceArray<WheelBucket> hoursBuckets =
            new AtomicReferenceArray<>(HOURS_PER_DAY);

    public static HashedHierarchicalTimingWheels newInstance() {
        HashedHierarchicalTimingWheels inst = new HashedHierarchicalTimingWheels();

        startDaemonThread(TimeoutCallbackHandler.NAME, new TimeoutCallbackHandler(inst));
        startDaemonThread(BucketsGCHandler.NAME, new BucketsGCHandler(inst));

        return inst;
    }

    private static void startDaemonThread(String threadName, Runnable runnable) {
        Thread th = new Thread(runnable);
        th.setName(threadName);
        th.setDaemon(true);
        th.start();
    }

    /** Add callback that will be executed when timed out. */
    public void addCallback(Instant timeUtc, Runnable curCallback) {

        BucketsIndexes buckets = BucketsIndexes.of(timeUtc);

        WheelBucket callbacksBucket = getAndCreateBucketsIfNeeded(buckets);

        assert callbacksBucket.hasCallbacks();

        callbacksBucket.callbacks.add(curCallback);
    }

    WheelBucket getCallbacksBucket(BucketsIndexes bucketsIndexes) {
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

    private WheelBucket getAndCreateBucketsIfNeeded(BucketsIndexes bucketsIndexes) {
        WheelBucket minutesBucket = minutesBucket(bucketsIndexes.hour());
        WheelBucket secondsBucket = secondsBucket(minutesBucket, bucketsIndexes.minute());
        return callbacksBucket(secondsBucket, bucketsIndexes.second());
    }

    private WheelBucket minutesBucket(int hoursIdx) {

        WheelBucket minutesBucket = hoursBuckets.get(hoursIdx);

        if (minutesBucket == null) {
            minutesBucket = WheelBucket.withSize(MINUTES_PER_HOUR);
            if (hoursBuckets.compareAndSet(hoursIdx, null, minutesBucket)) {
                return minutesBucket;
            }
            return hoursBuckets.get(hoursIdx);
        }

        return minutesBucket;
    }

    private WheelBucket secondsBucket(WheelBucket minutesBucket, int minutesIdx) {

        WheelBucket secondsBucket = minutesBucket.children.get(minutesIdx);

        if (secondsBucket == null) {
            secondsBucket = WheelBucket.withSize(SECONDS_PER_MINUTE);
            if (minutesBucket.children.compareAndSet(minutesIdx, null, secondsBucket)) {
                return secondsBucket;
            }

            return minutesBucket.children.get(minutesIdx);
        }

        return secondsBucket;
    }

    private WheelBucket callbacksBucket(WheelBucket secondsBucket, int secondsIdx) {
        WheelBucket callbacksBucket = secondsBucket.children.get(secondsIdx);

        if (callbacksBucket == null) {
            callbacksBucket = WheelBucket.ofCallbacks();
            if (secondsBucket.children.compareAndSet(secondsIdx, null, callbacksBucket)) {
                return callbacksBucket;
            }
            return secondsBucket.children.get(secondsIdx);
        }

        return callbacksBucket;
    }

    void clearHourBucket(int hourIdx) {
        hoursBuckets.set(hourIdx, null);
    }

    static final class WheelBucket {

        @Nullable private final AtomicReferenceArray<WheelBucket> children;

        @Nullable private final Queue<Runnable> callbacks;

        public WheelBucket(
                @Nullable AtomicReferenceArray<WheelBucket> children,
                @Nullable Queue<Runnable> callbacks) {
            this.children = children;
            this.callbacks = callbacks;
        }

        static WheelBucket ofCallbacks() {
            return new WheelBucket(null, new ConcurrentLinkedQueue<>());
        }

        static WheelBucket withSize(int childrenCount) {
            return new WheelBucket(new AtomicReferenceArray<>(childrenCount), null);
        }

        boolean hasCallbacks() {
            return callbacks != null;
        }

        public Queue<Runnable> drainCallbacks() {
            assert callbacks != null;
            Queue<Runnable> copy = new ArrayDeque<>(callbacks);
            callbacks.clear();
            return copy;
        }
    }
}
