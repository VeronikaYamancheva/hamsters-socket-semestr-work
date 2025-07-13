package ru.itis.vhsroni.semestrovka.exception.server;

import ru.itis.vhsroni.semestrovka.exception.ThreadStopException;

public class ServerThreadStopException extends ThreadStopException {

    public ServerThreadStopException(Throwable cause) {
        super("Exception during server `stop`:", cause);
    }
}
