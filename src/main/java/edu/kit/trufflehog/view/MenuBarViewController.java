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

import edu.kit.trufflehog.command.usercommand.IUserCommand;
import edu.kit.trufflehog.interaction.MainInteraction;
import edu.kit.trufflehog.view.controllers.AnchorPaneController;
import edu.kit.trufflehog.view.controllers.MenuBarController;

import java.util.EnumMap;
import java.util.Map;

/**
 * <p>
 *     The MenuBarViewController contains the MenuBar that is used to control the entire application.
 * </p>
 *
 * @author Julian Brendl
 * @version 1.0
 */
public class MenuBarViewController extends MenuBarController<MainInteraction> {
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
    public MenuBarViewController(final String fxmlFileName) {
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

    @Override
    public final void addCommand(final MainInteraction interactor, final IUserCommand command) {
        interactionMap.put(interactor, command);
    }
}
