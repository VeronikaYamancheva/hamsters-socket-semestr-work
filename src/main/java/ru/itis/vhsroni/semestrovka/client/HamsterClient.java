package ru.itis.vhsroni.semestrovka.client;

import ru.itis.vhsroni.semestrovka.controller.MessageController;
import ru.itis.vhsroni.semestrovka.exception.client.ClientReadMessageException;
import ru.itis.vhsroni.semestrovka.exception.client.ClientThreadStopException;
import ru.itis.vhsroni.semestrovka.exception.message.ReadMessageException;
import ru.itis.vhsroni.semestrovka.exception.client.ClientWriteMessageException;
import ru.itis.vhsroni.semestrovka.exception.message.WriteMessageException;
import ru.itis.vhsroni.semestrovka.exception.client.ClientConnectionException;
import ru.itis.vhsroni.semestrovka.protocol.Message;
import ru.itis.vhsroni.semestrovka.protocol.MessageProtocol;
import ru.itis.vhsroni.semestrovka.protocol.MessageType;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;

public class HamsterClient implements Client {

    private final String host;
    private final int port;
    private Socket socket;
    private ClientThread thread;
    private MessageController controller;
    private int playerId;


    public HamsterClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    @Override
    public void connect() {
        try {
            socket = new Socket(host, port);
            InputStream input = socket.getInputStream();
            OutputStream output = socket.getOutputStream();
            thread = new ClientThread(input, output, this);
            new Thread(thread).start();
        } catch (IOException e) {
            throw new ClientConnectionException(e);
        }
    }

    @Override
    public void sendMessage(Message message) {
        try {
            MessageProtocol.writeMessage(thread.getOut(), message);
        } catch (WriteMessageException e) {
            thread.stop();
            throw new ClientWriteMessageException(e);
        }
    }

    public ClientThread getThread() {
        return thread;
    }

    public MessageController getController() {
        return controller;
    }

    public void setController(MessageController controller) {
        this.controller = controller;
    }

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public static class ClientThread implements Runnable {

        private final InputStream in;
        private final OutputStream out;
        private final HamsterClient hamsterClient;
        private boolean alive;

        public ClientThread(InputStream in, OutputStream out, HamsterClient hamsterClient) {
            this.in = in;
            this.out = out;
            this.hamsterClient = hamsterClient;
            this.alive = true;
        }

        @Override
        public void run() {
            try {
                while (alive) {
                    Message message = MessageProtocol.readMessage(in);
                    if (message != null) {
                        if (message.getType() == MessageType.PLAYER_ID_ASSIGNED) {
                            ByteBuffer buffer = ByteBuffer.wrap(message.getData());
                            int playerId = buffer.getInt();
                            hamsterClient.setPlayerId(playerId);
                        } else if (hamsterClient.controller != null) {
                            hamsterClient.controller.receiveMessage(message);
                        }
                    }
                }
            } catch (ReadMessageException e) {
                throw new ClientReadMessageException(e);
            }
        }

        public OutputStream getOut() {
            return out;
        }

        public void stop() {
            try {
                in.close();
                out.close();
                hamsterClient.socket.close();
                alive = false;
            } catch (IOException e) {
                throw new ClientThreadStopException(e);
            }
        }
    }
}