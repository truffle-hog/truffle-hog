package edu.kit.ipd.trufflehog.view;

import edu.kit.ipd.trufflehog.commands.IUserCommand;
import edu.kit.ipd.trufflehog.interactors.IInteraction;
import javafx.scene.layout.BorderPane;
import edu.kit.ipd.trufflehog.util.IListener;

public abstract class BorderPaneController<I extends IInteraction> extends BorderPane implements ViewController {

	private ViewControllerNotifier viewControllerNotifier;

	public void addListener(IListener listener) {

	}

	public void removeListener(IListener listener) {

	}

	public void sendToListeners(IUserCommand message) {

	}


	/**
	 * @see trufflehog.view.ViewController#addCommand(trufflehog.view.I, trufflehog.commands.IUserCommand)
	 * 
	 *  
	 */
	public void addCommand(I interactor, IUserCommand command) {

	}

}
