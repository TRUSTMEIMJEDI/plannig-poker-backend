package com.marcinfriedrich.planningpoker;

import com.marcinfriedrich.planningpoker.commons.Clock;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@TestConfiguration
public class ClockTestConfig {

    @Bean
    FixedClock clock() {
        return new FixedClock();
    }

    public static class FixedClock implements Clock {

        private Instant instant;

        FixedClock() {
            this.instant = Instant.parse("2021-07-24T12:00:00.000Z");
        }

        @Override
        public Instant getCurrentDate() {
            return instant;
        }

        public void plus(long value, ChronoUnit unit) {
            this.instant = instant.plus(value, unit);
        }

        public void reset() {
            this.instant = Instant.parse("2021-07-24T12:00:00.000Z");
        }
    }
}
