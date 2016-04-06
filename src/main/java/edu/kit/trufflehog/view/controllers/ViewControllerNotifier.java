package edu.kit.trufflehog.view.controllers;

import edu.kit.trufflehog.command.usercommand.IUserCommand;
import edu.kit.trufflehog.util.INotifier;
import edu.kit.trufflehog.util.Notifier;
import javafx.fxml.FXMLLoader;

import java.io.IOException;

/**
 * <p>
 *     This class has the single design purpose to be used by ViewControllers for implementing the functionality of the
 *     INotifier interface. Meaning, some view controllers are a composition that implements the {@link INotifier}
 *     interface, but calls the functionality on this wrapped instance.
 * </p>
 */
public class ViewControllerNotifier extends Notifier<IUserCommand> {

    public ViewControllerNotifier(String fxmlFile, Object rootController) {

        final FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlFile));
        fxmlLoader.setRoot(rootController);
        fxmlLoader.setController(rootController);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

}
