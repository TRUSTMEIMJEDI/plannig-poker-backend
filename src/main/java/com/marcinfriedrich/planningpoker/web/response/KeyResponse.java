package com.marcinfriedrich.planningpoker.web.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.marcinfriedrich.planningpoker.domain.RoomType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class KeyResponse {

    @JsonProperty("roomKey")
    private String roomId;

    @JsonProperty("userKey")
    private String userId;

    @JsonProperty
    private String roomName;

    @JsonProperty
    private String userName;

    @JsonProperty
    private Boolean observer;

    @JsonProperty
    private RoomType roomType;
}
