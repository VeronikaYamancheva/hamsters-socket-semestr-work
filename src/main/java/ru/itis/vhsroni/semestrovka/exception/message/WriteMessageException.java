package ru.itis.vhsroni.semestrovka.exception.message;

public class WriteMessageException extends RuntimeException{

    public WriteMessageException(String message, Throwable cause) {
        super(message, cause);
    }

    public WriteMessageException(Throwable cause) {
        super(cause);
    }
}
