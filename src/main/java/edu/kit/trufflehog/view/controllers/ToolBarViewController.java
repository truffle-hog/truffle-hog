package edu.kit.trufflehog.view.controllers;


import edu.kit.trufflehog.command.usercommand.IUserCommand;
import edu.kit.trufflehog.interaction.GraphInteraction;
import edu.kit.trufflehog.interaction.IInteraction;
import edu.kit.trufflehog.util.IListener;
import edu.kit.trufflehog.util.INotifier;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.util.Map;

/**
 * <p>
 *     The ToolBarViewController class is the view designed for all most important functionality that should be
 *     directly accessed by the user during the running program.
 * </p>
 */
    public class ToolBarViewController<I extends IInteraction> extends ToolBar implements IViewController<I> {

        /**
         * <p>
         *     The wrapped instance of view controllers notifier.
         * </p>
         */
        private final INotifier<IUserCommand> viewControllerNotifier;

        private final Map<I, IUserCommand> interactionMap;

        /**
         * <p>
         *     Creates a new AnchorPaneController based on the given fxml file.
         * </p>
         *
         * @param fxmlFile The fxml file to create the AnchorPaneController from.
         */
        public ToolBarViewController(String fxmlFile, Map<I, IUserCommand> interactionMap) {

            this.interactionMap = interactionMap;

            viewControllerNotifier = new ViewControllerNotifier(fxmlFile, this);
        }

        @Override
        public final boolean addListener(IListener<IUserCommand> listener) {
            return viewControllerNotifier.addListener(listener);
        }

        @Override
        public final boolean removeListener(IListener<IUserCommand> listener) {
            return viewControllerNotifier.removeListener(listener);
        }

        @Override
        public final void notifyListeners(IUserCommand message) {
            viewControllerNotifier.notifyListeners(message);
        }

        @Override
        public final void addCommand(I interaction, IUserCommand command) {
            interactionMap.put(interaction, command);
        }

        public final IUserCommand getCommand(I interaction) {
            return interactionMap.get(interaction);
        }
}
