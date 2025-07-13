package ru.itis.vhsroni.semestrovka.controller.impl;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import ru.itis.vhsroni.semestrovka.HamsterApplication;
import ru.itis.vhsroni.semestrovka.client.HamsterClient;
import ru.itis.vhsroni.semestrovka.controller.Controller;
import ru.itis.vhsroni.semestrovka.protocol.Message;
import ru.itis.vhsroni.semestrovka.settings.FXMLConstants;
import ru.itis.vhsroni.semestrovka.ui.CustomAlertManager;
import ru.itis.vhsroni.semestrovka.utils.FXMLVisualizer;

import java.net.URL;
import java.util.ResourceBundle;

public class JoinRoomController implements Controller {

    @FXML
    private Label IpInput;

    @FXML
    private TextField setIp;

    @FXML
    private Label portInput;

    @FXML
    private TextField setPort;

    @FXML
    private Button submitJoin;

    @FXML
    private Button backButton;

    private final FXMLVisualizer visualizer = new FXMLVisualizer();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        submitJoin.setOnAction(actionEvent -> connectToRoom());
        backButton.setOnAction(actionEvent -> visualizer.show(FXMLConstants.CREATE_OR_JOIN_ROOM_SCREEN_NAME));
    }

    private void connectToRoom() {
        String ip = setIp.getText().trim();
        String portString = setPort.getText().trim();

        if (ip.isEmpty() || portString.isEmpty()) {
            CustomAlertManager.showErrorAlert("Ошибка ввода", "Поля не должны быть пустыми.");
            return;
        }
        if (!isValidHost(ip)) {
            CustomAlertManager.showErrorAlert("Ошибка ввода", "Некорректный хост. Введите localhost или IP-адрес.");
            return;
        }
        int port;
        try {
            port = Integer.parseInt(portString);
            if (port < 1024 || port > 65535) {
                CustomAlertManager.showErrorAlert("Ошибка ввода", "Порт должен быть числом от 1024 до 65535.");
                return;
            }
        } catch (NumberFormatException e) {
            CustomAlertManager.showErrorAlert("Ошибка ввода", "Некорректный формат порта. Введите число.");
            return;
        }
        try {
            HamsterClient client = new HamsterClient(ip, port);
            client.connect();
            HamsterApplication.setClient(client);
            visualizer.show(FXMLConstants.WAITING_ROOM_SCREEN_NAME);
        } catch (Exception e) {
            CustomAlertManager.showErrorAlert("Ошибка подключения", "Не удалось подключиться к комнате.");
        }
    }

    private boolean isValidHost(String host) {
        return host.equalsIgnoreCase("localhost") || host.matches("^([0-9]{1,3}\\.){3}[0-9]{1,3}$");
    }
}