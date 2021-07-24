package com.marcinfriedrich.planningpoker;

import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest(classes = {PlanningPokerApplication.class},
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(initializers = {WireMockServerInitializer.class})
@AutoConfigureTestEntityManager
@ActiveProfiles("it")
public abstract class IntegrationTest {
}

