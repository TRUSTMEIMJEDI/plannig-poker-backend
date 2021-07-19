package com.marcinfriedrich.planningpoker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class PlanningPokerApplication {

    public static void main(String[] args) {
        SpringApplication.run(PlanningPokerApplication.class, args);
    }

}
