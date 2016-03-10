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

package edu.kit.trufflehog.view.elements;

import edu.kit.trufflehog.view.OverlayViewController;
import javafx.animation.TranslateTransition;
import javafx.geometry.Pos;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

/**
 * <p>
 * </p>
 *
 * @author Julian Brendl
 * @version 1.0
 */
public class AddFilterOverlayMenu {
    private final TranslateTransition addFilterMenuTransitionShow;
    private final TranslateTransition addFilterMenuTransitionHide;
    private final OverlayViewController addFilterOverlayViewController;
    private final StackPane stackPane;


    public AddFilterOverlayMenu(AnchorPane groundView) {
        addFilterOverlayViewController = new OverlayViewController("add_filter_menu_overlay.fxml");
        stackPane = new StackPane();
        stackPane.setMaxHeight(300);
        stackPane.setMaxHeight(300);

        AnchorPane.setTopAnchor(stackPane, 0d);
        AnchorPane.setLeftAnchor(stackPane, 0d);
        AnchorPane.setRightAnchor(stackPane, 0d);
        AnchorPane.setBottomAnchor(stackPane, 0d);

        groundView.getChildren().add(stackPane);
        stackPane.getChildren().add(addFilterOverlayViewController);
        StackPane.setAlignment(addFilterOverlayViewController, Pos.TOP_CENTER);

        // Set up transition animation to show menu
        addFilterMenuTransitionShow = new TranslateTransition(Duration.seconds(0.5), addFilterOverlayViewController);
        addFilterMenuTransitionShow.setFromY(-400);
        addFilterMenuTransitionShow.setToY(0);

        // Set up transition animation to hide menu
        addFilterMenuTransitionHide = new TranslateTransition(Duration.seconds(0.5), addFilterOverlayViewController);
        addFilterMenuTransitionHide.setFromY(0);
        addFilterMenuTransitionHide.setToY(-450);

        // Hide by default
        stackPane.setVisible(false);
        addFilterOverlayViewController.setVisible(false);
    }

    public void showMenu() {
        stackPane.setVisible(true);
        addFilterOverlayViewController.setVisible(true);
        addFilterMenuTransitionShow.play();
    }

    public void hideMenu() {
        stackPane.setVisible(false);
        addFilterMenuTransitionHide.play();
    }
}
