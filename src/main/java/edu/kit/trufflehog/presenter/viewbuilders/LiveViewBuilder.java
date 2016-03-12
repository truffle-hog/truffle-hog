package edu.kit.trufflehog.presenter.viewbuilders;

import edu.kit.trufflehog.model.configdata.ConfigData;
import edu.kit.trufflehog.model.network.INetworkViewPort;
import edu.kit.trufflehog.view.MainToolBarController;
import edu.kit.trufflehog.view.NetworkViewScreen;
import edu.kit.trufflehog.view.OverlayViewController;
import edu.kit.trufflehog.view.elements.FilterOverlayMenu;
import edu.kit.trufflehog.view.elements.ImageButton;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * <p>
 * </p>
 *
 * @author Julian Brendl
 * @version 1.0
 */
class LiveViewBuilder implements IViewBuilder {
    // General variables
    private final ConfigData configData;

    // Graph variables
    private final INetworkViewPort viewPort;

    // View layers
    private final Stage primaryStage;
    private final StackPane stackPane;
    private final AnchorPane liveView;

    private OverlayViewController recordOverlayViewController;
    private OverlayViewController filterOverlayViewController;
    private OverlayViewController settingsOverlayViewController;

    private TableView tableView;

    public LiveViewBuilder(ConfigData configData, StackPane stackPane, Stage primaryStage, INetworkViewPort viewPort) {
        this.configData = configData;
        this.primaryStage = primaryStage;
        this.stackPane = stackPane;
        this.viewPort = viewPort;
        liveView = new AnchorPane();
    }

    @Override
    public AnchorPane buildView() {
        final Node node = new NetworkViewScreen(viewPort, 10);

        liveView.getChildren().add(node);

        liveView.setMinWidth(200d);
        liveView.setMinHeight(200d);

        AnchorPane.setBottomAnchor(node, 0d);
        AnchorPane.setTopAnchor(node, 0d);
        AnchorPane.setLeftAnchor(node, 0d);
        AnchorPane.setRightAnchor(node, 0d);

        buildToolbar();
        buildGeneralStatisticsOverlay();
        buildNodeStatisticsOverlay();
        buildSettingsOverlay();
        buildFilterMenuOverlay();
        buildRecordOverlay();

        return liveView;
    }

    /**
     * <p>
     *     Builds the settings overlay.
     * </p>
     */
    private void buildSettingsOverlay() {
        settingsOverlayViewController = new OverlayViewController("local_settings_overlay.fxml");
        liveView.getChildren().add(settingsOverlayViewController);
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
        liveView.getChildren().add(filterOverlayViewController);
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
        liveView.getChildren().add(recordOverlayViewController);
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
        liveView.getChildren().add(nodeStatisticsOverlay);
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
        liveView.getChildren().add(generalStatisticsOverlay);
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
        liveView.getChildren().add(mainToolBarController);
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
}
