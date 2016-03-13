package edu.kit.trufflehog.view;

import edu.kit.trufflehog.command.usercommand.NodeSelectionCommand;
import edu.kit.trufflehog.interaction.GraphInteraction;
import edu.kit.trufflehog.model.configdata.ConfigData;
import edu.kit.trufflehog.model.network.INetworkViewPort;
import edu.kit.trufflehog.view.controllers.AnchorPaneController;
import edu.kit.trufflehog.view.controllers.NetworkGraphViewController;
import edu.kit.trufflehog.view.elements.ImageButton;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * <p>
 * </p>
 *
 * @author Julian Brendl
 * @version 1.0
 */
public class LiveViewViewController extends AnchorPaneController {
    // General variables
    private final ConfigData configData;

    // View layers
    private final StackPane stackPane;

    private final Scene scene;

    private OverlayViewController recordOverlayViewController;
    private FilterOverlayViewController filterOverlayViewController;
    private OverlayViewController settingsOverlayViewController;

    public LiveViewViewController(String fxml, ConfigData configData, StackPane stackPane, NetworkGraphViewController networkViewScreen, Scene scene) {
        super(fxml);
        this.configData = configData;
        this.scene = scene;

        this.stackPane = stackPane;

        this.getChildren().add(networkViewScreen);

        this.setMinWidth(200d);
        this.setMinHeight(200d);

        AnchorPane.setBottomAnchor(networkViewScreen, 0d);
        AnchorPane.setTopAnchor(networkViewScreen, 0d);
        AnchorPane.setLeftAnchor(networkViewScreen, 0d);
        AnchorPane.setRightAnchor(networkViewScreen, 0d);

        addToolbar();
        addGeneralStatisticsOverlay();
        addNodeStatisticsOverlay();
        addSettingsOverlay();
        addFilterMenuOverlay();
        addRecordOverlay();
    }

    /**
     * <p>
     *     Builds the settings overlay.
     * </p>
     */
    private void addSettingsOverlay() {
        settingsOverlayViewController = new OverlayViewController("local_settings_overlay.fxml");
        this.getChildren().add(settingsOverlayViewController);
        AnchorPane.setBottomAnchor(settingsOverlayViewController, 60d);
        AnchorPane.setLeftAnchor(settingsOverlayViewController, 18d);
        settingsOverlayViewController.setVisible(false);
    }

    /**
     * <p>
     *     Builds the filter menu overlay.
     * </p>
     */
    private void addFilterMenuOverlay() {
        // Build filter menu
        filterOverlayViewController = new FilterOverlayViewController("filter_menu_overlay.fxml", configData, stackPane);

        // Set up overlay on screen
        this.getChildren().add(filterOverlayViewController);
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
    private void addRecordOverlay() {
        recordOverlayViewController = new OverlayViewController("node_statistics_overlay.fxml");
        this.getChildren().add(recordOverlayViewController);
        AnchorPane.setBottomAnchor(recordOverlayViewController, 60d);
        AnchorPane.setLeftAnchor(recordOverlayViewController, 18d);
        recordOverlayViewController.setVisible(false);
    }

    /**
     * <p>
     *     Builds the node statistics overlay.
     * </p>
     */
    private void addNodeStatisticsOverlay() {
        OverlayViewController nodeStatisticsOverlay = new OverlayViewController("node_statistics_overlay.fxml");
        this.getChildren().add(nodeStatisticsOverlay);
        AnchorPane.setTopAnchor(nodeStatisticsOverlay, 10d);
        AnchorPane.setRightAnchor(nodeStatisticsOverlay, 10d);
        nodeStatisticsOverlay.setVisible(false);
    }

    /**
     * <p>
     *     Builds the general statistics overlay.
     * </p>
     */
    private void addGeneralStatisticsOverlay() {
        OverlayViewController generalStatisticsOverlay = new OverlayViewController("general_statistics_overlay.fxml");
        this.getChildren().add(generalStatisticsOverlay);
        AnchorPane.setBottomAnchor(generalStatisticsOverlay, 10d);
        AnchorPane.setRightAnchor(generalStatisticsOverlay, 10d);
    }

    /**
     * <p>
     *     Builds the toolbar (3 buttons on the bottom left corner).
     * </p>
     */
    private void addToolbar() {
        Button settingsButton = buildSettingsButton();
        Button filterButton = buildFilterButton();
        Button recordButton = buildRecordButton();

        ToolBarViewController mainToolBarController = new ToolBarViewController("main_toolbar.fxml", settingsButton,
                filterButton, recordButton);
        this.getChildren().add(mainToolBarController);
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
        settingsButton.setOnAction(event -> handleShowMechanism(settingsOverlayViewController, filterOverlayViewController,
                recordOverlayViewController));

        scene.getAccelerators().put(new KeyCodeCombination(KeyCode.S, KeyCombination.ALT_DOWN),
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
        filterButton.setOnAction(event -> handleShowMechanism(filterOverlayViewController, recordOverlayViewController,
                settingsOverlayViewController));

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

        recordButton.setOnAction(event -> handleShowMechanism(recordOverlayViewController, filterOverlayViewController,
                settingsOverlayViewController));

        recordButton.setScaleX(0.8);
        recordButton.setScaleY(0.8);

        return recordButton;
    }

    /**
     * <p>
     *     Handles the showing mechanism of all overlays from the toolbar. OverlayViewController1 will be shown
     *     while the other two will be hidden.
     * </p>
     */
    private void handleShowMechanism(final Node overlay1,
                                    final Node overlay2,
                                    final Node overlay3) {
        // Show the first menu
        overlay1.setVisible(!overlay1.isVisible());

        // Hide the second menu if it is visible
        if (overlay2.isVisible()) {
            overlay2.setVisible(false);
        }

        // Hide the third menu if it is visible
        if (overlay3.isVisible()) {
            overlay3.setVisible(false);
        }
    }
}
