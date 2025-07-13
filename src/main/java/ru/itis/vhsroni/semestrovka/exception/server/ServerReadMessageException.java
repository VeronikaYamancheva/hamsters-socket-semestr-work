package ru.itis.vhsroni.semestrovka.exception.server;

import ru.itis.vhsroni.semestrovka.exception.message.ReadMessageException;

public class ServerReadMessageException extends ReadMessageException {

    public ServerReadMessageException(Throwable cause) {
        super("Exception during reading message by server:", cause);
    }
}
