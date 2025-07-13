package ru.itis.vhsroni.semestrovka.listener.impl;

import ru.itis.vhsroni.semestrovka.exception.listener.EventListenerException;
import ru.itis.vhsroni.semestrovka.exception.listener.EventListerIsNotInitException;
import ru.itis.vhsroni.semestrovka.listener.AbstractEventListener;
import ru.itis.vhsroni.semestrovka.utils.MessageCreator;
import ru.itis.vhsroni.semestrovka.protocol.Message;
import ru.itis.vhsroni.semestrovka.protocol.MessageType;

import java.nio.ByteBuffer;

public class SendClientsCountEventListener extends AbstractEventListener {

    @Override
    public void handle(Message message, int connectionId, int clientsCount) throws EventListenerException {
        if (!isInit) {
            throw new EventListerIsNotInitException(SendClientsCountEventListener.class.getName());
        }
        ByteBuffer buffer = ByteBuffer.allocate(INT_BYTES).putInt(clientsCount);
        Message response = MessageCreator.createMessage(MessageType.CLIENT_TEMP_COUNT_INFO_RESPONSE,
                buffer.array());
        server.sendBroadCastMessage(response);

    }

    @Override
    public int getType() {
        return MessageType.CLIENT_TEMP_COUNT_INFO_REQUEST;
    }
}
