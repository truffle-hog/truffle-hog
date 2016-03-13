package edu.kit.trufflehog.view;

import edu.kit.trufflehog.view.controllers.BorderPaneController;

/**
 * <p>
 *     The StatisticsViewController provides GUI components and functionality for every statistic view.
 * </p>
 */
public class StatisticsViewController extends BorderPaneController {

    /**
     * <p>
     *     Creates a new StatisticsViewController with the given fxmlFileName. The fxml file has to be in the same
     *     namespace as the StatisticsViewController.
     * </p>
     *
     * @param fxmlFileName The name of the fxml file to be loaded.
     */
    public StatisticsViewController(final String fxmlFileName) {
        super(fxmlFileName);
    }
}
