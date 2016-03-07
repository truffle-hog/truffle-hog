package edu.kit.trufflehog.view;

import edu.kit.trufflehog.command.usercommand.IUserCommand;
import edu.kit.trufflehog.interaction.MainInteraction;
import edu.kit.trufflehog.view.controllers.AnchorPaneController;

import java.util.EnumMap;
import java.util.Map;

/**
 * <p>
 *     The MainViewController incorporates all GUI elements that belong to the
 *     primary scope of the application. This for example includes the top
 *     Menu Bar.
 * </p>
 */
public class MainViewController extends AnchorPaneController<MainInteraction> {

	/** The commands that are mapped to their interactions. **/
    private final Map<MainInteraction, IUserCommand> interactionMap = new EnumMap<>(MainInteraction.class);

    /**
     * <p>
     *     The commands that are mapped to their interactions.
     * </p>
     */
    private final Map<MainInteraction, IUserCommand> interactionMap = new EnumMap<>(MainInteraction.class);

    /**
     * <p>
     *     Creates a new MainViewController with the given fxmlFileName. The fxml file has to be in the same namespace
     *     as the MainViewController.
     * </p>
     *
     * @param fxmlFileName the name of the fxml file to be loaded.
     */
    public MainViewController(final String fxmlFileName) {

        super(fxmlFileName);
    }

    /**
     * <p>
     *     Execute the routine for quitting the application.
     * </p>
     */
    public void onExit() {

        throw new UnsupportedOperationException("Not implemented yet!");
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public final void addCommand(final MainInteraction interactor, final IUserCommand command) {

        interactionMap.put(interactor, command);
    }
}
