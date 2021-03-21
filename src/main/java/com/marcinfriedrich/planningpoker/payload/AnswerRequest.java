package com.marcinfriedrich.planningpoker.payload;

import com.marcinfriedrich.planningpoker.enums.Size;

public class AnswerRequest {

    private String roomKey;
    private String userKey;
    private Size size;

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

    public Size getSize() {
        return size;
    }

    public void setSize(Size size) {
        this.size = size;
    }
}
