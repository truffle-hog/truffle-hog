
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
import edu.kit.trufflehog.command.usercommand.NodeSelectionCommand;
import edu.kit.trufflehog.command.usercommand.StartRecordCommand;
import edu.kit.trufflehog.interaction.GraphInteraction;
import edu.kit.trufflehog.model.configdata.ConfigData;
import edu.kit.trufflehog.model.network.INetwork;
import edu.kit.trufflehog.model.network.recording.INetworkDevice;
import edu.kit.trufflehog.model.network.recording.INetworkTape;
import edu.kit.trufflehog.model.network.recording.INetworkViewPortSwitch;
import edu.kit.trufflehog.model.network.recording.NetworkTape;
import edu.kit.trufflehog.util.IListener;
import edu.kit.trufflehog.view.MainToolBarController;
import edu.kit.trufflehog.view.MainViewController;
import edu.kit.trufflehog.view.MenuBarViewController;
import edu.kit.trufflehog.view.NetworkViewScreen;
import edu.kit.trufflehog.view.OverlayViewController;
import edu.kit.trufflehog.view.RootWindowController;
import edu.kit.trufflehog.view.controllers.IWindowController;
import edu.kit.trufflehog.view.controllers.NetworkGraphViewController;
import edu.kit.trufflehog.view.elements.FilterOverlayMenu;
import edu.kit.trufflehog.view.elements.ImageButton;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToolBar;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.File;

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
    private final AnchorPane monitoringView;

    private OverlayViewController recordOverlayViewController;
    private OverlayViewController filterOverlayViewController;
    private OverlayViewController settingsOverlayViewController;

    private TableView tableView;

    /**
     * <p>
     *     Creates the ViewBuilder, which builds the entire view.
     * </p>
     *
     * @param configData The {@link ConfigData} that is necessary to save and load configurations, like
     *                        filters or settings.
     * @param primaryStage The primary stage, where everything is drawn upon.
     */
    public ViewBuilder(final ConfigData configData, final Stage primaryStage) {
        this.configData = configData;
        this.primaryStage = primaryStage;
        this.groundView = new AnchorPane();
        this.stackPane = new StackPane();
        this.splitPane = new SplitPane();
        this.monitoringView = new AnchorPane();
        this.mainViewController = new MainViewController("main_view.fxml");

        if (this.primaryStage == null || this.configData == null) {
            throw new NullPointerException("primaryStage and configData shouldn't be null.");
        }
    }

    private FlowPane buildReplayFunction(INetworkDevice networkDevice,
                                     INetwork liveNetwork, INetworkViewPortSwitch viewPortSwitch) {

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
            networkDevice.play(tape, viewPortSwitch);
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
        //  toolBar.getItems().add(slider);

        final FlowPane flowPane = new FlowPane();

        flowPane.getChildren().addAll(toolBar, slider);

        return flowPane;
    }

    /**
     * <p>
     *     Builds the entire view. That means it connects all view components with each other and with other necessary
     *     components as well.
     * </p>
     *
     * @param viewPort The viewport of the graph that should be drawn here
     * @param viewPort
     * @param userCommandIListener
     */
    public void build(INetworkViewPortSwitch viewPort, INetwork liveNetwork, INetworkDevice device, IListener<IUserCommand> userCommandIListener) {
        loadFonts();

        final IListener<IUserCommand> commandListener = userCommandIListener;

        final NetworkGraphViewController node = new NetworkViewScreen(viewPort, 10);
        node.addListener(commandListener);
        node.addCommand(GraphInteraction.VERTEX_SELECTED, new NodeSelectionCommand());


        final MenuBarViewController menuBar = buildMenuBar();

        // Set up the ground view. This is always the full center of the BorderPane. We add the splitPane to it
        // because it is right on top of it.
        groundView.getChildren().add(splitPane);
        splitPane.setOrientation(Orientation.HORIZONTAL);

        // Now we fix the splitPane to the edges of the groundView
        AnchorPane.setBottomAnchor(splitPane, 0d);
        AnchorPane.setTopAnchor(splitPane, 0d);
        AnchorPane.setLeftAnchor(splitPane, 0d);
        AnchorPane.setRightAnchor(splitPane, 0d);

        // Now we add the actual view to the split pane, the monitoring view.
        splitPane.getItems().addAll(monitoringView);
        monitoringView.getChildren().add(node);
        AnchorPane.setBottomAnchor(node, 0d);
        AnchorPane.setTopAnchor(node, 0d);
        AnchorPane.setLeftAnchor(node, 0d);
        AnchorPane.setRightAnchor(node, 0d);

        // We add a stackPane here for the PopOverOverlays that are displayed on it
        AnchorPane.setTopAnchor(stackPane, 0d);
        AnchorPane.setLeftAnchor(stackPane, 0d);
        AnchorPane.setRightAnchor(stackPane, 0d);
        AnchorPane.setBottomAnchor(stackPane, 0d);
        groundView.getChildren().add(stackPane);
        stackPane.setVisible(false);

        // Set up scene
        final Scene mainScene = new Scene(mainViewController);
        final IWindowController rootWindow = new RootWindowController(primaryStage, mainScene, "icon.png", menuBar);



       // mainViewController.setCenter(primaryView);
        mainViewController.setBottom(buildReplayFunction(device, liveNetwork, viewPort));
        mainViewController.setCenter(groundView);

        rootWindow.show();

        // Set min. dimensions
        primaryStage.setMinWidth(950d);
        primaryStage.setMinHeight(650d);

        primaryStage.getScene().getAccelerators().put(new KeyCodeCombination(KeyCode.Q, KeyCombination.CONTROL_DOWN),
                primaryStage::close);
        primaryStage.getScene().getAccelerators().put(new KeyCodeCombination(KeyCode.F11), () -> {
            primaryStage.setFullScreen(!primaryStage.isFullScreen());
            menuBar.setVisible(!menuBar.isVisible());
        });
        primaryStage.getScene().getAccelerators().put(new KeyCodeCombination(KeyCode.Q, KeyCombination.CONTROL_DOWN),
                primaryStage::close);

        buildToolbar();
        buildGeneralStatisticsOverlay();
        buildNodeStatisticsOverlay();
        buildSettingsOverlay();
        buildFilterMenuOverlay();
        buildRecordOverlay();
    }

    /**
     * <p>
     *     Builds the top menu bar which contains file, edit, help etc.
     * </p>
     *
     * @return The menu bar once it is built.
     */
    private MenuBarViewController buildMenuBar() {
        final MenuBarViewController menuBarViewController = new MenuBarViewController("menu_bar.fxml");
        return menuBarViewController;
    }

    /**
     * <p>
     *     Builds the settings overlay.
     * </p>
     */
    private void buildSettingsOverlay() {
        settingsOverlayViewController = new OverlayViewController("local_settings_overlay.fxml");
        monitoringView.getChildren().add(settingsOverlayViewController);
        AnchorPane.setBottomAnchor(settingsOverlayViewController, 60d);
        AnchorPane.setLeftAnchor(settingsOverlayViewController, 18d);
        settingsOverlayViewController.setVisible(false);
    }

    /**
     * <p>
     *     Builds the filter menu overlay.
     * </p>
     */
    private void buildFilterMenuOverlay() {
        // Build filter menu
        FilterOverlayMenu filterOverlayMenu = new FilterOverlayMenu(configData, stackPane);
        filterOverlayViewController = filterOverlayMenu.setUpOverlayViewController();
        tableView = filterOverlayMenu.setUpTableView();
        BorderPane borderPane = filterOverlayMenu.setUpMenu(tableView);

        // Add menu to overlay
        filterOverlayViewController.getChildren().add(borderPane);

        // Set up overlay on screen
        monitoringView.getChildren().add(filterOverlayViewController);
        AnchorPane.setBottomAnchor(filterOverlayViewController, 60d);
        AnchorPane.setLeftAnchor(filterOverlayViewController, 18d);
        filterOverlayViewController.setMaxSize(330d, 210d);
        filterOverlayViewController.setVisible(false);
    }

    /**
     * <p>
     *     Builds the record menu overlay.
     * </p>
     */
    private void buildRecordOverlay() {
        recordOverlayViewController = new OverlayViewController("node_statistics_overlay.fxml");
        monitoringView.getChildren().add(recordOverlayViewController);
        AnchorPane.setBottomAnchor(recordOverlayViewController, 60d);
        AnchorPane.setLeftAnchor(recordOverlayViewController, 18d);
        recordOverlayViewController.setVisible(false);
    }

    /**
     * <p>
     *     Builds the node statistics overlay.
     * </p>
     */
    private void buildNodeStatisticsOverlay() {
        OverlayViewController nodeStatisticsOverlay = new OverlayViewController("node_statistics_overlay.fxml");
        monitoringView.getChildren().add(nodeStatisticsOverlay);
        AnchorPane.setTopAnchor(nodeStatisticsOverlay, 10d);
        AnchorPane.setRightAnchor(nodeStatisticsOverlay, 10d);
        nodeStatisticsOverlay.setVisible(false);
    }

    /**
     * <p>
     *     Builds the general statistics overlay.
     * </p>
     */
    private void buildGeneralStatisticsOverlay() {
        OverlayViewController generalStatisticsOverlay = new OverlayViewController("general_statistics_overlay.fxml");
        monitoringView.getChildren().add(generalStatisticsOverlay);
        AnchorPane.setBottomAnchor(generalStatisticsOverlay, 10d);
        AnchorPane.setRightAnchor(generalStatisticsOverlay, 10d);
    }

    /**
     * <p>
     *     Builds the toolbar (3 buttons on the bottom left corner).
     * </p>
     */
    private void buildToolbar() {
        Button settingsButton = buildSettingsButton();
        Button filterButton = buildFilterButton();
        Button recordButton = buildRecordButton();

        MainToolBarController mainToolBarController = new MainToolBarController("main_toolbar.fxml", settingsButton,
                filterButton, recordButton);
        monitoringView.getChildren().add(mainToolBarController);
        AnchorPane.setBottomAnchor(mainToolBarController, 5d);
        AnchorPane.setLeftAnchor(mainToolBarController, 5d);
    }

    /**
     * <p>
     *     Builds the settings button.
     * </p>
     */
    private Button buildSettingsButton() {
        Button settingsButton = new ImageButton("gear.png");
        settingsButton.setOnAction(event -> {
            settingsOverlayViewController.setVisible(!settingsOverlayViewController.isVisible());

            // Hide the filter menu if it is visible
            if (filterOverlayViewController.isVisible()) {
                filterOverlayViewController.setVisible(false);
            }

            // Hide the record menu if it is visible
            if (recordOverlayViewController.isVisible()) {
                recordOverlayViewController.setVisible(false);
            }

//            Stage settingsStage = new Stage();
//            SettingsViewController settingsView = new SettingsViewController("settings_view.fxml");
//            Scene settingsScene = new Scene(settingsView);
//            settingsStage.setScene(settingsScene);
//            settingsStage.show();
//
//            // CTRL+W for closing
//            settingsStage.getScene().getAccelerators().put(new KeyCodeCombination(KeyCode.W, KeyCombination.CONTROL_DOWN)
//                    , settingsStage::close);
//
//            // CTRL+S triggers info about program settings saving
//            settingsStage.getScene().getAccelerators().put(new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN),
//                    () -> {
//                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
//                        alert.setTitle("Relax, no need to save anything here");
//                        alert.setHeaderText(null);
//                        alert.setContentText("Oops. Seems you wanted to save the configuration by pressing CTRL+S. This" +
//                                " is not necessary thanks to the awesome always up-to-date saving design of TruffleHog.");
//                        alert.showAndWait();
//                    });
        });

        primaryStage.getScene().getAccelerators().put(new KeyCodeCombination(KeyCode.S, KeyCombination.ALT_DOWN),
                settingsButton::fire);

        settingsButton.setScaleX(0.8);
        settingsButton.setScaleY(0.8);

        return settingsButton;
    }

    /**
     * <p>
     *     Builds the filter button.
     * </p>
     */
    private Button buildFilterButton() {
        Button filterButton = new ImageButton("filter.png");
        filterButton.setOnAction(event -> {
            filterOverlayViewController.setVisible(!filterOverlayViewController.isVisible());

            // Deselect anything that was selected
            if (!filterOverlayViewController.isVisible()) {
                tableView.getSelectionModel().clearSelection();
            }

            // Hide the settings menu if it is visible
            if (settingsOverlayViewController.isVisible()) {
                settingsOverlayViewController.setVisible(false);
            }

            // Hide the record menu if it is visible
            if (recordOverlayViewController.isVisible()) {
                recordOverlayViewController.setVisible(false);
            }
        });

        filterButton.setScaleX(0.8);
        filterButton.setScaleY(0.8);
        filterButton.setMaxSize(20, 20);
        filterButton.setMinSize(20, 20);

        return filterButton;
    }

    /**
     * <p>
     *     Builds the record button.
     * </p>
     */
    private Button buildRecordButton() {
        ImageButton recordButton = new ImageButton("record.png");

        recordButton.setOnAction(event -> {
            recordOverlayViewController.setVisible(!recordOverlayViewController.isVisible());

            // Hide the settings menu if it is visible
            if (settingsOverlayViewController.isVisible()) {
                settingsOverlayViewController.setVisible(false);
            }

            // Hide the filter menu if it is visible
            if (filterOverlayViewController.isVisible()) {
                filterOverlayViewController.setVisible(false);
            }
        });

        recordButton.setScaleX(0.8);
        recordButton.setScaleY(0.8);

        return recordButton;
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