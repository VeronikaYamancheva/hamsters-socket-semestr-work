package ru.itis.vhsroni.semestrovka;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import ru.itis.vhsroni.semestrovka.client.HamsterClient;
import ru.itis.vhsroni.semestrovka.exception.ApplicationInitializationException;
import ru.itis.vhsroni.semestrovka.settings.FXMLConstants;
import ru.itis.vhsroni.semestrovka.settings.GameConstants;
import ru.itis.vhsroni.semestrovka.settings.UIConstants;

import java.io.IOException;
import java.io.InputStream;

public class HamsterApplication extends Application {

    private static Stage stage;

    private static HamsterClient client;

    private static String userName;

    @Override
    public void start(Stage primaryStage) {
        stage = primaryStage;

        primaryStage.setTitle(GameConstants.GAME_NAME);
        primaryStage.setWidth(UIConstants.SCREEN_DEFAULT_WIDTH);
        primaryStage.setHeight(UIConstants.SCREEN_DEFAULT_HEIGHT);

        InputStream iconStream = this.getClass().getResourceAsStream(GameConstants.APP_ICON_IMAGE);
        if (iconStream != null) {
            Image image = new Image(iconStream);
            primaryStage.getIcons().add(image);
        }

        primaryStage.setOnCloseRequest(e ->
        {
            if (client != null && client.getThread() != null) {
                client.getThread().stop();
            }
            System.exit(0);
        });

        FXMLLoader loader = new FXMLLoader(HamsterApplication.class.getResource(FXMLConstants.START_SCREEN_NAME));
        Scene scene;
        try {
            scene = new Scene(loader.load());
        } catch (IOException e) {
            throw new ApplicationInitializationException(e);
        }
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static Stage getStage() {
        return stage;
    }

    public static HamsterClient getClient() {
        return client;
    }

    public static void setClient(HamsterClient client) {
        HamsterApplication.client = client;
    }

    public static void setUserName(String userName) {
        HamsterApplication.userName = userName;
    }

    public static String getUserName() {
        return userName;
    }
}
