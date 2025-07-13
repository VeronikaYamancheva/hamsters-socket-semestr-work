package ru.itis.vhsroni.semestrovka.exception.listener;

import ru.itis.vhsroni.semestrovka.exception.listener.EventListenerException;

public class EventListerIsNotInitException extends EventListenerException {

    public EventListerIsNotInitException(String className) {
        super("EventListener `%s` isn't init exception".formatted(className));
    }
}
