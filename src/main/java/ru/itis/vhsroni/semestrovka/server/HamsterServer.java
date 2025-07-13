package ru.itis.vhsroni.semestrovka.server;

import javafx.application.Platform;
import ru.itis.vhsroni.semestrovka.exception.listener.EventListenerException;
import ru.itis.vhsroni.semestrovka.exception.server.HostException;
import ru.itis.vhsroni.semestrovka.exception.message.ReadMessageException;
import ru.itis.vhsroni.semestrovka.exception.message.WriteMessageException;
import ru.itis.vhsroni.semestrovka.exception.server.ServerException;
import ru.itis.vhsroni.semestrovka.exception.server.ServerReadMessageException;
import ru.itis.vhsroni.semestrovka.exception.server.ServerThreadStopException;
import ru.itis.vhsroni.semestrovka.exception.server.ServerWriteMessageException;
import ru.itis.vhsroni.semestrovka.listener.EventListener;
import ru.itis.vhsroni.semestrovka.protocol.Message;
import ru.itis.vhsroni.semestrovka.protocol.MessageProtocol;
import ru.itis.vhsroni.semestrovka.protocol.MessageType;
import ru.itis.vhsroni.semestrovka.settings.FXMLConstants;
import ru.itis.vhsroni.semestrovka.utils.FXMLVisualizer;
import ru.itis.vhsroni.semestrovka.utils.MessageCreator;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.*;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class HamsterServer implements GameServer {

    private ServerSocket serverSocket;
    private final int port;
    private final int playersCount;
    private final List<Client> clients;
    private final List<EventListener> listeners;
    private final List<Thread> updaterThreads;
    private boolean isGameStarted;
    private boolean isAlive;
    private String roomName;

    public HamsterServer(int port, int playersCount) {
        this.port = port;
        this.playersCount = playersCount;
        this.clients = new CopyOnWriteArrayList<>();
        this.listeners = new CopyOnWriteArrayList<>();
        this.updaterThreads = new CopyOnWriteArrayList<>();
        this.isGameStarted = false;
        this.isAlive = true;

        Runtime.getRuntime().addShutdownHook(new Thread(this::closeServer));
    }

    @Override
    public void registerListener(EventListener listener) {
        listener.init(this);
        listeners.add(listener);
    }

    @Override
    public int getPort() {
        return port;
    }

    @Override
    public void endGame() {
        for (Thread updaterThread : updaterThreads) {
            updaterThread.interrupt();
        }
        updaterThreads.clear();
    }

    @Override
    public void sendMessage(int connectionId, Message message) {
        Client client = clients.get(connectionId);
        try {
            MessageProtocol.writeMessage(client.getOutput(), message);
        } catch (WriteMessageException e) {
            client.stop();
            throw new ServerWriteMessageException(e);
        }
    }

    @Override
    public void sendBroadCastMessage(Message message) {
        for (Client c : clients) {
            if (c.alive) {
                try {
                    MessageProtocol.writeMessage(c.getOutput(), message);
                } catch (WriteMessageException e) {
                    c.stop();
                }
            }
        }
    }

    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket();
            serverSocket.setReuseAddress(true);
            serverSocket.bind(new InetSocketAddress(port));

            while (isAlive) {
                try {
                    Socket clientSocket = serverSocket.accept();
                    InputStream input = clientSocket.getInputStream();
                    OutputStream output = clientSocket.getOutputStream();
                    Client client = new Client(input, output, this, clientSocket);
                    clients.add(client);
                    new Thread(client).start();
                } catch (SocketException e) {
                    if (!isAlive) {
                        break;
                    }
                    throw e;
                }
                if (!isGameStarted && clients.size() == playersCount) {
                    isGameStarted = true;
                }
            }
        } catch (BindException e) {
            portAlreadyUsed();
        } catch (UnknownHostException e) {
            throw new HostException(e);
        } catch (SocketException e) {
            if (!isAlive) {
                return;
            }
            throw new ServerException(e);
        } catch (ServerWriteMessageException e) {
            for (Client client : clients) {
                if (client.alive) client.stop();
            }
            closeServer();
        } catch (IOException e) {
            throw new ServerException(e);
        } finally {
            closeServer();
        }
    }

    private void portAlreadyUsed() {
        FXMLVisualizer visualizer = new FXMLVisualizer();
        Platform.runLater(() -> visualizer.show(FXMLConstants.START_SCREEN_NAME));
    }

    @Override
    public void closeServer() {
        try {
            endGame();
            isAlive = false;
            for (Client client : clients) {
                client.stop();
            }
            clients.clear();
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
            serverSocket = null;
        } catch (IOException e) {
            throw new ServerThreadStopException(e);
        }
    }

    private void clientDisconnected(Client disconnectedClient) {
        ByteBuffer buffer = ByteBuffer.allocate(4);
        buffer.putInt(disconnectedClient.getId());
        clients.remove(disconnectedClient);
        sendBroadCastMessage(MessageCreator.createMessage(MessageType.PLAYER_DISCONNECTED, buffer.array()));
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public int getPlayersCount() {
        return playersCount;
    }

    static class Client implements Runnable {
        private final InputStream input;
        private final OutputStream output;
        private final HamsterServer server;
        private final Socket clientSocket;
        private boolean alive = true;
        private final int id;

        public Client(InputStream input, OutputStream output, HamsterServer server, Socket clientSocket) {
            this.input = input;
            this.output = output;
            this.server = server;
            this.clientSocket = clientSocket;
            this.id = server.clients.size();

            sendPlayerId();
        }

        private void sendPlayerId() {
            try {
                ByteBuffer buffer = ByteBuffer.allocate(4);
                buffer.putInt(id);
                Message message = new Message(MessageType.PLAYER_ID_ASSIGNED, buffer.array());
                MessageProtocol.writeMessage(output, message);
            } catch (WriteMessageException e) {
                throw new ServerWriteMessageException(e);
            }
        }

        @Override
        public void run() {
            try {
                while (alive) {
                    Message message = MessageProtocol.readMessage(input);
                    if (message != null) {
                        for (EventListener eventListener : server.listeners) {
                            if (message.getType() == eventListener.getType()) {
                                eventListener.handle(message, id, server.clients.size());
                                break;
                            }
                        }
                    }
                }
            } catch (ReadMessageException e) {
                if (!alive) {
                    return;
                }
                throw new ServerReadMessageException(e);
            } catch (ServerWriteMessageException e) {
                for (Client client : server.clients) {
                    if (client.alive) client.stop();
                }
                server.closeServer();
            } catch (EventListenerException e) {
                throw new ServerException(e);
            } finally {
                stop();
            }
        }

        public void stop() {
            try {
                input.close();
                output.close();
                clientSocket.close();
                alive = false;
                server.clientDisconnected(this);
            } catch (IOException e) {
                throw new ServerThreadStopException(e);
            }
        }

        public OutputStream getOutput() {
            return output;
        }

        public HamsterServer getServer() {
            return server;
        }

        public int getId() {
            return id;
        }
    }
}