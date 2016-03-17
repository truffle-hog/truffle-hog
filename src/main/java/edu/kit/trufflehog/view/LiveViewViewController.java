package edu.kit.trufflehog.view;

import edu.kit.trufflehog.command.usercommand.IUserCommand;
import edu.kit.trufflehog.command.usercommand.SelectionCommand;
import edu.kit.trufflehog.interaction.FilterInteraction;
import edu.kit.trufflehog.interaction.GraphInteraction;
import edu.kit.trufflehog.interaction.ProtocolControlInteraction;
import edu.kit.trufflehog.model.configdata.ConfigData;
import edu.kit.trufflehog.model.filter.FilterInput;
import edu.kit.trufflehog.model.network.INetwork;
import edu.kit.trufflehog.model.network.INetworkViewPort;
import edu.kit.trufflehog.model.network.graph.FRLayoutFactory;
import edu.kit.trufflehog.model.network.recording.INetworkDevice;
import edu.kit.trufflehog.util.IListener;
import edu.kit.trufflehog.view.controllers.AnchorPaneInteractionController;
import edu.kit.trufflehog.view.controllers.NetworkGraphViewController;
import edu.kit.trufflehog.view.elements.ImageButton;
import edu.kit.trufflehog.viewmodel.StatisticsViewModel;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;
import java.util.EnumMap;
import java.util.Map;

/**
 * <p>
 * </p>
 *
 * @author Julian Brendl
 * @version 1.0
 */
public class LiveViewViewController extends AnchorPaneInteractionController<ProtocolControlInteraction> {
    private static final Logger logger = LogManager.getLogger(LiveViewViewController.class);

    // General variables
    private final Map<ProtocolControlInteraction, IUserCommand> interactionMap = new EnumMap<>(ProtocolControlInteraction.class);
    private final ConfigData configData;
    private boolean connected = false;

    // View layers
    private final StackPane stackPane;

    private final Scene scene;

    private RecordMenuViewController recordOverlayViewController;
    private FilterOverlayViewController filterOverlayViewController;
    private OverlayViewController settingsOverlayViewController;
    private final IUserCommand<FilterInput> updateFilterCommand;
    private final IListener<IUserCommand> userCommandListener;
    private final StatisticsViewModel statViewModel = new StatisticsViewModel();

