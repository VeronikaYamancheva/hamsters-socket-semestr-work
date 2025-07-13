package ru.itis.vhsroni.semestrovka.controller.impl;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import ru.itis.vhsroni.semestrovka.HamsterApplication;
import ru.itis.vhsroni.semestrovka.client.HamsterClient;
import ru.itis.vhsroni.semestrovka.controller.Controller;
import ru.itis.vhsroni.semestrovka.exception.server.HostException;
import ru.itis.vhsroni.semestrovka.server.HamsterServer;
import ru.itis.vhsroni.semestrovka.server.ServerInitializer;
import ru.itis.vhsroni.semestrovka.settings.FXMLConstants;
import ru.itis.vhsroni.semestrovka.settings.GameConstants;
import ru.itis.vhsroni.semestrovka.ui.CustomAlertManager;
import ru.itis.vhsroni.semestrovka.utils.FXMLVisualizer;

import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ResourceBundle;

public class CreateRoomController implements Controller {


    @FXML
    private Label roomNameInput;

    @FXML
    private TextField setRoomName;

    @FXML
    private Label playersCountInput;

    @FXML
    private TextField setPlayersCount;

    @FXML
    private Label portInput;

    @FXML
    private TextField setPort;

    @FXML
    private Button submit;

    @FXML
    private Button backButton;

    private final FXMLVisualizer visualizer = new FXMLVisualizer();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        submit.setOnAction(actionEvent -> launchServer());
        backButton.setOnAction(actionEvent -> visualizer.show(FXMLConstants.CREATE_OR_JOIN_ROOM_SCREEN_NAME));
    }

    private void launchServer() {
        try {
            String roomName = setRoomName.getText().trim();
            int playersCount = Integer.parseInt(setPlayersCount.getText());
            int port = Integer.parseInt(setPort.getText());

            if (playersCount < GameConstants.MIN_PLAYERS_COUNT || playersCount > GameConstants.MAX_PLAYERS_COUNT) {
                CustomAlertManager.showErrorAlert("Некорректное количество игроков",
                        "Количество игроков должно быть от %s до %s.".formatted(
                                GameConstants.MIN_PLAYERS_COUNT,
                                GameConstants.MAX_PLAYERS_COUNT
                        ));
                return;
            }
            if (port < 1024 || port > 65535) {
                CustomAlertManager.showErrorAlert("Некорректный порт",
                        "Порт должен быть в диапазоне от 1024 до 65535.");
                return;
            }
            HamsterServer server = ServerInitializer.createServer(port, playersCount);
            server.setRoomName(roomName.isBlank() ? GameConstants.DEFAULT_ROOM_NAME : roomName);
            ServerInitializer.startServer(server);
            Platform.runLater(() -> showWaitingRoomScreen(port));
        } catch (NumberFormatException e) {
            CustomAlertManager.showErrorAlert("Ошибка ввода",
                    "Пожалуйста, введите корректные числовые значения.");
        }
    }

    public void showWaitingRoomScreen(int port) {
        try {
            String host = InetAddress.getLocalHost().getHostAddress();
            HamsterClient client = new HamsterClient(host, port);
            HamsterApplication.setClient(client);
            client.connect();
            visualizer.show(FXMLConstants.WAITING_ROOM_SCREEN_NAME);
        } catch (UnknownHostException e) {
            throw new HostException(e);
        }
    }
}
