package ru.itmo.iandolzhanskii.sd.hw8.statistic.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.itmo.iandolzhanskii.sd.hw8.clock.testing.FakeClock;
import ru.itmo.iandolzhanskii.sd.hw8.statistic.EventStatistic;

import java.time.Instant;

import static com.google.common.truth.Truth.assertThat;

class EventStatisticImplTest {

    private static final double EPS = 0.0001;

    private FakeClock clock;
    private Instant now;
    private EventStatistic eventStatistic;

    @BeforeEach
    public void setUp() {
        now = Instant.EPOCH;
        clock = new FakeClock(now);
        eventStatistic = new EventStatisticImpl(clock);
    }

    @Test
    public void oneEvent() {
        eventStatistic.incEvent("A");
        assertThat(eventStatistic.getEventStatisticByName("A")).isWithin(EPS).of(1 / 60D);
    }

    @Test
    public void manyEventsInMinute() {
        for (int i = 0; i < 60; i++) {
            eventStatistic.incEvent("A");
            advanceTimeInSeconds(1);
        }
        assertThat(eventStatistic.getEventStatisticByName("A")).isWithin(EPS).of(1D);
    }

    @Test
    public void everyMinuteEvents() {
        for (int i = 0; i < 60; i++) {
            eventStatistic.incEvent("A");
            advanceTimeInSeconds(60);
        }
        rollbackTimeInSeconds(1);
        assertThat(eventStatistic.getEventStatisticByName("A")).isWithin(EPS).of(1D);

        for (int i = 0; i < 30; i++) {
            eventStatistic.incEvent("A");
            advanceTimeInSeconds(60);
        }
        assertThat(eventStatistic.getEventStatisticByName("A")).isWithin(EPS).of(1D);
    }

    @Test
    public void forgetEvent() {
        for (int i = 0; i < 60; i++) {
            eventStatistic.incEvent("A");
        }
        advanceTimeInSeconds(60 * 60);
        assertThat(eventStatistic.getEventStatisticByName("A")).isWithin(EPS).of(0D);
    }

    @Test
    public void sparseEvents() {
        for (int i = 0; i < 3; i++) {
            eventStatistic.incEvent("A");
            advanceTimeInSeconds(60 * 3);
        }
        assertThat(eventStatistic.getEventStatisticByName("A")).isWithin(EPS).of(3 / 60D);
    }

    @Test
    public void sparseForgetEvents() {
        for (int i = 0; i < 3; i++) {
            eventStatistic.incEvent("A");
            advanceTimeInSeconds(60 * 40);
        }
        rollbackTimeInSeconds(60 * 40);
        assertThat(eventStatistic.getEventStatisticByName("A")).isWithin(EPS).of(2 / 60D);
    }

    @Test
    public void simultaneousEvents() {
        for (int i = 0; i < 60; i++) {
            eventStatistic.incEvent("A");
        }
        advanceTimeInSeconds(1);
        assertThat(eventStatistic.getEventStatisticByName("A")).isWithin(EPS).of(1D);
    }

    @Test
    public void multipleEvents() {
        int aEvents = 0;
        int bEvents = 0;
        int cEvents = 0;
        for (int i = 0; i < 60; i++) {
            eventStatistic.incEvent("A");
            aEvents++;
            if (i % 2 == 0) {
                eventStatistic.incEvent("B");
                bEvents++;
            }
            if (i % 3 == 0) {
                eventStatistic.incEvent("C");
                cEvents++;
            }
            advanceTimeInSeconds(1);
        }
        rollbackTimeInSeconds(1);
        assertThat(eventStatistic.getEventStatisticByName("A")).isWithin(EPS).of(aEvents / 60D);
        assertThat(eventStatistic.getEventStatisticByName("B")).isWithin(EPS).of(bEvents / 60D);
        assertThat(eventStatistic.getEventStatisticByName("C")).isWithin(EPS).of(cEvents / 60D);
        assertThat(eventStatistic.getAllEventStatistic()).isWithin(EPS).of((aEvents + bEvents + cEvents) / 3D / 60D);
    }

    private void advanceTimeInSeconds(long seconds) {
        now = now.plusSeconds(seconds);
        clock.setNow(now);
    }

    private void rollbackTimeInSeconds(long seconds) {
        now = now.minusSeconds(seconds);
        clock.setNow(now);
    }
}