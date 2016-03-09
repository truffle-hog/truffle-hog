package edu.kit.trufflehog.view;

import edu.kit.trufflehog.command.usercommand.IUserCommand;
import edu.kit.trufflehog.interaction.OverlayInteraction;
import edu.kit.trufflehog.view.controllers.GridPaneController;

import java.util.EnumMap;
import java.util.Map;

/**
 * <p>
 *     The OverlayViewController provides GUI functionality for all
 *     interactions that a user performs on an overlay that can be displayed
 *     as a floating (slightly transparent) view on top of the main view.
 * </p>
 */
public class OverlayViewController extends GridPaneController<OverlayInteraction> {

	/** The commands that are mapped to their interactions. **/
    private final Map<OverlayInteraction, IUserCommand> interactionMap =
            new EnumMap<>(OverlayInteraction.class);

    /**
     * <p>
     *     Creates a new OverlayViewController with the given fxmlFileName.
     *     The fxml file has to be in the same namespace as the
     *     OverlayViewController.
     * </p>
     * @param fxmlFileName the name of the fxml file to be loaded.
     */
    public OverlayViewController(final String fxmlFileName) {

        super(fxmlFileName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void addCommand(final OverlayInteraction interactor, final
    IUserCommand
            command) {

        interactionMap.put(interactor, command);
    }
}
