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

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Orientation;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;

import java.io.IOException;

/**
 * <p>
 *     The MenuBarViewController contains the MenuBar that is used to control the entire application.
 * </p>
 *
 * @author Julian Brendl
 * @version 1.0
 */
public class MenuBarViewController extends MenuBar {

    private final ViewSplitter viewSplitter;

    @FXML
    private MenuItem splitHMenuItem;
    @FXML
    private MenuItem splitVMenuItem;
    @FXML
    private MenuItem deleteMenuItem;

    /**
     * <p>
     *     Creates a new MainViewController with the given fxmlFileName. The fxml file has to be in the same namespace
     *     as the MainViewController.
     * </p>
     *
     * @param fxmlFileName the name of the fxml file to be loaded.
     */
    public MenuBarViewController(final String fxmlFileName, final ViewSplitter viewSplitter) {
        final FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlFileName));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        this.viewSplitter = viewSplitter;

        setUpViewSwitchingButtons();
    }

    private void setUpViewSwitchingButtons() {
        splitHMenuItem.setOnAction(event -> viewSplitter.split(Orientation.HORIZONTAL));
        splitVMenuItem.setOnAction(event -> viewSplitter.split(Orientation.VERTICAL));
        deleteMenuItem.setOnAction(event -> viewSplitter.merge());
    }

    /**
     * <p>
     *     Execute the routine for quitting the application.
     * </p>
     */
    public void onExit() {

    }
}
