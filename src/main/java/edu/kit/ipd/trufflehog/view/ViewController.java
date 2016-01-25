package edu.kit.ipd.trufflehog.view;

import edu.kit.ipd.trufflehog.commands.IUserCommand;
import edu.kit.ipd.trufflehog.util.INotifier;
import edu.kit.ipd.trufflehog.interactors.IInteraction;

public interface ViewController<I extends IInteraction> extends INotifier {

	public abstract void addCommand(I interactor, IUserCommand command);

}
