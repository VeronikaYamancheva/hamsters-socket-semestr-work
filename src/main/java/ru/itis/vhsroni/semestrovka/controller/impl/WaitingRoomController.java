package ru.itis.vhsroni.semestrovka.controller.impl;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import ru.itis.vhsroni.semestrovka.HamsterApplication;
import ru.itis.vhsroni.semestrovka.client.HamsterClient;
import ru.itis.vhsroni.semestrovka.controller.MessageController;
import ru.itis.vhsroni.semestrovka.exception.message.MessageTypeException;
import ru.itis.vhsroni.semestrovka.settings.FXMLConstants;
import ru.itis.vhsroni.semestrovka.utils.MessageCreator;
import ru.itis.vhsroni.semestrovka.protocol.Message;
import ru.itis.vhsroni.semestrovka.protocol.MessageType;
import ru.itis.vhsroni.semestrovka.utils.FXMLVisualizer;

import java.net.URL;
import java.nio.ByteBuffer;
import java.util.ResourceBundle;

public class WaitingRoomController implements MessageController {

    @FXML
    private Label tempPlayersCountLabel;

    private int maxPlayersCount;

    private int tempPlayersCount;

    private final FXMLVisualizer visualizer = new FXMLVisualizer();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        HamsterClient client = HamsterApplication.getClient();
        client.setController(this);
        client.sendMessage(MessageCreator.createMessage(MessageType.CLIENT_MAX_COUNT_INFO_REQUEST, new byte[0]));
        client.sendMessage(MessageCreator.createMessage(MessageType.CLIENT_TEMP_COUNT_INFO_REQUEST, new byte[0]));
        tempPlayersCountLabel.setText("Players count: %s/%s".formatted(tempPlayersCount, maxPlayersCount));
    }

    @Override
    public void receiveMessage(Message message) {
        if (message != null) {
            switch (message.getType()) {
                case MessageType.CLIENT_TEMP_COUNT_INFO_RESPONSE -> {
                    ByteBuffer buffer = ByteBuffer.wrap(message.getData());
                    tempPlayersCount = buffer.getInt();
                    Platform.runLater(() -> tempPlayersCountLabel.setText(
                            "Players count: %s/%s".formatted(tempPlayersCount, maxPlayersCount)));
                }
                case MessageType.CLIENT_MAX_COUNT_INFO_RESPONSE -> {
                    ByteBuffer buffer = ByteBuffer.wrap(message.getData());
                    maxPlayersCount = buffer.getInt();
                }
                default -> throw new MessageTypeException(message.getType());
            }
        }
        if (tempPlayersCount == maxPlayersCount) {
            Platform.runLater(() -> visualizer.show(FXMLConstants.GAME_SCREEN_NAME));
        }
    }
}
