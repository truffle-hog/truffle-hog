package edu.kit.trufflehog.view;

import edu.kit.trufflehog.view.controllers.IWindowController;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * <p>
 *     This class is designed as being the main window for the application.
 *     Every view that is placed on a scene that should be shown in the main
 *     window has to be placed into this window controller.
 * </p>
 */
public final class RootWindowController implements IWindowController {

    /** The Stage this window is using. **/
    private final Stage stage;

    /**
     * <p>Instantiates a new RootWindowController.</p>
     *
     * @param primaryStage the stage this window is placed on
     * @param monitorScene the scene to be placed into the window
     */
    public RootWindowController(final Stage primaryStage, final Scene monitorScene) {

        this.stage = primaryStage;
        this.stage.setScene(monitorScene);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void show() {

        this.stage.show();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setScene(final Scene scene) {
        this.stage.setScene(scene);
    }

}
