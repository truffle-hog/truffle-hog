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
import edu.kit.trufflehog.interaction.OverlayInteraction;
import edu.kit.trufflehog.model.filter.FilterInput;
import edu.kit.trufflehog.model.filter.FilterType;
import edu.kit.trufflehog.view.controllers.GridPaneController;
import edu.kit.trufflehog.view.elements.FilterOverlayMenu;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

/**
 * <p>
 *     The AddFilterMenuViewController is an overlay that slides in from the top center, similar to menus that ask you
 *     if you are really sure that you want to quit the app. It has a show and a hide method, both of which trigger the
 *     respective animations. It is used to add new filters.
 * </p>
 *
 * @author Julian Brendl
 * @version 1.0
 */
public class AddFilterMenuViewController extends GridPaneController<OverlayInteraction> {
    private final FilterOverlayMenu filterOverlayMenu;
    private final TranslateTransition transitioShow;
    private final TranslateTransition transitionHide;
    private final OverlayViewController viewController;
    private final StackPane stackPane;

    // FXML variables
    @FXML
    private Button createButton;
    @FXML
    private Button cancelButton;
    @FXML
    private TextField nameTextField;

    /**
     * <p>
     *     Creates a new SlideInOverlay. A SlideInOverlay is an overlay that slides in from the top center, similar to
     *     menus that ask you if you are really sure that you want to quit the app.
     * </p>
     *
     * @param stackPane The stackPane to put the menu on.
     * @param fxml The fxml to load.
     */
    public AddFilterMenuViewController(StackPane stackPane, String fxml, FilterOverlayMenu filterOverlayMenu) {
        super(fxml);
        this.filterOverlayMenu = filterOverlayMenu;
        this.viewController = new OverlayViewController(fxml);
        this.stackPane = stackPane;
        this.stackPane.getChildren().add(viewController);
        StackPane.setAlignment(viewController, Pos.TOP_CENTER);

        // Set up transition animation to show menu
        transitioShow = new TranslateTransition(Duration.seconds(0.5), viewController);
        transitioShow.setFromY(-450);
        transitioShow.setToY(0);

        // Set up transition animation to hide menu
        transitionHide = new TranslateTransition(Duration.seconds(0.5), viewController);
        transitionHide.setFromY(0);
        transitionHide.setToY(-450);

        // Hide by default
        stackPane.setVisible(false);
        viewController.setVisible(false);

        // Set the add filter mechanic
        createButton.setOnAction(eventHandler ->  {
            FilterInput filterInput = createFilterInput();
            if (filterInput != null) {
                filterOverlayMenu.addFilter(filterInput);
                hideMenu();
            }
        });

        cancelButton.setOnAction(eventHandler -> hideMenu());
    }

    /**
     * <p>
     *     Shows the menu: starts the slide in animation.
     * </p>
     */
    public void showMenu() {
        StackPane.setAlignment(viewController, Pos.TOP_CENTER);
        stackPane.setVisible(true);
        viewController.setVisible(true);
        transitioShow.play();
    }

    /**
     * <p>
     *     Hides the menu: starts the slide out animation.
     * </p>
     */
    public void hideMenu() {
        stackPane.setVisible(false);
        transitionHide.play();
    }

    private FilterInput createFilterInput() {
        return new FilterInput("Filter A", FilterType.BLACKLIST, null, null);
    }

    @Override
    public void addCommand(OverlayInteraction interaction, IUserCommand command) {

    }

    public void test() {
        System.out.println(nameTextField.getText());
    }
}
