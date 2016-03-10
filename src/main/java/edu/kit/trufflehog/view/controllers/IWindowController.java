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
	 * <p>
     *     Shows this window on the screen.
	 * </p>
	 */
	void show();

	/**
     * <p>
     *     Sets the scene to be displayed by the window controller.
     * </p>
     *
	 * @param scene the scene to be displayed
	 */
	void setScene(Scene scene);
}
