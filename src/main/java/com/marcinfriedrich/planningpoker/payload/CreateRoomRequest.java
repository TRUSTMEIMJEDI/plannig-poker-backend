package com.marcinfriedrich.planningpoker.payload;

import com.marcinfriedrich.planningpoker.enums.RoomType;

public class CreateRoomRequest {

    private String roomName;
    private String userName;
    private RoomType roomType;

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public RoomType getRoomType() {
        return roomType;
    }

    public void setRoomType(RoomType roomType) {
        this.roomType = roomType;
    }
}
