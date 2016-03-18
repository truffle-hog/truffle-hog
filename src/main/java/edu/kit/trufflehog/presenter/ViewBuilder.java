
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

import edu.kit.trufflehog.Main;
import edu.kit.trufflehog.command.usercommand.IUserCommand;
import edu.kit.trufflehog.command.usercommand.StartRecordCommand;
import edu.kit.trufflehog.model.configdata.ConfigData;
import edu.kit.trufflehog.model.filter.FilterInput;
import edu.kit.trufflehog.model.network.INetwork;
import edu.kit.trufflehog.model.network.INetworkViewPort;
import edu.kit.trufflehog.model.network.recording.INetworkDevice;
import edu.kit.trufflehog.model.network.recording.INetworkTape;
import edu.kit.trufflehog.model.network.recording.INetworkViewPortSwitch;
import edu.kit.trufflehog.model.network.recording.NetworkTape;
import edu.kit.trufflehog.util.IListener;
import edu.kit.trufflehog.view.*;
import edu.kit.trufflehog.view.controllers.IWindowController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.File;
import java.util.Map;

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
    // General variables
    private ConfigData configData;

    // View layers
    private final Stage primaryStage;
    private final MainViewController mainViewController;
    private final AnchorPane groundView;
    private final StackPane stackPane;
    private final SplitPane splitPane;
    private final ViewSwitcher viewSwitcher;
    private final Map<String, INetworkViewPort> viewPorts;

    /**
     * <p>
     *     Creates the ViewBuilder, which builds the entire view.
     * </p>
     *
     * @param configData   The {@link ConfigData} that is necessary to save and load configurations, like
     *                     filters or settings.
     * @param primaryStage The primary stage, where everything is drawn upon.
     * @param viewPorts The viewports that TruffleHog supports mapped to their names.
     */
    public ViewBuilder(final ConfigData configData,
                       final Stage primaryStage,
                       final Map<String, INetworkViewPort> viewPorts) {
        this.configData = configData;
        this.primaryStage = primaryStage;
        this.groundView = new AnchorPane();
        this.stackPane = new StackPane();
        this.splitPane = new SplitPane();
        this.viewSwitcher = new ViewSwitcher();
        this.mainViewController = new MainViewController("main_view.fxml");
        this.viewPorts = viewPorts;

        if (this.primaryStage == null || this.configData == null) {
            throw new NullPointerException("primaryStage and configData shouldn't be null.");
        }
    }

    /**
     *
     * @param viewPort
     * @param liveNetwork
     * @param device
     * @param userCommandIListener
     * @param updateFilterCommand
     */
    public void build(final INetworkViewPortSwitch viewPort,
                      final INetwork liveNetwork,
                      final INetworkDevice device,
                      final IListener<IUserCommand> userCommandIListener,
                      final IUserCommand<FilterInput> updateFilterCommand) {
        loadFonts();


        //final StatisticsViewModel statView = new StatisticsViewModel();

        // Load menu bar
        final MenuBarViewController menuBar = new MenuBarViewController("menu_bar.fxml");

        // Set up the ground view. This is always the full center of the BorderPane. We add the splitPane to it
        // because it is right on top of it.
        groundView.getChildren().add(splitPane);
        splitPane.setOrientation(Orientation.HORIZONTAL);

        // Now we fix the splitPane to the edges of the groundView so that it can display the various graph views
        AnchorPane.setBottomAnchor(splitPane, 0d);
        AnchorPane.setTopAnchor(splitPane, 0d);
        AnchorPane.setLeftAnchor(splitPane, 0d);
        AnchorPane.setRightAnchor(splitPane, 0d);

        // We add a stackPane here for the PopOverOverlays that are displayed on it
        AnchorPane.setTopAnchor(stackPane, 0d);
        AnchorPane.setLeftAnchor(stackPane, 0d);
        AnchorPane.setRightAnchor(stackPane, 0d);
        AnchorPane.setBottomAnchor(stackPane, 0d);
        groundView.getChildren().add(stackPane);
        stackPane.setVisible(false);

        // Set up the scene
        final Scene mainScene = new Scene(mainViewController);
        final IWindowController rootWindow = new RootWindowController(primaryStage, mainScene, "icon.png", menuBar);

        //mainViewController.setBottom(buildReplayFunction(device, liveNetwork, viewPort));

        // Add the ground view to the center
        mainViewController.setCenter(groundView);

        rootWindow.show();

        // Set min. dimensions
        primaryStage.setMinWidth(950d);
        primaryStage.setMinHeight(650d);

        // Add some keyboard shortcuts
        setKeyboardShortcuts(menuBar);

        // Now we add the actual views to the split pane and to the view switcher
        buildViews(userCommandIListener, updateFilterCommand, device, liveNetwork);

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

    // Will go away, keep for reference
//    private FlowPane buildReplayFunction(INetworkDevice networkDevice,
//                                         INetwork liveNetwork,
//                                         INetworkViewPortSwitch viewPortSwitch) {
//
//        final INetworkTape tape = new NetworkTape(20);
//
//        final Slider slider = new Slider(0, 100, 0);
//        slider.setTooltip(new Tooltip("replay"));
//        tape.getCurrentReadingFrameProperty().bindBidirectional(slider.valueProperty());
//        tape.getFrameCountProperty().bindBidirectional(slider.maxProperty());
//
//        final ToggleButton liveButton = new ToggleButton("Live");
//        liveButton.setDisable(true);
//        final ToggleButton playButton = new ToggleButton("Play");
//        playButton.setDisable(false);
//        final ToggleButton stopButton = new ToggleButton("Stop");
//        stopButton.setDisable(false);
//        final ToggleButton recButton = new ToggleButton("Rec");
//        recButton.setDisable(false);
//
//        liveButton.setOnAction(h -> {
//            networkDevice.goLive(liveNetwork, viewPortSwitch);
//            liveButton.setDisable(true);
//        });
//
//        playButton.setOnAction(handler -> {
//            networkDevice.play(tape, viewPortSwitch);
//            liveButton.setDisable(false);
//        });
//
//        final IUserCommand startRecordCommand = new StartRecordCommand(networkDevice, liveNetwork, tape);
//        recButton.setOnAction(h -> startRecordCommand.execute());
//
//        slider.setStyle("-fx-background-color: transparent");
//
//        final ToolBar toolBar = new ToolBar();
//        toolBar.getItems().add(stopButton);
//        toolBar.getItems().add(playButton);
//        toolBar.getItems().add(recButton);
//        toolBar.getItems().add(liveButton);
//        toolBar.setStyle("-fx-background-color: transparent");
//        //toolBar.getItems().add(slider);
//
//        final FlowPane flowPane = new FlowPane();
//
//        flowPane.getChildren().addAll(toolBar, slider);
//
//        return flowPane;
//    }


    private void buildViews(final IListener<IUserCommand> userCommandIListener,
                            final IUserCommand<FilterInput> updateFilterCommand,
                            final INetworkDevice networkDevice,
                            final INetwork liveNetwork) {
        ObservableList<String> liveItems = FXCollections.observableArrayList(viewPorts.keySet());
        ObservableList<String> captureItems = FXCollections.observableArrayList("capture-932", "capture-724",
                "capture-457", "capture-167");

        AnchorPane startView = new StartViewViewController("start_view.fxml", liveItems, captureItems, viewSwitcher);
        AnchorPane demoView = new LiveViewViewController("live_view.fxml", configData, viewSwitcher, stackPane,
                viewPorts.get(ViewSwitcher.PROFINET_VIEW) , primaryStage.getScene(), updateFilterCommand, userCommandIListener,
                networkDevice, liveNetwork);

//        AnchorPane profinetView = new LiveViewViewController("live_view.fxml", configData, stackPane, primaryStage,
//                viewPorts.get("Profinet"));
        viewSwitcher.putView(ViewSwitcher.START_VIEW, startView);
        viewSwitcher.putView(ViewSwitcher.PROFINET_VIEW, demoView);

        //viewSwitcher.putView("Profinet", profinetView);
        splitPane.getItems().addAll(startView);
    }

    private void setKeyboardShortcuts(MenuBar menuBar) {
        primaryStage.getScene().getAccelerators().put(new KeyCodeCombination(KeyCode.Q, KeyCombination.CONTROL_DOWN),
                primaryStage::close);
        primaryStage.getScene().getAccelerators().put(new KeyCodeCombination(KeyCode.F11), () -> {
            primaryStage.setFullScreen(!primaryStage.isFullScreen());
            menuBar.setVisible(!menuBar.isVisible());
        });
        primaryStage.getScene().getAccelerators().put(new KeyCodeCombination(KeyCode.Q, KeyCombination.CONTROL_DOWN),
                primaryStage::close);
    }

    /**
     * <p>
     *     Loads all custom fonts.
     * </p>
     */
    private void loadFonts() {
        Font.loadFont(Main.class.getClassLoader().getResourceAsStream("fonts" + File.separator + "DroidSans" +
                File.separator + "DroidSans.ttf"), 12);
    }
}