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

import javafx.fxml.FXMLLoader;
import javafx.scene.control.MenuBar;

import java.io.IOException;

/**
 * <p>
 *     The MenuBarView contains the MenuBar that is used to control the entire application.
 * </p>
 *
 * @author Julian Brendl
 * @version 1.0
 */
public class MenuBarView extends MenuBar {

    /**
     * <p>
     *     Creates a new MainView with the given fxmlFileName. The fxml file has to be in the same namespace
     *     as the MainView.
     * </p>
     *
     * @param fxmlFileName the name of the fxml file to be loaded.
     */
    public MenuBarView(final String fxmlFileName) {
        final FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlFileName));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    /**
     * <p>
     *     Execute the routine for quitting the application.
     * </p>
     */
    public void onExit() {
        throw new UnsupportedOperationException("Not implemented yet!");
    }
}
