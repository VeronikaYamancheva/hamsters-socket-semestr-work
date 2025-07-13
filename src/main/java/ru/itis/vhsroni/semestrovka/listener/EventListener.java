package ru.itis.vhsroni.semestrovka.listener;

import ru.itis.vhsroni.semestrovka.exception.listener.EventListenerException;
import ru.itis.vhsroni.semestrovka.protocol.Message;
import ru.itis.vhsroni.semestrovka.server.HamsterServer;

public interface EventListener {

    void init(HamsterServer server);

    void handle(Message message, int connectionId, int clientsCount) throws EventListenerException;

    int getType();
}
