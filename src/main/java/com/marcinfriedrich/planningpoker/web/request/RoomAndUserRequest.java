package com.marcinfriedrich.planningpoker.web.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class RoomAndUserRequest {

    @JsonProperty
    private String roomKey;

    @JsonProperty
    private String userKey;
}
