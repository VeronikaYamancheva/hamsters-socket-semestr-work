package ru.itis.vhsroni.semestrovka.server;

import ru.itis.vhsroni.semestrovka.listener.EventListener;

public interface GameServer extends Server{

    void registerListener(EventListener listener);

    int getPort();

    void endGame();
}
