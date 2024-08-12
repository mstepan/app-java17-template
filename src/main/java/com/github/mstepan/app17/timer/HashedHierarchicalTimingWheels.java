package com.github.mstepan.app17.timer;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public final class HashedHierarchicalTimingWheels {

    // We should store hours buckets as a root of a wheel.
    private final TimeBucket[] hoursBuckets = new TimeBucket[24];

    public static HashedHierarchicalTimingWheels newInstance() {
        HashedHierarchicalTimingWheels inst = new HashedHierarchicalTimingWheels();

        inst.startCallbackHandlerThread(inst);

        return inst;
    }

    private void startCallbackHandlerThread(HashedHierarchicalTimingWheels inst) {

        Thread thread = new Thread(new TimeoutCallbackHandler(inst));

        thread.setDaemon(true);

        thread.start();
    }

    public synchronized void addCallback(Instant timeUtc, Runnable curCallback) {

        TimeBucketsIndexes buckets = TimeBucketsIndexes.of(timeUtc);

        TimeBucket callbacksBucket = getAndCreateBucketsIfNeeded(buckets);

        assert callbacksBucket.isCallbacks();

        callbacksBucket.callbacks.add(curCallback);
    }

    TimeBucket getCallbacksBucket(TimeBucketsIndexes bucketsIndexes) {
        final int hoursIdx = bucketsIndexes.hour();
        final int minutesIdx = bucketsIndexes.minute();
        final int secondIdx = bucketsIndexes.second();
        if (hoursBuckets[hoursIdx] == null
                || hoursBuckets[hoursIdx].children[minutesIdx] == null
                || hoursBuckets[hoursIdx].children[minutesIdx].children[secondIdx] == null) {
            return null;
        }

        return hoursBuckets[hoursIdx].children[minutesIdx].children[secondIdx];
    }

    private TimeBucket getAndCreateBucketsIfNeeded(TimeBucketsIndexes bucketsIndexes) {

        final int hoursIdx = bucketsIndexes.hour();
        final int minutesIdx = bucketsIndexes.minute();
        final int secondIdx = bucketsIndexes.second();

        // no HOURS
        if (hoursBuckets[hoursIdx] == null) {
            TimeBucket minutesBucket = new TimeBucket("MINUTES", 60);
            hoursBuckets[hoursIdx] = minutesBucket;

            TimeBucket secondBucket = new TimeBucket("SECONDS", 60);
            minutesBucket.children[minutesIdx] = secondBucket;

            TimeBucket callbacksBucket = new TimeBucket("CALLBACKS", 0);
            secondBucket.children[secondIdx] = callbacksBucket;

            return callbacksBucket;
        }
        // no MINUTES
        else if (hoursBuckets[hoursIdx].children[minutesIdx] == null) {
            TimeBucket secondBucket = new TimeBucket("SECONDS", 60);
            hoursBuckets[hoursIdx].children[minutesIdx] = secondBucket;

            TimeBucket callbacksBucket = new TimeBucket("CALLBACKS", 0);
            secondBucket.children[secondIdx] = callbacksBucket;

            return callbacksBucket;
        }

        // no SECONDS
        else if (hoursBuckets[hoursIdx].children[minutesIdx].children[secondIdx] == null) {
            TimeBucket callbacksBucket = new TimeBucket("CALLBACKS", 0);
            hoursBuckets[hoursIdx].children[minutesIdx].children[secondIdx] = callbacksBucket;

            return callbacksBucket;
        }

        return hoursBuckets[hoursIdx].children[minutesIdx].children[secondIdx];
    }

    static final class TimeBucket {

        private final String name;

        private final TimeBucket[] children;

        final List<Runnable> callbacks;

        public TimeBucket(String name, int childrenCount) {
            this.name = name;
            children = new TimeBucket[childrenCount];
            callbacks = new ArrayList<>();
        }

        boolean isCallbacks() {
            return children.length == 0;
        }

        @Override
        public String toString() {
            return name;
        }
    }
}
