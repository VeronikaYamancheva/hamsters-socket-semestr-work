package ru.itis.vhsroni.semestrovka.controller.impl;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import ru.itis.vhsroni.semestrovka.controller.Controller;
import ru.itis.vhsroni.semestrovka.settings.FXMLConstants;
import ru.itis.vhsroni.semestrovka.utils.FXMLVisualizer;

import java.net.URL;
import java.util.ResourceBundle;

public class CreateOrJoinController implements Controller {

    @FXML
    private Label choosingLabel;

    @FXML
    private Button chooseJoin;

    @FXML
    private Button chooseCreate;

    @FXML
    private Button backButton;


    private final FXMLVisualizer visualizer = new FXMLVisualizer();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        chooseCreate.setOnAction(actionEvent -> visualizer.show(FXMLConstants.CREATE_ROOM_SCREEN_NAME));
        chooseJoin.setOnAction(actionEvent -> visualizer.show(FXMLConstants.JOIN_ROOM_SCREEN_NAME));
        backButton.setOnAction(actionEvent -> visualizer.show(FXMLConstants.START_SCREEN_NAME));
    }
}
