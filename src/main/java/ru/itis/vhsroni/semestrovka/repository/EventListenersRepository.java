package ru.itis.vhsroni.semestrovka.repository;

import ru.itis.vhsroni.semestrovka.listener.EventListener;
import ru.itis.vhsroni.semestrovka.listener.impl.*;

public class EventListenersRepository {

    private EventListenersRepository(){}

    public static EventListener[] getEventListeners() {
        return new EventListener[]{
                new SendClientsCountEventListener(),
                new SendClientsMaxCountEventListener(),
                new GameStartingEventListener(),
                new PlayerMovementEventListener(),
                new PlayerJumpingEventListener(),
                new CookieCollectingEventListener(),
                new AllCookiesCollectedEventListener(),
                new ScoreUpdateEventListener(),
                new PlayerCompletedLevelEventListener(),
                new PlayerWantToCompleteLevelEventListener(),
                new GameOverEventListener(),
                new NewLevelEventListener(),
                new RoomNameInfoEventListener(),
                new PlayerDisconnectedListener(),
                new PortAlreadyUsedListener()
        };
    }
}
