package ru.itis.vhsroni.semestrovka.exception.client;

public class ClientConnectionException extends RuntimeException{

    public ClientConnectionException(Throwable cause) {
        super("Exception during client connection: ", cause);
    }
}
