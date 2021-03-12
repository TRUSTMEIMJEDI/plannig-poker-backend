package com.marcinfriedrich.planningpoker.payload;

public class CreateRoomRequest {
    private String roomName;
    private String userName;

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
}
