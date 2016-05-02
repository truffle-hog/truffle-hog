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
 *  You should have received a copy of the GNU General Public License
 *  along with TruffleHog.  If not, see <http://www.gnu.org/licenses/>.
 */
package edu.kit.trufflehog.view.jung.visualization;

import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * \brief Listeners for making the scene's canvas draggable and zoomable
 * \details
 * \date 22.03.16
 * \copyright GNU Public License
 *
 * @author Jan Hermes
 * @version 0.0.1
 */
public class SceneGestures {

    private static Logger logger = LogManager.getLogger();

    private static final double MAX_SCALE = 30.0d;
    private static final double MIN_SCALE = .001d;

    private DragContext sceneDragContext = new DragContext();

    PannableCanvas canvas;

    Pane parent;

    public SceneGestures(Pane parent, PannableCanvas canvas) {

        this.parent = parent;
        this.canvas = canvas;
    }

    public EventHandler<MouseEvent> getOnMousePressedEventHandler() {
        return onMousePressedEventHandler;
    }

    public EventHandler<MouseEvent> getOnMouseDraggedEventHandler() {
        return onMouseDraggedEventHandler;
    }

    public EventHandler<ScrollEvent> getOnScrollEventHandler() {
        return onScrollEventHandler;
    }

    private EventHandler<MouseEvent> onMousePressedEventHandler = new EventHandler<MouseEvent>() {

        public void handle(MouseEvent event) {

            // right mouse button => panning
            if( !event.isSecondaryButtonDown())
                return;

            sceneDragContext.mouseAnchorX = event.getSceneX();
            sceneDragContext.mouseAnchorY = event.getSceneY();

            sceneDragContext.translateAnchorX = canvas.getTranslateX();
            sceneDragContext.translateAnchorY = canvas.getTranslateY();
        }
    };

    private EventHandler<MouseEvent> onMouseDraggedEventHandler = new EventHandler<MouseEvent>() {
        public void handle(MouseEvent event) {

            // right mouse button => panning
            if( !event.isSecondaryButtonDown())
                return;

            canvas.getGhost().setTranslateX(sceneDragContext.translateAnchorX + event.getSceneX() - sceneDragContext.mouseAnchorX);
            canvas.getGhost().setTranslateY(sceneDragContext.translateAnchorY + event.getSceneY() - sceneDragContext.mouseAnchorY);

            canvas.setTranslateX(sceneDragContext.translateAnchorX + event.getSceneX() - sceneDragContext.mouseAnchorX);
            canvas.setTranslateY(sceneDragContext.translateAnchorY + event.getSceneY() - sceneDragContext.mouseAnchorY);

            event.consume();
        }
    };

    /**
     * Mouse wheel handler: zoom to pivot point
     */
    private EventHandler<ScrollEvent> onScrollEventHandler = new EventHandler<ScrollEvent>() {

        @Override
        public void handle(ScrollEvent event) {

            /*double scaleDelta = 1.2;
            double factor = (event.getDeltaY() > 0) ? scaleDelta : 1 / scaleDelta;

            final Point2D center = canvas.localToParent(event.getX(), event.getY());
            final Bounds bounds = canvas.getBoundsInParent();
            final double w = bounds.getWidth();
            final double h = bounds.getHeight();

            final double dw = w * (factor - 1);
            final double xr = 2 * (w / 2 - (center.getX() - bounds.getMinX())) / w;

            final double dh = h * (factor - 1);
            final double yr = 2 * (h / 2 - (center.getY() - bounds.getMinY())) / h;

            canvas.setScaleX(canvas.getScaleX() * factor);
            canvas.setScaleY(canvas.getScaleY() * factor);
            canvas.setTranslateX(canvas.getTranslateX() + xr * dw / 2);
            canvas.setTranslateY(canvas.getTranslateY() + yr * dh / 2);*/

            //FIXME fix scrolling/scaling

            double delta = 1.2;

            double scale = canvas.getScale(); // currently we only use Y, same value is used for X
            double oldScale = scale;

            if (event.getDeltaY() < 0) {
                scale /= delta;
            } else {
                scale *= delta;
            }

            double f = (scale / oldScale) - 1;

            double dx = (event.getSceneX() - (canvas.getGhost().getBoundsInParent().getWidth() / 2 + canvas.getGhost().getBoundsInParent().getMinX()));
            double dy = (event.getSceneY() - (canvas.getGhost().getBoundsInParent().getHeight() / 2 + canvas.getGhost().getBoundsInParent().getMinY()));

            //logger.debug(canvas.getBoundsInParent());
            //canvas.setPrefHeight(600 / canvas.getScale());
            //canvas.setPrefWidth(600 / canvas.getScale());

            canvas.setScale( scale);

            // note: pivot value must be untransformed, i. e. without scaling
            canvas.setPivot(f * dx, f * dy);
            event.consume();
        }

    };


    public static double clamp( double value, double min, double max) {

        if( Double.compare(value, min) < 0)
            return min;

        if( Double.compare(value, max) > 0)
            return max;

        return value;
    }
}