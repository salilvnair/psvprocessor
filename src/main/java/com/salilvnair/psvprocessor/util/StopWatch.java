package com.salilvnair.psvprocessor.util;

import java.util.concurrent.TimeUnit;

public class StopWatch {
    private static long startTime;

    public static void start() {
        startTime = System.currentTimeMillis();
    }
    public static long stop() {
        long stopTime = System.currentTimeMillis();
        return stopTime - startTime;
    }

    public static long elapsed(TimeUnit timeUnit) {
        TimeUnit time = TimeUnit.MILLISECONDS;
        return timeUnit.convert(stop(), time);
    }
}
