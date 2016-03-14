package edu.kit.trufflehog.view;

import edu.kit.trufflehog.presenter.LoggedScheduledExecutor;
import edu.kit.trufflehog.view.controllers.AnchorPaneController;
import edu.kit.trufflehog.view.elements.GlowImageButton;
import edu.kit.trufflehog.view.elements.TimerField;
import javafx.scene.layout.AnchorPane;


/**
 * <p>
 *     This is an extension of the
 * </p>
 *
 * @author Julian Brendl
 * @version 1.0
 */
public class RecordMenuViewController extends AnchorPaneController {
    private boolean pressed = false;

    /**
     * <p>
     *     Creates a new AnchorPaneController based on the given fxml file.
     * </p>
     *
     * @param fxmlFile The fxml file to create the AnchorPaneController from.
     */
    public RecordMenuViewController(String fxmlFile) {
        super(fxmlFile);

        final GlowImageButton button = new GlowImageButton("record-circle.png");
        final TimerField timerField = new TimerField(new LoggedScheduledExecutor(1));
        timerField.setId("timer");

        getChildren().addAll(timerField, button);

        AnchorPane.setTopAnchor(timerField, 10d);
        AnchorPane.setRightAnchor(timerField, 0d);
        AnchorPane.setLeftAnchor(timerField, 45d);

        AnchorPane.setTopAnchor(button, 20d);
        AnchorPane.setRightAnchor(button, 0d);
        AnchorPane.setLeftAnchor(button, 0d);

        button.setOnAction(event -> {
            if (!pressed) {
                pressed = true;
                button.startGlow();
                timerField.startTimer();
            } else {
                pressed = false;
                button.stopGlow();
                timerField.stopTimer();
            }
        });
    }
}
