package ru.itis.vhsroni.semestrovka.listener.impl;

import ru.itis.vhsroni.semestrovka.exception.listener.EventListenerException;
import ru.itis.vhsroni.semestrovka.exception.listener.EventListerIsNotInitException;
import ru.itis.vhsroni.semestrovka.listener.AbstractEventListener;
import ru.itis.vhsroni.semestrovka.protocol.Message;
import ru.itis.vhsroni.semestrovka.protocol.MessageType;
import ru.itis.vhsroni.semestrovka.utils.MessageCreator;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class RoomNameInfoEventListener extends AbstractEventListener {

    @Override
    public void handle(Message message, int connectionId, int clientsCount) throws EventListenerException {
        if (!isInit) {
            throw new EventListerIsNotInitException(RoomNameInfoEventListener.class.getName());
        }
        byte[] roomName = server.getRoomName().getBytes(StandardCharsets.UTF_8);
        ByteBuffer buffer = ByteBuffer.allocate(roomName.length);
        buffer.put(roomName);
        server.sendMessage(connectionId, MessageCreator.createMessage(MessageType.ROOM_NAME_INFO_RESPONSE, buffer.array()));
    }

    @Override
    public int getType() {
        return MessageType.ROOM_NAME_INFO_REQUEST;
    }
}
