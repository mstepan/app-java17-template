package com.github.mstepan.app17.timer;

import java.time.Instant;
import java.util.concurrent.TimeUnit;

/** Wake up every hour and nullify previous HOUR bucket. */
final class BucketsGCHandler implements Runnable {

    private final HashedHierarchicalTimingWheels inst;

    public BucketsGCHandler(HashedHierarchicalTimingWheels inst) {
        this.inst = inst;
    }

    @Override
    public void run() {
        System.out.println("Buckets GC thread started");

        while (!Thread.currentThread().isInterrupted()) {
            try {
                TimeUnit.HOURS.sleep(1L);

                Instant curTime = Instant.now();

                BucketsIndexes bucketsIndexes = BucketsIndexes.of(curTime);

                int hourIdx = normalizeBucketIdx(bucketsIndexes.hour() - 1);

                inst.clearHourBucket(hourIdx);
            } catch (InterruptedException interEx) {
                break;
            }
        }

        System.out.println("BucketsGCHandler thread completed");
    }

    /** Negative values converted to positive. */
    private int normalizeBucketIdx(int bucketIdx) {
        if (bucketIdx < 0) {
            return bucketIdx + HashedHierarchicalTimingWheels.HOURS_PER_DAY;
        }

        return bucketIdx;
    }
}
