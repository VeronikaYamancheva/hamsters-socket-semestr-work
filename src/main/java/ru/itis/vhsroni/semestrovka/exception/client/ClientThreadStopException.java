package ru.itis.vhsroni.semestrovka.exception.client;

import ru.itis.vhsroni.semestrovka.exception.ThreadStopException;

public class ClientThreadStopException extends ThreadStopException {

    public ClientThreadStopException(Throwable cause) {
        super("Exception during client `stop`:", cause);
    }
}
