package ru.itis.vhsroni.semestrovka.utils;

import ru.itis.vhsroni.semestrovka.protocol.Message;
import ru.itis.vhsroni.semestrovka.protocol.MessageType;

public class MessageCreator {
    public static Message createMessage(int type, byte[] data) {
        if (!MessageType.getAllTypes().contains(type)) {
            throw new RuntimeException();
        }
        return new Message(type, data);
    }
}
