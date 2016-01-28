package edu.kit.trufflehog.view;

import edu.kit.trufflehog.command.usercommand.IUserCommand;
import edu.kit.trufflehog.interaction.MainInteraction;
import edu.kit.trufflehog.interaction.StatisticsInteraction;
import javafx.fxml.FXMLLoader;

import java.io.IOException;
import java.util.EnumMap;
import java.util.Map;

/**
 * <p>
 *     The StatisticsViewController provides GUI components and functionality
 *     for every statistic view.
 * </p>
 */
public class StatisticsViewController extends BorderPaneController<StatisticsInteraction> {

	/** The commands that are mapped to their interactions. **/
    private final Map<StatisticsInteraction, IUserCommand> interactionMap =
            new EnumMap<>(StatisticsInteraction.class);

    /**
     * <p>
     *     Creates a new StatisticsViewController with the given fxmlFileName.
     *     The fxml file has to be in the same namespace as the
     *     StatisticsViewController.
     * </p>
     * @param fxmlFileName the name of the fxml file to be loaded.
     */
    public StatisticsViewController(final String fxmlFileName) {

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
    public final void addCommand(final StatisticsInteraction interactor, final
    IUserCommand
            command) {

        interactionMap.put(interactor, command);
    }
}
