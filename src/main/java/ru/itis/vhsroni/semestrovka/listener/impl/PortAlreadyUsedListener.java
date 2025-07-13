package ru.itis.vhsroni.semestrovka.listener.impl;

import ru.itis.vhsroni.semestrovka.exception.listener.EventListenerException;
import ru.itis.vhsroni.semestrovka.exception.listener.EventListerIsNotInitException;
import ru.itis.vhsroni.semestrovka.listener.AbstractEventListener;
import ru.itis.vhsroni.semestrovka.protocol.Message;
import ru.itis.vhsroni.semestrovka.protocol.MessageType;
import ru.itis.vhsroni.semestrovka.utils.MessageCreator;

import java.nio.ByteBuffer;

public class PortAlreadyUsedListener extends AbstractEventListener {

    @Override
    public void handle(Message message, int connectionId, int clientsCount) throws EventListenerException {
        if (!isInit) {
            throw new EventListerIsNotInitException(PortAlreadyUsedListener.class.getName());
        }
        byte[] data = message.getData();
        ByteBuffer buffer = ByteBuffer.allocate(data.length + INT_BYTES);
        buffer.putInt(connectionId);
        buffer.put(data);
        server.sendMessage(connectionId, MessageCreator.createMessage(MessageType.PORT_ALREADY_USED, buffer.array()));
    }

    @Override
    public int getType() {
        return MessageType.PORT_ALREADY_USED;
    }
}
