package ru.itis.vhsroni.semestrovka.exception.message;

import ru.itis.vhsroni.semestrovka.protocol.MessageType;

public class MessageTypeException extends RuntimeException{
    public MessageTypeException(int type) {
        super("We can't process message with type: %s".formatted(type));
    }
}
