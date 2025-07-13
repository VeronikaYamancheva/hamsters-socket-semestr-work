package ru.itis.vhsroni.semestrovka.exception.server;

import ru.itis.vhsroni.semestrovka.exception.message.WriteMessageException;

public class ServerWriteMessageException extends WriteMessageException {

    public ServerWriteMessageException(Throwable cause) {
        super("Exception during write message by server:", cause);
    }
}
