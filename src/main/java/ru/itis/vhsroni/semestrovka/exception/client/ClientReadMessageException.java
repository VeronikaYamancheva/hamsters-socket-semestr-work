package ru.itis.vhsroni.semestrovka.exception.client;

import ru.itis.vhsroni.semestrovka.exception.message.ReadMessageException;

public class ClientReadMessageException extends ReadMessageException {

    public ClientReadMessageException(Throwable cause) {
        super("Exception during readind message by client: ", cause);
    }
}
