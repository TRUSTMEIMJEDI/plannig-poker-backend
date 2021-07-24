package com.marcinfriedrich.planningpoker.exception;

public class NameIsTakenException extends RuntimeException {

    public NameIsTakenException() {
        super("Nazwa użytkownika jest już zajęta");
    }

}
