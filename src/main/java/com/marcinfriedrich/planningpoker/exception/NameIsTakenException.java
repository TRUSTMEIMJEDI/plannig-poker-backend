package com.marcinfriedrich.planningpoker.exception;

public class NameIsTakenException extends Exception {

    public NameIsTakenException() {
        super("Nazwa użytkownika jest już zajęta");
    }

}
