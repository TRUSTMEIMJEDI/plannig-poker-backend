package com.marcinfriedrich.planningpoker.web.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.marcinfriedrich.planningpoker.domain.Size;
import lombok.Data;
import lombok.Value;

@Value
public class AnswerRequest {
    @JsonProperty
    String roomKey;

    @JsonProperty
    String userKey;

    @JsonProperty
    Size size;
}
