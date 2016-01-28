package edu.kit.trufflehog.view;

import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * This class is designed as being the main window for the application. Every
 * view that is placed on a scene that should be shown in the main window
 * has to be placed into this window controller.
 */
public final class RootWindowController implements IWindowController {

	/** The Stage this window is using. **/
	private final Stage stage;

	/**
	 * Instantiates a new RootWindowController.
	 *
	 * @param primaryStage the stage this window is placed on
	 * @param monitorScene the scene to be placed into the window
	 */
	public RootWindowController(final Stage primaryStage, final Scene
								monitorScene) {

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
	 * @param scene
	 */
	@Override
	public void setScene(final Scene scene) {
		this.stage.setScene(scene);
	}

}
