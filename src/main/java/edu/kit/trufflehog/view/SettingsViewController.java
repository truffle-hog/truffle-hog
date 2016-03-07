package edu.kit.trufflehog.view;

import edu.kit.trufflehog.command.usercommand.IUserCommand;
import edu.kit.trufflehog.interaction.SettingsInteraction;
import edu.kit.trufflehog.view.controllers.TabPaneController;

import java.util.EnumMap;
import java.util.Map;

/**
 * <p>
 *     The SettingsViewController provides the functionality for user
 *     interaction with the settings.
 * </p>
 */
public class SettingsViewController extends TabPaneController<SettingsInteraction> {
    /**
     * <p>
     *     The commands that are mapped to their interactions.
     * </p>
     */
    private final Map<SettingsInteraction, IUserCommand> interactionMap = new EnumMap<>(SettingsInteraction.class);

    /**
     * <p>
     *     Creates a new SettingsViewController with the given fxmlFileName.
     *     The fxml file has to be in the same namespace as the
     *     SettingsViewController.
     * </p>
     *
     * @param fxmlFileName the name of the fxml file to be loaded
     */
    public SettingsViewController(final String fxmlFileName) {

        super(fxmlFileName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void addCommand(final SettingsInteraction interactor, final IUserCommand command) {
        interactionMap.put(interactor, command);
    }
}
