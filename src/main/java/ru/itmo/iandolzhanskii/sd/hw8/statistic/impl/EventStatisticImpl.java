package ru.itmo.iandolzhanskii.sd.hw8.statistic.impl;

import ru.itmo.iandolzhanskii.sd.hw8.clock.Clock;
import ru.itmo.iandolzhanskii.sd.hw8.statistic.EventStatistic;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

public class EventStatisticImpl implements EventStatistic {

    private final Clock clock;
    private final Map<String, HourToMinuteEventBuckets> bucketQueues = new HashMap<>();

    public EventStatisticImpl(Clock clock) {
        this.clock = clock;
    }

    @Override
    public void incEvent(String name) {
        Instant now = clock.now();
        ensureBucketQueueExists(name);
        bucketQueues.get(name).incrementCount(now);
    }

    @Override
    public double getEventStatisticByName(String name) {
        Instant now = clock.now();
        ensureBucketQueueExists(name);
        return bucketQueues.get(name).getRpm(now);
    }

    @Override
    public double getAllEventStatistic() {
        Instant now = clock.now();
        return bucketQueues.values().stream()
                .mapToDouble(bucket -> bucket.getRpm(now))
                .average()
                .orElse(0);
    }

    @Override
    public void printStatistic() {
        if (bucketQueues.isEmpty()) {
            System.out.println("No events stored yet");
            return;
        }
        Instant now = clock.now();
        int maxEventNameLength = bucketQueues.keySet().stream().mapToInt(String::length).max().orElse(0);
        String format = "%-" + maxEventNameLength + "s | %-16s%n";

        System.out.println("Events statistic:");
        System.out.format(format, "Event name", "RPM");
        bucketQueues.keySet().stream()
                .sorted()
                .forEachOrdered(name -> System.out.format(format, name, bucketQueues.get(name).getRpm(now)));
        System.out.format("Global RPM: %f%n", getAllEventStatistic());
    }

    private void ensureBucketQueueExists(String name) {
        bucketQueues.putIfAbsent(name, new HourToMinuteEventBuckets());
    }

}
