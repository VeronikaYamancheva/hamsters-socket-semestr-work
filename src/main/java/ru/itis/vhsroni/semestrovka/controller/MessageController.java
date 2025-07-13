package ru.itis.vhsroni.semestrovka.controller;

import ru.itis.vhsroni.semestrovka.protocol.Message;

public interface MessageController extends Controller{

    void receiveMessage(Message message);
}
