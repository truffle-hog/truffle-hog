
/*
 * This file is part of TruffleHog.
 *
 * TruffleHog is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * TruffleHog is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with TruffleHog.  If not, see <http://www.gnu.org/licenses/>.
 */

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
        timerField.setId("record_timer");

        getChildren().addAll(timerField, button);

        AnchorPane.setTopAnchor(timerField, 10d);
        AnchorPane.setRightAnchor(timerField, 0d);
        AnchorPane.setLeftAnchor(timerField, 15d);

        AnchorPane.setTopAnchor(button, 20d);
        AnchorPane.setRightAnchor(button, 0d);
        AnchorPane.setLeftAnchor(button, 0d);

        button.setOnAction(event -> {
            if (!pressed) {
                pressed = true;
                Platform.runLater(() -> {
                    button.startGlow();
                    timerField.startTimer();
                });


                //startRecord(networkDevice, liveNetwork);
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
    private void startAction(INetworkDevice networkDevice, INetwork liveNetwork) {
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
