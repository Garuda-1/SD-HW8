package ru.itmo.iandolzhanskii.sd.hw8.statistic.impl;

import java.time.Instant;
import java.util.ArrayDeque;
import java.util.Deque;

class HourToMinuteEventBuckets {

    private final Deque<Bucket> bucketQueue = new ArrayDeque<>();
    private final long totalMinutes = 60;

    public void incrementCount(Instant now) {
        if (!bucketQueue.isEmpty() && bucketQueue.getFirst().isInstantInBucket(now)) {
            bucketQueue.getFirst().incrementCount();
        } else {
            removeOutdatedBuckets(now);
            bucketQueue.addFirst(Bucket.minuteBucket(now));
            bucketQueue.getFirst().incrementCount();
        }
    }

    public double getRpm(Instant now) {
        removeOutdatedBuckets(now);
        return bucketQueue.stream()
                .mapToDouble(bucket -> (double) bucket.getCount() / bucket.getMinutes())
                .sum() / totalMinutes;
    }

    private void removeOutdatedBuckets(Instant now) {
        Instant lastStoredMinute = now.minusSeconds(totalMinutes * 60);
        while (!bucketQueue.isEmpty() && bucketQueue.getLast().isInstantInOrAfterBucket(lastStoredMinute)) {
            bucketQueue.removeLast();
        }
    }
}
