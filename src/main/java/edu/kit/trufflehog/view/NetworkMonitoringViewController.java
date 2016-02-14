package edu.kit.trufflehog.view;

import edu.kit.trufflehog.command.usercommand.IUserCommand;
import edu.kit.trufflehog.interaction.NetworkMonitoringInteraction;
import edu.kit.trufflehog.view.controllers.BorderPaneController;
import javafx.fxml.FXMLLoader;

import java.io.IOException;
import java.util.EnumMap;
import java.util.Map;

/**
 * <p>
 *     The NetworkMonitoringViewController provides GUI functionality for all
 *     interactions that a user performs during monitoring of an ongoing
 *     network communication.
 * </p>
 */
public class NetworkMonitoringViewController extends BorderPaneController<NetworkMonitoringInteraction> {

	/** The commands that are mapped to their interactions. **/
    private final Map<NetworkMonitoringInteraction, IUserCommand> interactionMap =
            new EnumMap<>(NetworkMonitoringInteraction.class);

    /**
     * <p>
     *     Creates a new NetworkMonitoringViewController with the given fxmlFileName.
     *     The fxml file has to be in the same namespace as the
     *     NetworkMonitoringViewController.
     * </p>
     * @param fxmlFileName the name of the fxml file to be loaded
     */
    public NetworkMonitoringViewController(final String fxmlFileName) {

        super(fxmlFileName);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public final void addCommand(final NetworkMonitoringInteraction interactor, final
    IUserCommand
            command) {

        interactionMap.put(interactor, command);
    }
}
