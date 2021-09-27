package com.marcinfriedrich.planningpoker.web.request;

import lombok.Value;

@Value
public class ChangePasswordRequest {
    String roomKey;
    String userKey;
    String newUsername;
}
