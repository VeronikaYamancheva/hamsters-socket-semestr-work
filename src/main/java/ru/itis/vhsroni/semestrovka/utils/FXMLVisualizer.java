package ru.itis.vhsroni.semestrovka.utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import ru.itis.vhsroni.semestrovka.HamsterApplication;

import java.io.IOException;

public class FXMLVisualizer {

    public void show(String fxmlName) {
        Stage stage = HamsterApplication.getStage();
        double currentWidth = stage.getWidth();
        double currentHeight = stage.getHeight();

        FXMLLoader loader = new FXMLLoader(HamsterApplication.class.getResource(fxmlName));
        try {
            AnchorPane root = loader.load();
            Scene scene = new Scene(root, currentWidth, currentHeight);
            stage.setScene(scene);
            stage.sizeToScene();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
