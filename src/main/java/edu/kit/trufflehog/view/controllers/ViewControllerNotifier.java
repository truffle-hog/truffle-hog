package edu.kit.trufflehog.view.controllers;

import edu.kit.trufflehog.command.usercommand.IUserCommand;
import edu.kit.trufflehog.util.INotifier;
import edu.kit.trufflehog.util.Notifier;

/**
 * <p>
 *     This class has the single design purpose to be used by ViewControllers for implementing the functionality of the
 *     INotifier interface. Meaning, some view controllers are a composition that implements the {@link INotifier}
 *     interface, but calls the functionality on this wrapped instance.
 * </p>
 */
class ViewControllerNotifier extends Notifier<IUserCommand> {
}