    public LiveViewViewController(final String fxml,
                                  final ConfigData configData,
                                  final StackPane stackPane,
                                  final INetworkViewPort viewPort,
                                  final Scene scene,
                                  final IUserCommand<FilterInput> updateFilterCommand,
                                  final IListener<IUserCommand> userCommandIListener,
                                  final INetworkDevice networkDevice,
                                  final INetwork liveNetwork) {
        super(fxml);

        this.updateFilterCommand = updateFilterCommand;
        this.userCommandListener = userCommandIListener;

        this.configData = configData;
        this.scene = scene;

        this.stackPane = stackPane;


        //final StatisticsViewModel statView = new StatisticsViewModel();
        // FIXME this screen is also create in the ViewBuilder... is that necessary??!
        final NetworkViewScreen networkViewScreen = new NetworkViewScreen(viewPort, 30, new Dimension(700, 700));
        networkViewScreen.addListener(userCommandIListener);
        networkViewScreen.addCommand(GraphInteraction.SELECTION, new SelectionCommand(statViewModel));
        //networkViewScreen.addCommand(GraphInteraction.VERTEX_SELECTED, new NodeSelectionCommand());

        networkViewScreen.setLayoutFactory(new FRLayoutFactory());

        scene.getAccelerators().put(new KeyCodeCombination(KeyCode.X, KeyCombination.CONTROL_DOWN),
                networkViewScreen::refreshLayout);

        this.getChildren().add(networkViewScreen);

        this.setMinWidth(200d);
        this.setMinHeight(200d);

        AnchorPane.setBottomAnchor(networkViewScreen, 0d);
        AnchorPane.setTopAnchor(networkViewScreen, 0d);
        AnchorPane.setLeftAnchor(networkViewScreen, 0d);
        AnchorPane.setRightAnchor(networkViewScreen, 0d);

        addCloseButton();

        addToolbar();
        addGeneralStatisticsOverlay();
        addNodeStatisticsOverlay();
        addSettingsOverlay();
        addFilterMenuOverlay(networkViewScreen);
        addRecordOverlay(networkDevice, liveNetwork);
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
    private void addFilterMenuOverlay(NetworkGraphViewController networkViewScreen) {
        // Build filter menu
        filterOverlayViewController = new FilterOverlayViewController("filter_menu_overlay.fxml", configData, stackPane,
                networkViewScreen.getPickedVertexState());

        // Set up overlay on screen
        this.getChildren().add(filterOverlayViewController);
        AnchorPane.setBottomAnchor(filterOverlayViewController, 60d);
        AnchorPane.setLeftAnchor(filterOverlayViewController, 18d);
        filterOverlayViewController.setMaxSize(330d, 210d);
        filterOverlayViewController.setVisible(false);

        filterOverlayViewController.addListener(userCommandListener);
        filterOverlayViewController.addCommand(FilterInteraction.UPDATE, updateFilterCommand);
        filterOverlayViewController.addCommand(FilterInteraction.ADD, updateFilterCommand);
        filterOverlayViewController.addCommand(FilterInteraction.REMOVE, updateFilterCommand);
    }

    /**
     * <p>
     *     Builds the record menu overlay.
     * </p>
     */
    private void addRecordOverlay(INetworkDevice networkDevice, INetwork liveNetwork) {
        recordOverlayViewController = new RecordMenuViewController("record_overlay_menu.fxml", networkDevice, liveNetwork);
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
        final StatisticsViewController statisticsViewController = new StatisticsViewController(statViewModel);
        this.getChildren().add(statisticsViewController);

        AnchorPane.setTopAnchor(statisticsViewController, 10d);
        AnchorPane.setRightAnchor(statisticsViewController, 10d);
    }

    /**
     * <p>
     *     Builds the general statistics overlay.
     * </p>
     */
    private void addGeneralStatisticsOverlay() {
        final OverlayViewController generalStatisticsOverlay = new OverlayViewController("general_statistics_overlay.fxml");
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
        final Button settingsButton = addSettingsButton();
        final Button filterButton = addFilterButton();
        final Button recordButton = addRecordButton();
        final Button connectButton = addConnectButton();

        final ToolBarViewController mainToolBarController = new ToolBarViewController("main_toolbar.fxml", settingsButton,
                filterButton, recordButton, connectButton);
        this.getChildren().add(mainToolBarController);
        AnchorPane.setBottomAnchor(mainToolBarController, 5d);
        AnchorPane.setLeftAnchor(mainToolBarController, 5d);
    }

    /**
     * <p>
     *     Builds the settings button.
     * </p>
     */
    private Button addSettingsButton() {
        final Button settingsButton = new ImageButton("gear.png");
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
    private Button addFilterButton() {
        final Button filterButton = new ImageButton("filter.png");
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
    private Button addRecordButton() {
        final ImageButton recordButton = new ImageButton("record.png");

        recordButton.setOnAction(event -> handleShowMechanism(recordOverlayViewController, filterOverlayViewController,
                settingsOverlayViewController));

        recordButton.setScaleX(0.8);
        recordButton.setScaleY(0.8);

        return recordButton;
    }

    /**
     * <p>
     *     Builds the connect button.
     * </p>
     */
    private Button addConnectButton() {
        final ImageButton connectButton = new ImageButton("access-point-disconnected.png");

        connectButton.setOnAction(event -> {
            if (!connected) {
                // Fire event to connect to protocol source (e.g. snort)
                //notifyListeners(interactionMap.get(ProtocolControlInteraction.CONNECT));
                connected = true;
                connectButton.setGraphic("access-point-connected.png");
            } else {
                // Fire event to disconnect from protocol source (e.g. snort)
                //notifyListeners(interactionMap.get(ProtocolControlInteraction.DISCONNECT));
                connected = false;
                connectButton.setGraphic("access-point-disconnected.png");
            }
        });

        connectButton.setScaleX(0.8);
        connectButton.setScaleY(0.8);

        return connectButton;
    }

    private void addCloseButton() {
        final ImageButton closeButton = new ImageButton("close.png");

        closeButton.setOnAction(event -> {

        });

        closeButton.setScaleX(0.8);
        closeButton.setScaleY(0.8);

        AnchorPane.setTopAnchor(closeButton, 10d);
        AnchorPane.setLeftAnchor(closeButton, 10d);

        this.getChildren().add(closeButton);
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

    @Override
    public void addCommand(ProtocolControlInteraction interaction, IUserCommand command) {
        interactionMap.put(interaction, command);
    }
}
