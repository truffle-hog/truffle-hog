package edu.kit.trufflehog.view;

import edu.kit.trufflehog.command.usercommand.IUserCommand;
import edu.kit.trufflehog.command.usercommand.StartRecordCommand;
import edu.kit.trufflehog.model.network.INetwork;
import edu.kit.trufflehog.model.network.recording.INetworkDevice;
import edu.kit.trufflehog.model.network.recording.INetworkTape;
import edu.kit.trufflehog.model.network.recording.NetworkTape;
import edu.kit.trufflehog.view.controllers.AnchorPaneController;
import edu.kit.trufflehog.view.elements.GlowImageButton;
import edu.kit.trufflehog.view.elements.TimerField;
import javafx.application.Platform;
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
    public RecordMenuViewController(String fxmlFile, INetworkDevice networkDevice, INetwork liveNetwork) {
        super(fxmlFile);

        final GlowImageButton button = new GlowImageButton("record-circle.png");
        final TimerField timerField = new TimerField();
        timerField.setId("timer");

        getChildren().addAll(timerField, button);

        AnchorPane.setTopAnchor(timerField, 10d);
        AnchorPane.setRightAnchor(timerField, 0d);
        AnchorPane.setLeftAnchor(timerField, 51d);

        AnchorPane.setTopAnchor(button, 20d);
        AnchorPane.setRightAnchor(button, 0d);
        AnchorPane.setLeftAnchor(button, 0d);

        button.setOnAction(event -> {
            if (!pressed) {
                pressed = true;
                Platform.runLater(() -> {
                    //button.startGlow();
                    timerField.startTimer();
                });

                startRecord(networkDevice, liveNetwork);
            } else {
                pressed = false;
                button.stopGlow();
                timerField.stopTimer();
            }
        });
    }

    /**
     * <p>
     *     Starts recording the current graph.
     * </p>
     *
     * @param networkDevice
     * @param liveNetwork
     */
    private void startRecord(INetworkDevice networkDevice, INetwork liveNetwork) {
        final INetworkTape tape = new NetworkTape(20);

        final IUserCommand startRecordCommand = new StartRecordCommand(networkDevice, liveNetwork, tape);
        startRecordCommand.execute();
    }

    /**
     * <p>
     *     Stops recording the current graph, and opens a window to save the recorded graph properly.
     * </p>
     */
    private void stopRecord() {
    }
}
