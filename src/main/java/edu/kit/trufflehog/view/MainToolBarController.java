package edu.kit.trufflehog.view;


import edu.kit.trufflehog.command.usercommand.IUserCommand;
import edu.kit.trufflehog.interaction.ToolBarInteraction;
import edu.kit.trufflehog.view.controllers.ToolBarController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.EnumMap;
import java.util.Map;

/**
 * <p>
 *     The MainToolBarController class is the view designed for all most
 *     important functionality that should be directly accessed by the user
 *     during the running program.
 * </p>
 */
public final class MainToolBarController extends
        ToolBarController<ToolBarInteraction> {

    /** The commands that are mapped to their interactions. **/
    private final Map<ToolBarInteraction, IUserCommand> interactionMap =
            new EnumMap<>(ToolBarInteraction.class);


    /**
     * <p>
     *     Creates a new MainToolBarController with the given fxmlFileName.
     *     The fxml file has to be in the same namespace as the
     *     MainToolBarController.
     * </p>
     * @param fxmlFileName the name of the fxml file to be loaded.
     */
    public MainToolBarController(final String fxmlFileName, Node... items) {
        super(fxmlFileName, items);

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
