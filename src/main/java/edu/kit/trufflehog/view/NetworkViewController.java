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
import edu.kit.trufflehog.presenter.NetworkType;
import edu.kit.trufflehog.view.elements.ImageButton;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ToolBar;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

import java.util.EnumMap;
import java.util.Map;

/**
 * <p>
 *     The NetworkViewController is the core view of TruffleHog. It incorporates the network graph along with all
 *     the menus.
 * </p>
 *
 * @author Julian Brendl
 * @version 1.0
 */
public class NetworkViewController extends SplitableView<ProtocolControlInteraction> {
    private final Map<ProtocolControlInteraction, IUserCommand> interactionMap = new EnumMap<>(ProtocolControlInteraction.class);
    private boolean connected = false;

    // View layers
    private final AnchorPane captureMenu;
    private final AnchorPane filterMenu;
    private final AnchorPane viewSettingsMenu;
    private final BorderPane selectionStatistics;
    private final BorderPane generalStatistics;
    private final ToolBar toolBar;
    private ImageButton closeButton;

    /**
     * <p>
     *     Creates a new NetworkViewController.
     * </p>
     *
     * @param fxml The path to the fxml file that the NetworkViewController is linked to.
     * @param viewSplitter The view splitter that handles the close button action.
     * @param networkViewScreen The networkViewScreen that contains the network that should be drawn on screen.
     * @param viewSettingsMenu The view settings menu (view local as opposed to global).
     * @param filterMenu The filter menu.
     * @param captureMenu The capture menu.
     * @param selectionStatistics The selection statistic view.
     * @param generalStatistics The general statistic view.
     * @param toolBar The toolbar on the bottom right.
     */
    public NetworkViewController(final String fxml,
                                 final ViewSplitter viewSplitter,
                                 final NetworkViewScreen networkViewScreen,
                                 final AnchorPane viewSettingsMenu,
                                 final AnchorPane filterMenu,
                                 final AnchorPane captureMenu,
                                 final BorderPane selectionStatistics,
                                 final BorderPane generalStatistics,
                                 final ToolBar toolBar) {

        super(fxml);
        this.viewSettingsMenu = viewSettingsMenu;
        this.filterMenu = filterMenu;
        this.captureMenu = captureMenu;
        this.selectionStatistics = selectionStatistics;
        this.generalStatistics = generalStatistics;
        this.toolBar = toolBar;

        this.getChildren().add(networkViewScreen);

        this.setMinWidth(200d);
        this.setMinHeight(200d);

        // Fix the view to the corners of the screen
        AnchorPane.setBottomAnchor(networkViewScreen, 0d);
        AnchorPane.setTopAnchor(networkViewScreen, 0d);
        AnchorPane.setLeftAnchor(networkViewScreen, 0d);
        AnchorPane.setRightAnchor(networkViewScreen, 0d);

        // Add all components and configure them correctly
        addToolbar();
        addCloseButton(viewSplitter);
        addGeneralStatisticsOverlay();
        addSelectionStatisticsOverlay();
        addSettingsOverlay();
        addFilterMenuOverlay();
        addCaptureOverlay();

        // Set this view as the currently selected view when it is pressed
        networkViewScreen.addEventHandler(MouseEvent.MOUSE_PRESSED, event -> viewSplitter.setSelected(this));
    }

    /**
     * <p>
     *     Adds the settings overlay to the view.
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
     *     Adds the filter menu overlay to the view.
     * </p>
     */
    private void addFilterMenuOverlay() {
        this.getChildren().add(filterMenu);
        AnchorPane.setBottomAnchor(filterMenu, 60d);
        AnchorPane.setLeftAnchor(filterMenu, 18d);
        filterMenu.setMaxSize(330d, 210d);
        filterMenu.setVisible(false);
    }

    /**
     * <p>
     *     Adds the record menu overlay to the view.
     * </p>
     */
    private void addCaptureOverlay() {
        this.getChildren().add(captureMenu);
        AnchorPane.setBottomAnchor(captureMenu, 60d);
        AnchorPane.setLeftAnchor(captureMenu, 18d);
        captureMenu.setVisible(false);
    }

    /**
     * <p>
     *     Adds the node statistics overlay to the view.
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
     *     Adds the toolbar (3 buttons on the bottom left corner) to the view.
     * </p>
     */
    private void addToolbar() {
        final Button settingsButton = addSettingsButton();
        final Button filterButton = addFilterButton();
        final Button recordButton = addCaptureButton();
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
     *
     * @return The fully configured settings button
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
     *
     * @return The fully configured filter button
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
     *
     * @return The fully configured capture button
     */
    private Button addCaptureButton() {
        final ImageButton captureButton = new ImageButton("capture.png");

        captureButton.setOnAction(event -> handleShowMechanism(captureMenu, filterMenu,
                viewSettingsMenu));

        captureButton.setScaleX(0.8);
        captureButton.setScaleY(0.8);

        return captureButton;
    }

    /**
     * <p>
     *     Builds the connect button.
     * </p>
     *
     * @return The fully configured connect button
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
     *     Adds the close button to the view.
     * </p>
     *
     * @param viewSplitter The viewSplitter that is used to handle the close event.
     */
    private Button addCloseButton(ViewSplitter viewSplitter) {
        closeButton = new ImageButton("close.png");

        closeButton.setOnAction(event ->  {
            if (viewSplitter.getViewCounter() > 1) {
                viewSplitter.merge(this);
            } else {
                viewSplitter.replace(this, NetworkType.START);
            }
        });

        closeButton.setScaleX(0.8);
        closeButton.setScaleY(0.8);

        AnchorPane.setTopAnchor(closeButton, 10d);
        AnchorPane.setLeftAnchor(closeButton, 10d);

        getChildren().add(closeButton);

        return closeButton;
    }

    /**
     * <p>
     *     Handles the showing mechanism of all overlays from the toolbar. OverlayViewController1 will be shown
     *     while the other two will be hidden.
     * </p>
     *
     * @param overlay1 The overlay to show.
     * @param overlay2 An overlay to hide.
     * @param overlay3 An overlay to hide.
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
}
