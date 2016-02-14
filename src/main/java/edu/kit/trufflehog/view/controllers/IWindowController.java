package edu.kit.trufflehog.view.controllers;

import javafx.scene.Scene;

/**
 * <p>
 *     The basic interface for all windows. Windows are placed on the screen
 *     for user interaction and view.
 * </p>
 */
public interface IWindowController {

	/**
	 * Shows this window on the screen.
	 */
	void show();

	/**
	 * Sets the scene to be displayed by the window controller.
	 * @param scene the scene to be displayed
	 */
	void setScene(Scene scene);
}
