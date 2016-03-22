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

import edu.kit.trufflehog.view.controllers.AnchorPaneController;
import edu.kit.trufflehog.view.elements.ImageButton;
import javafx.fxml.FXML;
import javafx.scene.control.Slider;
import javafx.scene.layout.AnchorPane;

/**
 * <p>
 * </p>
 *
 * @author Julian Brendl
 * @version 1.0
 */
public class PlaybackMenuViewController extends AnchorPaneController {

    @FXML
    private Slider slider;

    /**
     * <p>
     * Creates a new AnchorPaneController based on the given fxml file.
     * </p>
     *
     * @param fxmlFile The fxml file to create the AnchorPaneController from.
     */
    public PlaybackMenuViewController(String fxmlFile) {
        super(fxmlFile);

        final ImageButton playButton = new ImageButton("play_capture.png");
        final ImageButton pauseButton = new ImageButton("pause_capture.png");
        getChildren().addAll(playButton, pauseButton);

        playButton.setScaleX(0.7);
        playButton.setScaleY(0.7);

        AnchorPane.setTopAnchor(playButton, 1d);
        AnchorPane.setLeftAnchor(playButton, 40d);

        pauseButton.setScaleX(0.7);
        pauseButton.setScaleY(0.7);

        AnchorPane.setTopAnchor(pauseButton, 1d);
        AnchorPane.setRightAnchor(pauseButton, 40d);
    }
}
