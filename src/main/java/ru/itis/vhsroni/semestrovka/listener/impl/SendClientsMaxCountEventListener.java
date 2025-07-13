package ru.itis.vhsroni.semestrovka.listener.impl;

import ru.itis.vhsroni.semestrovka.exception.listener.EventListenerException;
import ru.itis.vhsroni.semestrovka.exception.listener.EventListerIsNotInitException;
import ru.itis.vhsroni.semestrovka.listener.AbstractEventListener;
import ru.itis.vhsroni.semestrovka.protocol.Message;
import ru.itis.vhsroni.semestrovka.protocol.MessageType;
import ru.itis.vhsroni.semestrovka.utils.MessageCreator;

import java.nio.ByteBuffer;

public class SendClientsMaxCountEventListener extends AbstractEventListener {

    @Override
    public void handle(Message message, int connectionId, int clientsCount) throws EventListenerException {
        if (!isInit) {
            throw new EventListerIsNotInitException(SendClientsMaxCountEventListener.class.getName());
        }
        ByteBuffer buffer = ByteBuffer.allocate(INT_BYTES).putInt(server.getPlayersCount());
        Message response = MessageCreator.createMessage(MessageType.CLIENT_MAX_COUNT_INFO_RESPONSE,
                buffer.array());
        server.sendBroadCastMessage(response);
    }

    @Override
    public int getType() {
        return MessageType.CLIENT_MAX_COUNT_INFO_REQUEST;
    }
}
