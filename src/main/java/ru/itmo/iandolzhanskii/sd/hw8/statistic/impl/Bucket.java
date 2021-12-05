package ru.itmo.iandolzhanskii.sd.hw8.statistic.impl;

import java.time.Instant;

class Bucket {

    private final long fromMillis;
    private final long toMillis;
    private long count = 0;

    public static Bucket minuteBucket(Instant instant) {
        long fromMillis = (instant.toEpochMilli() / 1000 / 60) * 1000 * 60;
        long toMillis = fromMillis + 1000 * 60;
        return new Bucket(fromMillis, toMillis);
    }

    public boolean isInstantInBucket(Instant instant) {
        long millis = instant.toEpochMilli();
        return fromMillis <= millis && millis < toMillis;
    }

    public boolean isInstantInOrAfterBucket(Instant instant) {
        long millis = instant.toEpochMilli();
        return millis >= toMillis || isInstantInBucket(instant);
    }

    public double getMinutes() {
        return (double) (toMillis - fromMillis) / 1000 / 60;
    }

    public void incrementCount() {
        count++;
    }

    public long getCount() {
        return count;
    }

    private Bucket(long from, long to) {
        this.fromMillis = from;
        this.toMillis = to;
    }
}
