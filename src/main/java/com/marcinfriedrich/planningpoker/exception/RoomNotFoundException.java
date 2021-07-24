package com.marcinfriedrich.planningpoker.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class RoomNotFoundException extends RuntimeException {

    public RoomNotFoundException(String roomKey) {
        super(String.format("Room with key=%s not found", roomKey));
    }
}
