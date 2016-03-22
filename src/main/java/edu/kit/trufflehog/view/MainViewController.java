package edu.kit.trufflehog.view;

import edu.kit.trufflehog.view.controllers.BorderPaneController;
import edu.kit.trufflehog.view.controllers.IWindowController;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * <p>
 *     The MainViewController incorporates all GUI elements that belong to the primary scope of the application.
 *     This for example includes the top Menu Bar and all the settings menus as well as the statistic windows.
 * </p>
 */
public class MainViewController extends BorderPaneController {
    // View layers
    private final Stage primaryStage;
    private final Scene mainScene;
    private final AnchorPane groundView;
    private final StackPane stackPane;
    private final SplitPane splitPane;

    /**
     * <p>
     *     Creates a new MainViewController with the given fxmlFileName. The fxml file has to be in the same namespace
     *     as the MainViewController.
     * </p>
     *
     * @param fxmlFileName the name of the fxml file to be loaded.
     */
    public MainViewController(final String fxmlFileName,
                              final Stage primaryStage,
                              final MenuBarViewController menuBar) {
        super(fxmlFileName);
        this.primaryStage = primaryStage;
        this.groundView = new AnchorPane();
        this.stackPane = new StackPane();
        this.splitPane = new SplitPane();

        if (this.primaryStage == null) {
            throw new NullPointerException("primaryStage should not be null.");
        }

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
        mainScene = new Scene(this);
        final IWindowController rootWindow = new RootWindowController(primaryStage, mainScene, "icon.png", menuBar);

        // Add the ground view to the center
        setCenter(groundView);

        rootWindow.show();

        // Set min. dimensions
        primaryStage.setMinWidth(950d);
        primaryStage.setMinHeight(675d);
    }

    /**
     * <p>
     *     Gets the main scene.
     * </p>
     *
     * @return the main scene.
     */
    public Scene getMainScene() {
        return mainScene;
    }

    /**
     * <p>
     *     Gets the stack pane that lies over everything (used to display the pop over menus like the edit filter menu).
     * </p>
     *
     * @return the stack pane that lies over everything.
     */
    public StackPane getStackPane() {
        return stackPane;
    }

    /**
     * <p>
     *     Gets the primary split pane.
     * </p>
     *
     * @return the primary split pane.
     */
    public SplitPane getSplitPane() {
        return splitPane;
    }

    /**
     * <p>
     *     Execute the routine for quitting the application.
     * </p>
     */
    public void onExit() {
        throw new UnsupportedOperationException("Not implemented yet!");
    }
}
