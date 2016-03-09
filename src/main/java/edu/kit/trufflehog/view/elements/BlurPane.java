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

import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.SnapshotParameters;
import javafx.scene.effect.BoxBlur;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;

public class BlurPane extends StackPane {

    private ImageView imageView;

    public BlurPane() {
        imageView = new ImageView();
        imageView.setFocusTraversable(false);
        BoxBlur bb = new BoxBlur();
        bb.setWidth(8);
        bb.setHeight(8);
        bb.setIterations(3);
        imageView.setEffect(bb);
    }

    @Override protected void layoutChildren() {
        super.layoutChildren();
        if (getParent() != null && isVisible()) {
            setVisible(false);
            getChildren().remove(imageView);

            SnapshotParameters parameters = new SnapshotParameters();
            Point2D startPointInScene = this.localToScene(0, 0);

            Rectangle2D toPaint = new Rectangle2D(startPointInScene.getX(), startPointInScene.getY(),
                    getLayoutBounds().getWidth(), getLayoutBounds().getHeight());
            parameters.setViewport(toPaint);
            WritableImage image = new WritableImage(100, 100);
            image = getScene().getRoot().snapshot(parameters, image);
            imageView.setImage(image);

            getChildren().add(imageView);
            imageView.toBack();
            setClip(new Rectangle(toPaint.getWidth(), toPaint.getHeight()));
            setVisible(true);
        }
    }
}