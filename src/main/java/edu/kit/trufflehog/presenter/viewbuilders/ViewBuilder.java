
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

package edu.kit.trufflehog.presenter.viewbuilders;

import edu.kit.trufflehog.Main;
import edu.kit.trufflehog.model.configdata.ConfigData;
import edu.kit.trufflehog.model.network.INetworkViewPort;
import edu.kit.trufflehog.view.MainViewController;
import edu.kit.trufflehog.view.MenuBarViewController;
import edu.kit.trufflehog.view.RootWindowController;
import edu.kit.trufflehog.view.controllers.IWindowController;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.SplitPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

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

    // View viewbuilders
    private final LiveViewBuilder liveViewBuilder;

    // View layers
    private final Stage primaryStage;
    private final MainViewController mainViewController;
    private final AnchorPane groundView;
    private final StackPane stackPane;
    private final SplitPane splitPane;
    private final List<AnchorPane> views;

    /**
     * <p>
     * Creates the ViewBuilder, which builds the entire view.
     * </p>
     *
     * @param configData   The {@link ConfigData} that is necessary to save and load configurations, like
     *                     filters or settings.
     * @param primaryStage The primary stage, where everything is drawn upon.
     * @param viewPort The viewport of the graph that should be drawn here
     */
    public ViewBuilder(final ConfigData configData, final Stage primaryStage, final INetworkViewPort viewPort) {
        this.configData = configData;
        this.primaryStage = primaryStage;
        this.groundView = new AnchorPane();
        this.stackPane = new StackPane();
        this.splitPane = new SplitPane();
        this.views = new ArrayList<>();
        this.mainViewController = new MainViewController("main_view.fxml");

        this.liveViewBuilder = new LiveViewBuilder(configData, stackPane, primaryStage, viewPort);

        if (this.primaryStage == null || this.configData == null) {
            throw new NullPointerException("primaryStage and configData shouldn't be null.");
        }
    }

    /**
     * <p>
     * Builds the entire view. That means it connects all view components with each other and with other necessary
     * components as well.
     * </p>
     *
     */
    public void build() {
        loadFonts();

        // Load menu bar
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

        // Add the ground view to the center
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

        // Now we add the actual view to the split pane, the live view.
        AnchorPane liveView = liveViewBuilder.buildView();
        views.add(liveView);
        splitPane.getItems().addAll(liveView);
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
     *     Loads all custom fonts.
     * </p>
     */
    private void loadFonts() {
        Font.loadFont(Main.class.getClassLoader().getResourceAsStream("fonts" + File.separator + "DroidSans" +
                File.separator + "DroidSans.ttf"), 12);
    }
}