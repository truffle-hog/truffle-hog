/*
 * This file is part of TruffleHog.
 *
 * TruffleHog is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * TruffleHog is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with TruffleHog.  If not, see <http://www.gnu.org/licenses/>.
 */

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
public abstract class SplitableView<I extends IInteraction> extends AnchorPaneInteractionController<I> implements BaseView {

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

    public abstract SplitableView clone();
}
