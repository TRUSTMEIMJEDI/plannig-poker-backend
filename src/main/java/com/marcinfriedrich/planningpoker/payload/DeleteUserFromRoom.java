package com.marcinfriedrich.planningpoker.payload;

public class DeleteUserFromRoom extends RoomAndUserRequest {
    private String userNameToDelete;

    public String getUserNameToDelete() {
        return userNameToDelete;
    }

    public void setUserNameToDelete(String userNameToDelete) {
        this.userNameToDelete = userNameToDelete;
    }
}
