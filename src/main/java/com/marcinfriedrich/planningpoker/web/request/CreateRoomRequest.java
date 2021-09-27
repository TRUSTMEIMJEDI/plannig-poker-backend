package com.marcinfriedrich.planningpoker.web.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.marcinfriedrich.planningpoker.domain.RoomType;
import lombok.Value;

@Value
public class CreateRoomRequest {
    @JsonProperty
    String roomName;

    @JsonProperty
    String userName;

    @JsonProperty
    RoomType roomType;
}
