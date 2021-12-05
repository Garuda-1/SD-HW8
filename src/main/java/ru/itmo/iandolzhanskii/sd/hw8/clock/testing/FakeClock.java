package ru.itmo.iandolzhanskii.sd.hw8.clock.testing;

import ru.itmo.iandolzhanskii.sd.hw8.clock.Clock;

import java.time.Instant;

public class FakeClock implements Clock {

    private Instant now;

    public FakeClock(Instant now) {
        this.now = now;
    }

    public void setNow(Instant now) {
        this.now = now;
    }

    @Override
    public Instant now() {
        return now;
    }
}
