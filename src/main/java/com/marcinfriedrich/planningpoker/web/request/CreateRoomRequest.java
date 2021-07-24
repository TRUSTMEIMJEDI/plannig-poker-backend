package com.marcinfriedrich.planningpoker.web.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.marcinfriedrich.planningpoker.domain.RoomType;
import lombok.Data;

@Data
public class CreateRoomRequest {
    @JsonProperty
    private String roomName;

    @JsonProperty
    private String userName;

    @JsonProperty
    private RoomType roomType;
}
