package edu.kit.ipd.trufflehog.view;

import edu.kit.ipd.trufflehog.commands.IUserCommand;
import edu.kit.ipd.trufflehog.interactors.IInteraction;
import edu.kit.ipd.trufflehog.util.IListener;
import javafx.scene.layout.Toolbar;

public abstract class ToolbarController<I extends IInteraction> extends Toolbar implements ViewController {

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
