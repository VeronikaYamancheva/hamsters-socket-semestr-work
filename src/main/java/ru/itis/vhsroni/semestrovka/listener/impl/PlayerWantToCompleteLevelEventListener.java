package ru.itis.vhsroni.semestrovka.listener.impl;

import ru.itis.vhsroni.semestrovka.exception.listener.EventListenerException;
import ru.itis.vhsroni.semestrovka.exception.listener.EventListerIsNotInitException;
import ru.itis.vhsroni.semestrovka.listener.AbstractEventListener;
import ru.itis.vhsroni.semestrovka.protocol.Message;
import ru.itis.vhsroni.semestrovka.protocol.MessageType;
import ru.itis.vhsroni.semestrovka.utils.MessageCreator;

public class PlayerWantToCompleteLevelEventListener extends AbstractEventListener {

    @Override
    public void handle(Message message, int connectionId, int clientsCount) throws EventListenerException {
        if (!isInit) {
            throw new EventListerIsNotInitException(PlayerWantToCompleteLevelEventListener.class.getName());
        }
        server.sendBroadCastMessage(MessageCreator.createMessage(MessageType.PLAYER_WANT_TO_COMPLETE_LEVEL, new byte[0]));
    }

    @Override
    public int getType() {
        return MessageType.PLAYER_WANT_TO_COMPLETE_LEVEL;
    }
}
