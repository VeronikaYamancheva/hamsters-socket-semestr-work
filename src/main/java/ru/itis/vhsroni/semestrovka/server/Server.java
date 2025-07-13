package ru.itis.vhsroni.semestrovka.server;

import ru.itis.vhsroni.semestrovka.protocol.Message;

public interface Server extends Runnable {
    void sendMessage(int connectionId, Message message);

    void sendBroadCastMessage(Message message);

    void closeServer();
}
