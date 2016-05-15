package edu.kit.trufflehog.command.trufflecommand;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.stage.Popup;
import javafx.stage.Stage;

/**
 * <p>
 *     This command is used to notify the user of any errors that occurred in the receiver.
 * </p>
 */
public class ReceiverErrorCommand implements ITruffleCommand {

    private final String message;

    public ReceiverErrorCommand(final String message) { //TODO replace the String with a language specific property?
        this.message = message;
    }

    @Override
    public void execute() {
        //final Popup popup = new Popup();
        //popup.setAutoHide(true);
        //popup.setHideOnEscape(true);
        Platform.runLater( () -> {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        //alert.setHeaderText("Look, a Warning Dialog");
        alert.setContentText(message);
        alert.showAndWait();});
        //popup.setX(primaryStage.getX() + primaryStage.getHeight() / 2);
        //popup.setY(primaryStage.getY() + primaryStage.getWidth() / 2);
        //popup.getContent().addAll(new Label(message));

        // popup.show(primaryStage);
    }
}
