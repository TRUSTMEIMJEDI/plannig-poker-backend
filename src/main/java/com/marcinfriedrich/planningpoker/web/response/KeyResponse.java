package com.marcinfriedrich.planningpoker.web.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.marcinfriedrich.planningpoker.domain.RoomType;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class KeyResponse {

    @JsonProperty("roomKey")
    String roomId;

    @JsonProperty("userKey")
    String userId;

    @JsonProperty
    String roomName;

    @JsonProperty
    String userName;

    @JsonProperty
    Boolean observer;

    @JsonProperty
    RoomType roomType;
}
