package com.marcinfriedrich.planningpoker;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.event.ContextClosedEvent;

import javax.annotation.Nonnull;

public class WireMockServerInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Override
    public void initialize(@Nonnull ConfigurableApplicationContext applicationContext) {
        final var wireMockConfig = new WireMockConfiguration()
                .dynamicPort();
        WireMockServer wireMockServer = new WireMockServer(wireMockConfig);
        wireMockServer.start();
        applicationContext.getBeanFactory().registerSingleton("wireMockServer", wireMockServer);
        applicationContext.addApplicationListener(applicationEvent -> {
            if (applicationEvent instanceof ContextClosedEvent) {
                wireMockServer.stop();
            }
        });
    }
}
