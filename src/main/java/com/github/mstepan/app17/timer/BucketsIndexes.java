package com.github.mstepan.app17.timer;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

record BucketsIndexes(int hour, int minute, int second) {

    static BucketsIndexes of(Instant timeUtc) {
        ZonedDateTime zoneDateTime = timeUtc.atZone(ZoneId.of("UTC"));

        return new BucketsIndexes(
                zoneDateTime.getHour(), zoneDateTime.getMinute(), zoneDateTime.getSecond());
    }
}
