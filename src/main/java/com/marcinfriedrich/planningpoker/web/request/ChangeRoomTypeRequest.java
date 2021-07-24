package com.marcinfriedrich.planningpoker.web.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.marcinfriedrich.planningpoker.domain.RoomType;
import lombok.Data;

@Data
public class ChangeRoomTypeRequest {

    @JsonProperty
    private String roomKey;

    @JsonProperty
    private RoomType roomType;
}
