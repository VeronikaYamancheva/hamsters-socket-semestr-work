package ru.itis.vhsroni.semestrovka.controller.impl;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import ru.itis.vhsroni.semestrovka.HamsterApplication;
import ru.itis.vhsroni.semestrovka.controller.Controller;
import ru.itis.vhsroni.semestrovka.settings.FXMLConstants;
import ru.itis.vhsroni.semestrovka.settings.GameConstants;
import ru.itis.vhsroni.semestrovka.utils.FXMLVisualizer;

import java.net.URL;
import java.util.ResourceBundle;

public class StartController implements Controller {

    @FXML
    private Label userNameInput;

    @FXML
    private TextField setUserName;

    @FXML
    private Button submit;

    private final FXMLVisualizer visualizer = new FXMLVisualizer();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        submit.setOnAction(actionEvent -> {
            String userName = setUserName.getText();
            HamsterApplication.setUserName(userName.isBlank() ? GameConstants.DEFAULT_USERNAME : userName);
            visualizer.show(FXMLConstants.CREATE_OR_JOIN_ROOM_SCREEN_NAME);
        });
    }
}
