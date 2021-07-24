package com.marcinfriedrich.planningpoker.commons;

import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class DateTimeClock implements Clock {
    @Override
    public Instant getCurrentDate() {
        return Instant.now();
    }
}
