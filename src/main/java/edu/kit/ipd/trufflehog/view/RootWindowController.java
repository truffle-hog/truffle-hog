package edu.kit.ipd.trufflehog.view;

import edu.kit.ipd.trufflehog.commands.IUserCommand;
import edu.kit.ipd.trufflehog.interactors.RootInteraction;
import javafx.stage.Stage;
import javafx.scene.Scene;

public class RootWindowController implements ViewController {

	private Stage stage;

	private Scene scene;

	private ViewControllerNotifier viewControllerNotifier;

	public void RootViewController(Stage primaryStage, Scene monitorScene) {

	}

	public void addCommand(RootInteraction interactor, IUserCommand command) {

	}

	public void show() {

	}


	/**
	 * @see trufflehog.view.ViewController#addCommand(trufflehog.view.I, trufflehog.commands.IUserCommand)
	 * 
	 *  
	 */
	public void addCommand(I interactor, IUserCommand command) {

	}

}
