package com.marcinfriedrich.planningpoker.payload;

public class ChangePasswordRequest {

    private String roomKey;
    private String userKey;
    private String newUsername;

    public String getRoomKey() {
        return roomKey;
    }

    public String getUserKey() {
        return userKey;
    }

    public String getNewUsername() {
        return newUsername;
    }
}
