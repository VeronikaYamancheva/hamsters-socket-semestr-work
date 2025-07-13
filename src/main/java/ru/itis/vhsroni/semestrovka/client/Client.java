package ru.itis.vhsroni.semestrovka.client;

import ru.itis.vhsroni.semestrovka.protocol.Message;

public interface Client {

    void connect();

    void sendMessage(Message message);

    int getPlayerId();
}
