package com.marcinfriedrich.planningpoker.web.request;

import lombok.Data;

@Data
public class ChangePasswordRequest {

    private String roomKey;
    private String userKey;
    private String newUsername;
}
