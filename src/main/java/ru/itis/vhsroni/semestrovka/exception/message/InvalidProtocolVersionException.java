package ru.itis.vhsroni.semestrovka.exception.message;

public class InvalidProtocolVersionException extends RuntimeException {

    public InvalidProtocolVersionException() {
        super("Invalid protocol version");
    }
}
