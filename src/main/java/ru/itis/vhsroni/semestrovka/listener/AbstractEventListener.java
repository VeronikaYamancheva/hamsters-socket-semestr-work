package ru.itis.vhsroni.semestrovka.listener;

import ru.itis.vhsroni.semestrovka.server.HamsterServer;

public abstract class AbstractEventListener implements EventListener {

    protected boolean isInit = false;

    protected HamsterServer server;

    public static final int INT_BYTES = 4;


    @Override
    public void init(HamsterServer server) {
        this.server = server;
        this.isInit = true;
    }
}
