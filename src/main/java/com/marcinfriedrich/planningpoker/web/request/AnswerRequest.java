package com.marcinfriedrich.planningpoker.web.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.marcinfriedrich.planningpoker.domain.Size;
import lombok.Data;

@Data
public class AnswerRequest {
    @JsonProperty
    private String roomKey;

    @JsonProperty
    private String userKey;

    @JsonProperty
    private Size size;
}
