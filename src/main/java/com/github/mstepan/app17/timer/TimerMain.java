package com.github.mstepan.app17.timer;

import java.time.Instant;
import java.util.concurrent.TimeUnit;

public class TimerMain {

    public static void main(String[] args) throws Exception {
        HashedHierarchicalTimingWheels timeWheels = HashedHierarchicalTimingWheels.newInstance();

        Instant now = Instant.now();

        for (int delay = 5; delay < 100; delay += 5) {
            final int delayInSec = delay;
            timeWheels.addCallback(
                    now.plusSeconds(delayInSec),
                    () -> System.out.printf("%d seconds%n", delayInSec));
        }

        TimeUnit.MINUTES.sleep(1);
    }
}
