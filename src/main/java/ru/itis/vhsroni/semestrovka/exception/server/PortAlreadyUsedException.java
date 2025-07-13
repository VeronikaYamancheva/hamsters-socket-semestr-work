package ru.itis.vhsroni.semestrovka.exception.server;

import ru.itis.vhsroni.semestrovka.exception.message.ReadMessageException;

import java.net.SocketException;

public class PortAlreadyUsedException extends SocketException {

    public PortAlreadyUsedException(int port) {
        super("Exception during creating server with used port: %s".formatted(port));
    }
}
