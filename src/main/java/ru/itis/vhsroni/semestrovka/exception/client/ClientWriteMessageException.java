package ru.itis.vhsroni.semestrovka.exception.client;

import ru.itis.vhsroni.semestrovka.exception.message.WriteMessageException;

public class ClientWriteMessageException extends WriteMessageException {

    public ClientWriteMessageException(Throwable cause) {
        super("Exception during write message by client: ", cause);
    }
}
