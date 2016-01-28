package edu.kit.trufflehog.view;

import edu.kit.trufflehog.command.usercommand.IUserCommand;
import edu.kit.trufflehog.util.INotifier;
import edu.kit.trufflehog.util.Notifier;

/**
 * This class has the single design purpose to be used by ViewControllers for
 * implementing the functionality of the INotifier interface. Meaning, every
 * view controller is a composition that implements the INotifier interface,
 * but calls the functionality on this wrapped instance.
 */
class ViewControllerNotifier extends Notifier<IUserCommand> implements
		INotifier<IUserCommand> {
}
