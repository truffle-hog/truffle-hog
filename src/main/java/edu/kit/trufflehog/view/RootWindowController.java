package edu.kit.trufflehog.view;

import edu.kit.trufflehog.view.controllers.IWindowController;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
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

    private final MenuBarViewController menuBar;

    /**
     * <p>Instantiates a new RootWindowController.</p>
     * @param primaryStage the stage this window is placed on
     * @param monitorScene the scene to be placed into the window
     * @param icon the window icon
     * @param menuBar the menu bar
     */
    public RootWindowController(final Stage primaryStage, final Scene monitorScene, String icon, MenuBarViewController menuBar) {

        this.stage = primaryStage;
        this.menuBar = menuBar;
        this.stage.setScene(monitorScene);

        BorderPane borderPane = (BorderPane) monitorScene.getRoot();
        borderPane.setTop(this.menuBar);
        borderPane.setPadding(new Insets(0, 0, 0, 0));


        this.stage.getIcons().add(new Image(this.getClass().getResourceAsStream(icon)));
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
