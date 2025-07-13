package ru.itis.vhsroni.semestrovka.listener.impl;

import ru.itis.vhsroni.semestrovka.exception.listener.EventListenerException;
import ru.itis.vhsroni.semestrovka.exception.listener.EventListerIsNotInitException;
import ru.itis.vhsroni.semestrovka.listener.AbstractEventListener;
import ru.itis.vhsroni.semestrovka.protocol.Message;
import ru.itis.vhsroni.semestrovka.protocol.MessageType;
import ru.itis.vhsroni.semestrovka.utils.MessageCreator;

import java.nio.ByteBuffer;

public class GameStartingEventListener extends AbstractEventListener {

    @Override
    public void handle(Message message, int connectionId, int clientsCount) throws EventListenerException {
        if (!isInit) {
            throw new EventListerIsNotInitException(GameStartingEventListener.class.getName());
        }
        ByteBuffer buffer = ByteBuffer.allocate(INT_BYTES);
        server.sendBroadCastMessage(MessageCreator.createMessage(MessageType.GAME_WAS_STARTED_RESPONSE, buffer.array()));
    }

    @Override
    public int getType() {
        return MessageType.GAME_WAS_STARTED_REQUEST;
    }
}
