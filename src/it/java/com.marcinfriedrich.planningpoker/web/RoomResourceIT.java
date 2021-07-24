package com.marcinfriedrich.planningpoker.web;

import com.marcinfriedrich.planningpoker.IntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureMockMvc
@DisplayName("RoomResource tests")
public class RoomResourceIT extends IntegrationTest {

    private final MockMvc mockMvc;

    public RoomResourceIT(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }
}
