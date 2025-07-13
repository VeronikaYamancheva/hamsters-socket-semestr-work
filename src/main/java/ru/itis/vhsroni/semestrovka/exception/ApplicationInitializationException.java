package ru.itis.vhsroni.semestrovka.exception;

import java.io.IOException;

public class ApplicationInitializationException extends RuntimeException {
    public ApplicationInitializationException(Throwable cause) {
        super(cause);
    }
}
