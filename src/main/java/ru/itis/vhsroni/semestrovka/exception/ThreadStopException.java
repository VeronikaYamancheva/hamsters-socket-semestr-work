package ru.itis.vhsroni.semestrovka.exception;


public class ThreadStopException extends RuntimeException {

    public ThreadStopException(String message, Throwable cause) {
        super(message, cause);
    }

}
