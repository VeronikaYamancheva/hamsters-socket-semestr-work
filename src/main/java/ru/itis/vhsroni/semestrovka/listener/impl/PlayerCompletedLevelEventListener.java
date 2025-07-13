package ru.itis.vhsroni.semestrovka.listener.impl;

import ru.itis.vhsroni.semestrovka.exception.listener.EventListenerException;
import ru.itis.vhsroni.semestrovka.exception.listener.EventListerIsNotInitException;
import ru.itis.vhsroni.semestrovka.listener.AbstractEventListener;
import ru.itis.vhsroni.semestrovka.protocol.Message;
import ru.itis.vhsroni.semestrovka.protocol.MessageType;
import ru.itis.vhsroni.semestrovka.utils.MessageCreator;

import java.nio.ByteBuffer;

public class PlayerCompletedLevelEventListener extends AbstractEventListener {

    private static int playersCount = 0;

    @Override
    public void handle(Message message, int connectionId, int clientsCount) throws EventListenerException {
        if (!isInit) {
            throw new EventListerIsNotInitException(PlayerJumpingEventListener.class.getName());
        }
        playersCount++;
        ByteBuffer buffer = ByteBuffer.allocate(INT_BYTES);
        buffer.putInt(connectionId);
        server.sendBroadCastMessage(MessageCreator.createMessage(MessageType.PLAYER_COMPLETED_LEVEL, buffer.array()));
        if (playersCount == server.getPlayersCount()) {
            server.sendBroadCastMessage(MessageCreator.createMessage(MessageType.ALL_PLAYERS_COMPLETED_LEVEL, new byte[0]));
            playersCount = 0;
        }
    }

    @Override
    public int getType() {
        return MessageType.PLAYER_COMPLETED_LEVEL;
    }
}
