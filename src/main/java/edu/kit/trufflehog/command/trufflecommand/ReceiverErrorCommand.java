package edu.kit.trufflehog.command.trufflecommand;

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
    private final Stage primaryStage;

    public ReceiverErrorCommand(final String message, final Stage primaryStage) { //TODO replace the String with a language specific property?
        this.message = message;
        this.primaryStage = primaryStage;
    }

    @Override
    public void execute() {
        final Popup popup = new Popup();
        popup.setAutoHide(true);
        popup.setHideOnEscape(true);

        popup.setX(primaryStage.getX() + primaryStage.getHeight() / 2);
        popup.setY(primaryStage.getY() + primaryStage.getWidth() / 2);
        popup.getContent().addAll(new Label(message));
        popup.show(primaryStage);
    }
}
