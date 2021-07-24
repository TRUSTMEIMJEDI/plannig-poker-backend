package com.marcinfriedrich.planningpoker.web.request;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class DeleteUserFromRoom extends RoomAndUserRequest {
    private String userNameToDelete;
}
