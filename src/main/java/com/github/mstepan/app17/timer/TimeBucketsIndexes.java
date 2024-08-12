package com.github.mstepan.app17.timer;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public record TimeBucketsIndexes(int hour, int minute, int second) {

    public static TimeBucketsIndexes of(Instant timeUtc) {
        ZonedDateTime zoneDateTime = timeUtc.atZone(ZoneId.of("UTC"));

        return new TimeBucketsIndexes(
                zoneDateTime.getHour(), zoneDateTime.getMinute(), zoneDateTime.getSecond());
    }
}
