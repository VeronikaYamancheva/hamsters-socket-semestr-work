package ru.itis.vhsroni.semestrovka.ui;

import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import ru.itis.vhsroni.semestrovka.HamsterApplication;
import ru.itis.vhsroni.semestrovka.settings.GameConstants;
import ru.itis.vhsroni.semestrovka.settings.UIConstants;

import java.util.Objects;

public class CustomAlertManager {

    public static void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.initOwner(HamsterApplication.getStage());
        alert.getDialogPane().getStylesheets().add(
                Objects.requireNonNull(HamsterApplication.class.getResource(UIConstants.ALERT_CSS)).toExternalForm()
        );
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(
                Objects.requireNonNull(HamsterApplication.class.getResource(GameConstants.ERROR_ALERT_IMAGE)).toExternalForm()
        ));
        alert.show();
    }

    public static void setInfoAlertTheme(Alert alert) {
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(
                Objects.requireNonNull(HamsterApplication.class.getResource(GameConstants.INFO_ALERT_IMAGE)).toExternalForm()
        ));
        alert.getDialogPane().getStylesheets().add(
                Objects.requireNonNull(HamsterApplication.class.getResource(UIConstants.ALERT_CSS)).toExternalForm()
        );
    }

    public static void setConfirmAlertTheme (Alert alert) {
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(
                Objects.requireNonNull(HamsterApplication.class.getResource(GameConstants.CONFIRM_ALERT_IMAGE)).toExternalForm()
        ));
        alert.getDialogPane().getStylesheets().add(
                Objects.requireNonNull(HamsterApplication.class.getResource(UIConstants.ALERT_CSS)).toExternalForm()
        );
    }
}
