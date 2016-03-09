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

import edu.kit.trufflehog.model.network.graph.IComponent;
import edu.kit.trufflehog.model.network.graph.IUpdater;
import edu.kit.trufflehog.model.network.graph.components.IRendererComponent;
import edu.kit.trufflehog.util.ICopyCreator;

import java.awt.Color;
import java.awt.Shape;
import java.awt.Stroke;

/**
 * \brief
 * \details
 * \date 05.03.16
 * \copyright GNU Public License
 *
 * @author Jan Hermes
 * @version 0.0.1
 */
public class StaticRendererComponent implements IRendererComponent {

    private Shape shape;
    private Color picked;
    private Color unpicked;
    private Stroke stroke;

    public StaticRendererComponent(Shape shape, Color picked, Color unpicked, Stroke stroke) {

        this.shape = shape;
        this.picked = picked;
        this.unpicked = unpicked;
        this.stroke = stroke;
    }

    @Override
    public Shape getShape() {
        return shape;
    }

    @Override
    public Color getColorUnpicked() {
        return unpicked;
    }

    @Override
    public Color getColorPicked() {
        return picked;
    }

    @Override
    public Stroke getStroke() {
        return stroke;
    }

    @Override
    public void setColorPicked(Color colorPicked) {

        this.picked = colorPicked;
    }

    @Override
    public void setColorUnpicked(Color colorUnpicked) {

        this.unpicked = colorUnpicked;
    }

    @Override
    public void setShape(Shape shape) {

        this.shape = shape;
    }

    @Override
    public void setStroke(Stroke stroke) {

        this.stroke = stroke;
    }

    @Override
    public String name() {
        return "Static Renderer Component";
    }

    @Override
    public IComponent createDeepCopy(ICopyCreator copyCreator) {
        return null;
    }

    @Override
    public boolean isMutable() {
        return true;
    }

    @Override
    public boolean update(IComponent instance, IUpdater updater) {
        return false;
    }
}
