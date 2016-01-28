package edu.kit.trufflehog.view;


import edu.kit.trufflehog.command.usercommand.IUserCommand;
import edu.kit.trufflehog.interaction.ToolBarInteraction;
import javafx.fxml.FXMLLoader;

import java.io.IOException;
import java.util.EnumMap;
import java.util.Map;

/**
 * The MainToolBarController class is the View designed for all most
 * important functionality that should be directly accessed by the user
 * during the running program.
 */
public final class MainToolBarController extends
        ToolBarController<ToolBarInteraction> {

    /** The commands that are mapped to their interactions. **/
    private final Map<ToolBarInteraction, IUserCommand> interactionMap =
            new EnumMap<>(ToolBarInteraction.class);

    /**
     * Creates a new MainToolBarController with the fiven fxmlFileName.
     * The fxml file has to be in the same namespace as the
     * MainToolBarController.
     *
     * @param fxmlFileName the name of the fxml file to be loaded.
     */
    public MainToolBarController(final String fxmlFileName) {

        final FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource
				(fxmlFileName));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

	/**
	 * {@inheritDoc}
	 */
    @Override
    public void addCommand(final ToolBarInteraction interaction,
                           final IUserCommand command) {

        interactionMap.put(interaction, command);
    }
}
