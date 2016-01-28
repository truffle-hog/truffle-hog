package edu.kit.trufflehog.view;

import edu.kit.trufflehog.command.usercommand.IUserCommand;
import edu.kit.trufflehog.interaction.MainInteraction;
import javafx.fxml.FXMLLoader;

import java.io.IOException;
import java.util.EnumMap;
import java.util.Map;

/**
 * <p>
 *     The MainViewController incorporates all GUI elements that belong to the
 *     primary scope of the application. This for example includes the top
 *     Menu Bar.
 * </p>
 */
public class MainViewController extends BorderPaneController<MainInteraction> {

	/** The commands that are mapped to their interactions. **/
    private final Map<MainInteraction, IUserCommand> interactionMap =
            new EnumMap<>(MainInteraction.class);

    /**
     * <p>
     *     Creates a new MainViewController with the given fxmlFileName.
     *     The fxml file has to be in the same namespace as the
     *     MainViewController.
     * </p>
     * @param fxmlFileName the name of the fxml file to be loaded.
     */
    public MainViewController(final String fxmlFileName) {

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
     * Execute the routine for quitting the application.
     */
    public void onExit() {


    }


    /**
     * {@inheritDoc}
     */
    @Override
    public final void addCommand(final MainInteraction interactor, final
    IUserCommand
            command) {

        interactionMap.put(interactor, command);
    }
}
