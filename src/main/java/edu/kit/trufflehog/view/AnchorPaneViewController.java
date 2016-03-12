package edu.kit.trufflehog.view;

import edu.kit.trufflehog.command.usercommand.IUserCommand;
import edu.kit.trufflehog.interaction.AnchorPaneInteraction;
import edu.kit.trufflehog.interaction.OverlayInteraction;
import edu.kit.trufflehog.view.controllers.AnchorPaneController;

import java.util.EnumMap;
import java.util.Map;

/**
 * <p>
 * </p>
 *
 * @author Julian Brendl
 * @version 1.0
 */
public class AnchorPaneViewController extends AnchorPaneController<AnchorPaneInteraction> {

    /**
     * <p>
     *     The commands that are mapped to their interactions.
     * </p>
     */
    private final Map<OverlayInteraction, IUserCommand> interactionMap = new EnumMap<>(OverlayInteraction.class);

    /**
     * <p>
     *     Creates a new OverlayViewController with the given fxmlFileName.
     *     The fxml file has to be in the same namespace as the
     *     OverlayViewController.
     * </p>
     *
     * @param fxmlFileName the name of the fxml file to be loaded.
     */
    public AnchorPaneViewController(final String fxmlFileName) {
        super(fxmlFileName);
    }

    @Override
    public void addCommand(final AnchorPaneInteraction interaction, final IUserCommand command) {

    }
}
