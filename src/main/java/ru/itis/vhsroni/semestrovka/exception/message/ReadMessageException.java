package ru.itis.vhsroni.semestrovka.exception.message;

public class ReadMessageException extends RuntimeException{


    public ReadMessageException(String message, Throwable cause) {
        super(message, cause);
    }

    public ReadMessageException(Throwable cause) {
        super(cause);
    }
}
