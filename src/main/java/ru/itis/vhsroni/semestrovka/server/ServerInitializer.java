package ru.itis.vhsroni.semestrovka.server;

import ru.itis.vhsroni.semestrovka.listener.EventListener;
import ru.itis.vhsroni.semestrovka.repository.EventListenersRepository;

public class ServerInitializer {

    public static HamsterServer createServer(int port, int playersCount) {
        HamsterServer hamsterServer = new HamsterServer(port, playersCount);
        for (EventListener listener : EventListenersRepository.getEventListeners()) {
            hamsterServer.registerListener(listener);
        }
        return hamsterServer;
    }

    public static void startServer(HamsterServer hamsterServer) {
        new Thread(hamsterServer).start();
    }

}
