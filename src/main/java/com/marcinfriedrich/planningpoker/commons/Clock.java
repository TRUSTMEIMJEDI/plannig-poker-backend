package com.marcinfriedrich.planningpoker.commons;

import java.time.Instant;

public interface Clock {

    Instant getCurrentDate();
}
