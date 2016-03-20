package edu.kit.trufflehog.view;

import edu.kit.trufflehog.interaction.IInteraction;
import edu.kit.trufflehog.view.controllers.AnchorPaneInteractionController;

/**
 * <p>
 * </p>
 *
 * @author Julian Brendl
 * @version 1.0
 */
public abstract class SplitableView<I extends IInteraction> extends AnchorPaneInteractionController<I> {

    /**
     * <p>
     *     Creates a new SplitableView based on the given fxml file.
     * </p>
     *
     * @param fxmlFile The fxml file to create the AnchorPaneController from.
     */
    public SplitableView(String fxmlFile) {
        super(fxmlFile);
    }

    /**
     * <p>
     *     Shows the close button if show is set to true and hides the show button if show is set to false.
     * </p>
     *
     * @param show Determines whether the close button is shown or not.
     */
    public abstract void showCloseButton(boolean show);

    public abstract AnchorPaneInteractionController clone();
}
