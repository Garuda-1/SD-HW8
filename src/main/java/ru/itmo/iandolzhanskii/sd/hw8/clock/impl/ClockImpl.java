package ru.itmo.iandolzhanskii.sd.hw8.clock.impl;

import ru.itmo.iandolzhanskii.sd.hw8.clock.Clock;

import java.time.Instant;

public class ClockImpl implements Clock {

    @Override
    public Instant now() {
        return Instant.now();
    }
}
