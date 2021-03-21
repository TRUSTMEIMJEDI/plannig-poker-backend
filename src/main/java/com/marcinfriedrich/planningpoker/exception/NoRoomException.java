package com.marcinfriedrich.planningpoker.exception;

public class NoRoomException extends Exception {

    public NoRoomException() {
        super("Nie znaleziono pokoju dla podanego kodu");
    }

}
