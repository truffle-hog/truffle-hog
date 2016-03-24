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
package edu.kit.trufflehog.model.network.graph.components.edge;

import edu.kit.trufflehog.model.network.graph.components.IRenderer;
import javafx.beans.property.DoubleProperty;
import javafx.scene.paint.Color;
import javafx.scene.shape.QuadCurve;
import javafx.scene.shape.Shape;

/**
 * \brief
 * \details
 * \date 19.03.16
 * \copyright GNU Public License
 *
 * @author Jan Hermes
 * @version 0.0.1
 */
public interface IEdgeRenderer extends IRenderer {

    // TODO change to QuadCurve
    QuadCurve getLine();

    Shape getArrowShapePicked();

    void setArrowShapePicked(Shape arrowShapePicked);

    Shape getArrowShapeUnpicked();

    void setArrowShapeUnpicked(Shape arrowShapeUnpicked);

    Color getArrowDrawPicked();

    void setArrowDrawPicked(Color arrowDrawPicked);

    Color getArrowDrawUnpicked();

    void setArrowDrawUnpicked(Color arrowDrawUnpicked);

    Color getArrowFillPicked();
    Color getArrowFillUnpicked();
    void setArrowFillPicked(Color arrowFillPicked);
    void setArrowFillUnpicked(Color arrowFillUnpicked);

    Shape getArrowShape();

    DoubleProperty edgeWidthMultiplierProperty();
}
