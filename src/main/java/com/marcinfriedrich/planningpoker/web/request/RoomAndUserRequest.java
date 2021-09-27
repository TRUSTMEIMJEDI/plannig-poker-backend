package com.marcinfriedrich.planningpoker.web.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

@Value
public class RoomAndUserRequest {

    @JsonProperty
    String roomKey;

    @JsonProperty
    String userKey;
}
