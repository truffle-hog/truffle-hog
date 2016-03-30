package edu.kit.trufflehog.view.controllers;

import edu.kit.trufflehog.command.usercommand.IUserCommand;
import edu.kit.trufflehog.interaction.IInteraction;
import edu.kit.trufflehog.util.INotifier;

/**
 * <p>
 *     The IViewController class is the basic interface that is
 *     implemented by every GUI View Component in the application.
 * </p>
 * <p>
 *     {@code IViewControllers} always provide a specified range of
 *     interactions that can be invoked. By using this abstraction a view
 *     controllers only needs to know the interactions it can perform, and
 *     does not have any idea of the underlying commands that are mapped to
 *     these interactions by the
 *     {@link edu.kit.trufflehog.presenter.Presenter}.
 * </p>
 * <p>
 *     Every IViewController is also integrated into the notification
 *     framework of this application by implementing the
 *     {@link INotifier} interface. Meaning if
 *     interactions are invoked on a view controllers, the commands mapped to
 *     these interactions are sent to all listeners.
 * </p>
 *
 * @param <I> The type of interactions this view controllers can invoke.
 *           Typically implemented by a specific enumeration.
 */
public interface IViewController<I extends IInteraction> extends INotifier<IUserCommand> {

	/**
	 * <p>
	 * 		Maps the specified command to the given interaction.
	 * </p>
	 *
	 * @param interaction the interaction the command is mapped to
	 * @param command the command that will be send to the listeners if the
	 *                   given interaction will be invoked
	 */
	void addCommand(I interaction, IUserCommand command);
}
