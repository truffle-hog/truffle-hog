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
import edu.kit.trufflehog.interaction.ProtocolControlInteraction;
import edu.kit.trufflehog.view.elements.ImageButton;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ToolBar;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.EnumMap;
import java.util.Map;

/**
 * <p>
 * </p>
 *
 * @author Julian Brendl
 * @version 1.0
 */
public class LiveViewController extends SplitableView<ProtocolControlInteraction> {
    private static final Logger logger = LogManager.getLogger(LiveViewController.class);

    // General variables
    private final Map<ProtocolControlInteraction, IUserCommand> interactionMap = new EnumMap<>(ProtocolControlInteraction.class);
    private boolean connected = false;
    private final MultiViewManager multiViewManager;

    // View layers
    private final NetworkViewScreen networkViewScreen;
    private final AnchorPane captureMenu;
    private final AnchorPane filterMenu;
    private final AnchorPane viewSettingsMenu;
    private final BorderPane selectionStatistics;
    private final BorderPane generalStatistics;
    private final ToolBar toolBar;

    private final ImageButton closeButton;

    public LiveViewController(final String fxml,
                              final MultiViewManager multiViewManager,
                              final NetworkViewScreen networkViewScreen,
                              final AnchorPane viewSettingsMenu,
                              final AnchorPane filterMenu,
                              final AnchorPane captureMenu,
                              final BorderPane selectionStatistics,
                              final BorderPane generalStatistics,
                              final ToolBar toolBar) {

        super(fxml);
        this.networkViewScreen = networkViewScreen;
        this.viewSettingsMenu = viewSettingsMenu;
        this.filterMenu = filterMenu;
        this.captureMenu = captureMenu;
        this.selectionStatistics = selectionStatistics;
        this.generalStatistics = generalStatistics;
        this.toolBar = toolBar;

        this.multiViewManager = multiViewManager;

        this.getChildren().add(networkViewScreen);

        this.setMinWidth(200d);
        this.setMinHeight(200d);

        AnchorPane.setBottomAnchor(networkViewScreen, 0d);
        AnchorPane.setTopAnchor(networkViewScreen, 0d);
        AnchorPane.setLeftAnchor(networkViewScreen, 0d);
        AnchorPane.setRightAnchor(networkViewScreen, 0d);

        closeButton = addCloseButton(multiViewManager);
        getChildren().add(closeButton);

        addToolbar();
        addGeneralStatisticsOverlay();
        addSelectionStatisticsOverlay();
        addSettingsOverlay();
        addFilterMenuOverlay();
        addCaptureOverlay();

        // Set this view as the currently selected view when it is pressed
        networkViewScreen.addEventHandler(MouseEvent.MOUSE_PRESSED, event -> multiViewManager.setSelected(this));
    }

    /**
     * <p>
     *     Builds the settings overlay.
     * </p>
     */
    private void addSettingsOverlay() {
        this.getChildren().add(viewSettingsMenu);
        AnchorPane.setBottomAnchor(viewSettingsMenu, 60d);
        AnchorPane.setLeftAnchor(viewSettingsMenu, 18d);
        viewSettingsMenu.setVisible(false);
    }

    /**
     * <p>
     *     Builds the filter menu overlay.
     * </p>
     */
    private void addFilterMenuOverlay() {
        // Set up overlay on screen
        this.getChildren().add(filterMenu);
        AnchorPane.setBottomAnchor(filterMenu, 60d);
        AnchorPane.setLeftAnchor(filterMenu, 18d);
        filterMenu.setMaxSize(330d, 210d);
        filterMenu.setVisible(false);
    }

    /**
     * <p>
     *     Builds the record menu overlay.
     * </p>
     */
    private void addCaptureOverlay() {
        AnchorPane.setBottomAnchor(captureMenu, 60d);
        AnchorPane.setLeftAnchor(captureMenu, 18d);
        this.getChildren().add(captureMenu);
        captureMenu.setVisible(false);
    }

    /**
     * <p>
     *     Builds the node statistics overlay.
     * </p>
     */
    private void addSelectionStatisticsOverlay() {
        this.getChildren().add(selectionStatistics);
        AnchorPane.setTopAnchor(selectionStatistics, 10d);
        AnchorPane.setRightAnchor(selectionStatistics, 10d);
    }

    /**
     * <p>
     *     Builds the general statistics overlay.
     * </p>
     */
    private void addGeneralStatisticsOverlay() {
        this.getChildren().add(generalStatistics);

        AnchorPane.setBottomAnchor(generalStatistics, 10d);
        AnchorPane.setRightAnchor(generalStatistics, 10d);
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

        toolBar.getItems().addAll(settingsButton, filterButton, recordButton, connectButton);

        this.getChildren().add(toolBar);
        AnchorPane.setBottomAnchor(toolBar, 5d);
        AnchorPane.setLeftAnchor(toolBar, 5d);
    }

    /**
     * <p>
     *     Builds the settings button.
     * </p>
     */
    private Button addSettingsButton() {
        final Button settingsButton = new ImageButton("gear.png");
        settingsButton.setOnAction(event -> handleShowMechanism(viewSettingsMenu, filterMenu,
                captureMenu));

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
        filterButton.setOnAction(event -> handleShowMechanism(filterMenu, captureMenu,
                viewSettingsMenu));

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

        recordButton.setOnAction(event -> handleShowMechanism(captureMenu, filterMenu,
                viewSettingsMenu));

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
                notifyListeners(interactionMap.get(ProtocolControlInteraction.CONNECT));
                connected = true;
                connectButton.setGraphic("access-point-connected.png");
            } else {
                // Fire event to disconnect from protocol source (e.g. snort)
                notifyListeners(interactionMap.get(ProtocolControlInteraction.DISCONNECT));
                connected = false;
                connectButton.setGraphic("access-point-disconnected.png");
            }
        });

        connectButton.setScaleX(0.8);
        connectButton.setScaleY(0.8);

        return connectButton;
    }

    /**
     * <p>
     *     Adds the close button.
     * </p>
     */
    private ImageButton addCloseButton(MultiViewManager multiViewManager) {
        final ImageButton closeButton = new ImageButton("close.png");

        closeButton.setOnAction(event ->  {
            if (multiViewManager.getViewCounter() > 1) {
                multiViewManager.merge(this);
            } else {
                multiViewManager.replace(this, MultiViewManager.START_VIEW);
            }
        });

        closeButton.setScaleX(0.8);
        closeButton.setScaleY(0.8);

        AnchorPane.setTopAnchor(closeButton, 10d);
        AnchorPane.setLeftAnchor(closeButton, 10d);

        return closeButton;
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

    @Override
    public void showCloseButton(boolean show) {
        closeButton.setVisible(show);
    }

    @Override
    public LiveViewController clone() {
        return null;
    }
}
