package com.marcinfriedrich.planningpoker.payload;

public class ChangeRoomTypeRequest {

    private String roomKey;
    private String roomType;

    public String getRoomKey() {
        return roomKey;
    }

    public String getRoomType() {
        return roomType;
    }
}
