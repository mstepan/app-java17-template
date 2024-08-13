package com.github.mstepan.app17.timer;

import java.time.Instant;
import java.util.ArrayDeque;
import java.util.Objects;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicReferenceArray;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Non-blocking Hashed Hierarchical Timing Wheels timer. Can store up to 24 hours of callback
 * events. The callback handling granularity is 1 second.
 */
public final class HashedHierarchicalTimingWheels implements AutoCloseable {

    private Thread callbackHandlerThread;

    // We should store hours buckets as a root of a wheel.
    private final AtomicReferenceArray<WheelBucket> hoursBuckets =
            new AtomicReferenceArray<>(TimeConstants.HOURS_PER_DAY);

    public static HashedHierarchicalTimingWheels newInstance() {
        HashedHierarchicalTimingWheels inst = new HashedHierarchicalTimingWheels();

        inst.callbackHandlerThread = startCallbackHandlerDaemonThread(inst);

        return inst;
    }

    private static Thread startCallbackHandlerDaemonThread(HashedHierarchicalTimingWheels inst) {
        Thread th = new Thread(new TimeoutCallbackHandler(inst));
        th.setName(TimeoutCallbackHandler.NAME + "-" + System.identityHashCode(inst));
        th.setDaemon(true);
        th.start();
        return th;
    }

    @Override
    public void close() {
        callbackHandlerThread.interrupt();
    }

    /** Add callback that will be executed when timed out. */
    public void addCallback(@NotNull Instant timeUtc, @NotNull Runnable curCallback) {
        Objects.requireNonNull(timeUtc, "'timeUtc' instant is null");
        Objects.requireNonNull(curCallback, "'curCallback' is null");

        if (timeUtc.isBefore(Instant.now().minusSeconds(60))) {
            throw new IllegalArgumentException("Instant is in the past: %s".formatted(timeUtc));
        }

        BucketsIndexes buckets = BucketsIndexes.of(timeUtc);

        WheelBucket callbacksBucket = getAndCreateBucketsIfNeeded(buckets);

        assert callbacksBucket.isCallback();

        callbacksBucket.addCallback(curCallback);
    }

    WheelBucket getCallbacksBucket(BucketsIndexes bucketsIndexes) {
        final int hoursIdx = bucketsIndexes.hour();
        final int minutesIdx = bucketsIndexes.minute();
        final int secondIdx = bucketsIndexes.second();
        if (hoursBuckets.get(hoursIdx) == null
                || hoursBuckets.get(hoursIdx).getChild(minutesIdx) == null
                || hoursBuckets.get(hoursIdx).getChild(minutesIdx).getChild(secondIdx) == null) {
            return null;
        }

        return hoursBuckets.get(hoursIdx).getChild(minutesIdx).getChild(secondIdx);
    }

    private WheelBucket getAndCreateBucketsIfNeeded(BucketsIndexes bucketsIndexes) {
        WheelBucket minutesBucket = minutesBucket(bucketsIndexes.hour());
        WheelBucket secondsBucket = secondsBucket(minutesBucket, bucketsIndexes.minute());
        return callbacksBucket(secondsBucket, bucketsIndexes.second());
    }

    private WheelBucket minutesBucket(int hoursIdx) {

        WheelBucket minutesBucket = hoursBuckets.get(hoursIdx);

        if (minutesBucket == null) {
            minutesBucket = WheelBucket.timeBucket(TimeConstants.MINUTES_PER_HOUR);
            if (hoursBuckets.compareAndSet(hoursIdx, null, minutesBucket)) {
                return minutesBucket;
            }
            return hoursBuckets.get(hoursIdx);
        }

        return minutesBucket;
    }

    private WheelBucket secondsBucket(WheelBucket minutesBucket, int minutesIdx) {

        WheelBucket secondsBucket = minutesBucket.getChild(minutesIdx);

        if (secondsBucket == null) {
            secondsBucket = WheelBucket.timeBucket(TimeConstants.SECONDS_PER_MINUTE);
            if (minutesBucket.compareAndSetChild(minutesIdx, null, secondsBucket)) {
                return secondsBucket;
            }

            return minutesBucket.getChild(minutesIdx);
        }

        return secondsBucket;
    }

    private WheelBucket callbacksBucket(WheelBucket secondsBucket, int secondsIdx) {
        WheelBucket callbacksBucket = secondsBucket.getChild(secondsIdx);

        if (callbacksBucket == null) {
            callbacksBucket = WheelBucket.callbacksBucket();
            if (secondsBucket.compareAndSetChild(secondsIdx, null, callbacksBucket)) {
                return callbacksBucket;
            }
            return secondsBucket.getChild(secondsIdx);
        }

        return callbacksBucket;
    }

    void clearHourBucket(int hourIdx) {
        hoursBuckets.set(hourIdx, null);
    }

    private enum BucketType {
        CALLBACK,
        TIME
    }

    static final class WheelBucket {

        @NotNull private final BucketType type;

        @Nullable private final AtomicReferenceArray<WheelBucket> children;

        @Nullable private final Queue<Runnable> callbacks;

        private WheelBucket(
                @NotNull BucketType type,
                @Nullable AtomicReferenceArray<WheelBucket> children,
                @Nullable Queue<Runnable> callbacks) {
            this.type = type;
            this.children = children;
            this.callbacks = callbacks;
        }

        static WheelBucket callbacksBucket() {
            return new WheelBucket(BucketType.CALLBACK, null, new ConcurrentLinkedQueue<>());
        }

        static WheelBucket timeBucket(int size) {
            return new WheelBucket(BucketType.TIME, new AtomicReferenceArray<>(size), null);
        }

        WheelBucket getChild(int idx) {
            assert children != null;
            return children.get(idx);
        }

        boolean compareAndSetChild(int idx, WheelBucket expected, WheelBucket newValue) {
            assert children != null;
            return children.compareAndSet(idx, expected, newValue);
        }

        boolean isCallback() {
            return type == BucketType.CALLBACK;
        }

        public Queue<Runnable> drainCallbacks() {
            assert callbacks != null;
            Queue<Runnable> copy = new ArrayDeque<>(callbacks);
            callbacks.clear();
            return copy;
        }

        public void addCallback(Runnable curCallback) {
            assert callbacks != null;
            callbacks.add(curCallback);
        }
    }
}
