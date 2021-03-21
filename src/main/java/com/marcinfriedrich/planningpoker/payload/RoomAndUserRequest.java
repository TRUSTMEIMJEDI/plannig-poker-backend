package com.marcinfriedrich.planningpoker.payload;

public class RoomAndUserRequest {

    private String roomKey;
    private String userKey;

    public String getRoomKey() {
        return roomKey;
    }

    public void setRoomKey(String roomKey) {
        this.roomKey = roomKey;
    }

    public String getUserKey() {
        return userKey;
    }

    public void setUserKey(String userKey) {
        this.userKey = userKey;
    }
}
