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

import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

/**
 * <p>
 *     This class is a custom JavaFX {@link Button} that nicely displays an icon as a button.
 * </p>
 */
public class ImageButton extends Button {
    private final String STYLE_NORMAL = "-fx-background-color: transparent; -fx-opacity: 0.87; -fx-padding: 5, 5, 5, 5;";
    private final String STYLE_PRESSED = "-fx-background-color: transparent; -fx-opacity: 0.65; -fx-padding: 5, 6, 6, 5;";

    /**
     * <p>
     *     Creates a new GlowImageButton.
     * </p>
     *
     * @param image The path to the image to display as the button.
     */
    public ImageButton(String image) {
        setGraphic(image);
        setStyle(STYLE_NORMAL);

        setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                setStyle(STYLE_PRESSED);
            }
        });

        setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                setStyle(STYLE_NORMAL);
            }
        });
    }

    /**
     * <p>
     *     Sets the image of the image button.
     * </p>
     *
     * @param image The path to the image for this image button.
     */
    public void setGraphic(String image) {
        setGraphic(new ImageView(new Image(getClass().getResourceAsStream(image))));
    }
}