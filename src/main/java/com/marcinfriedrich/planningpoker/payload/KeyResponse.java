package com.marcinfriedrich.planningpoker.payload;

public class KeyResponse {

    private String roomKey;
    private String userKey;
    private String roomName;
    private String userName;
    private Boolean observer;

    public KeyResponse(String roomKey, String userKey, String roomName, String userName, Boolean observer) {
        this.roomKey = roomKey;
        this.userKey = userKey;
        this.roomName = roomName;
        this.userName = userName;
        this.observer = observer;
    }

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

    public Boolean getObserver() {
        return observer;
    }

    public void setObserver(Boolean isObserver) {
        this.observer = isObserver;
    }
}
