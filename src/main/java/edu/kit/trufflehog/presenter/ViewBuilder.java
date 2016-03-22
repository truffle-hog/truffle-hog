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

package edu.kit.trufflehog.presenter;

import edu.kit.trufflehog.command.usercommand.IUserCommand;
import edu.kit.trufflehog.command.usercommand.StartRecordCommand;
import edu.kit.trufflehog.model.network.INetwork;
import edu.kit.trufflehog.model.network.recording.INetworkDevice;
import edu.kit.trufflehog.model.network.recording.INetworkTape;
import edu.kit.trufflehog.model.network.recording.INetworkViewPortSwitch;
import edu.kit.trufflehog.model.network.recording.NetworkTape;
import edu.kit.trufflehog.view.ViewSplitter;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * <p>
 *     The ViewBuilder builds an instance of the TruffleHog view. It connects all view components with each other and
 *     with other vital components like the saving/loading of configurations.
 * </p>
 *
 * @author Julian Brendl, Maximilian Diez
 * @version 1.0
 */
public class ViewBuilder {
    // View layers
    private final Stage primaryStage;
    private final AnchorPane groundView;
    private final StackPane stackPane;
    private final SplitPane splitPane;
    private final ViewSplitter viewSplitter;

    /**
     * <p>
     *     Creates the ViewBuilder, which builds the entire view.
     * </p>
     * @param primaryStage The primary stage, where everything is drawn upon.
     */
    public ViewBuilder(final Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.groundView = new AnchorPane();
        this.stackPane = new StackPane();
        this.splitPane = new SplitPane();
        this.viewSplitter = new ViewSplitter();
    }

    public void build() {

        //final StatisticsViewModel statView = new StatisticsViewModel();


        // Now we add the actual views to the split pane and to the view switcher
        //buildViews(userCommandIListener, updateFilterCommand, device, liveNetwork);

        /*primaryStage.getScene().getAccelerators().put(new KeyCodeCombination(KeyCode.X, KeyCombination.CONTROL_DOWN),
                viewPort::refreshLayout);*/
    }

    private FlowPane buildReplayFunction(INetworkDevice networkDevice,
                                         INetwork liveNetwork,
                                         INetworkViewPortSwitch viewPortSwitch) {

        final INetworkTape tape = new NetworkTape(20);

        final Slider slider = new Slider(0, 100, 0);
        slider.setTooltip(new Tooltip("replay"));
        tape.getCurrentReadingFrameProperty().bindBidirectional(slider.valueProperty());
        tape.getFrameCountProperty().bindBidirectional(slider.maxProperty());

        final ToggleButton liveButton = new ToggleButton("Live");
        liveButton.setDisable(true);
        final ToggleButton playButton = new ToggleButton("Play");
        playButton.setDisable(false);
        final ToggleButton stopButton = new ToggleButton("Stop");
        stopButton.setDisable(false);
        final ToggleButton recButton = new ToggleButton("Rec");
        recButton.setDisable(false);

        liveButton.setOnAction(h -> {
            networkDevice.goLive(liveNetwork, viewPortSwitch);
            liveButton.setDisable(true);
        });

        playButton.setOnAction(handler -> {
            networkDevice.goReplay(tape, viewPortSwitch);
            liveButton.setDisable(false);
        });

        final IUserCommand startRecordCommand = new StartRecordCommand(networkDevice, liveNetwork, tape);
        recButton.setOnAction(h -> startRecordCommand.execute());

        slider.setStyle("-fx-background-color: transparent");

        final ToolBar toolBar = new ToolBar();
        toolBar.getItems().add(stopButton);
        toolBar.getItems().add(playButton);
        toolBar.getItems().add(recButton);
        toolBar.getItems().add(liveButton);
        toolBar.setStyle("-fx-background-color: transparent");
        //toolBar.getItems().add(slider);

        final FlowPane flowPane = new FlowPane();

        flowPane.getChildren().addAll(toolBar, slider);

        return flowPane;
    }
}