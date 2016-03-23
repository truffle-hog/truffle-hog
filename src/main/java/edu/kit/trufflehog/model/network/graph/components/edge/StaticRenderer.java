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

import edu.kit.trufflehog.model.network.graph.IUpdater;
import edu.kit.trufflehog.model.network.graph.components.IRenderer;
import edu.kit.trufflehog.util.ICopyCreator;

/**
 * \brief
 * \details
 * \date 05.03.16
 * \copyright GNU Public License
 *
 * @author Jan Hermes
 * @version 0.0.1
 */
public class StaticRenderer implements IRenderer {
    @Override
    public javafx.scene.shape.Shape getShape() {
        throw new UnsupportedOperationException("Operation not implemented yet");
    }

    @Override
    public javafx.scene.paint.Color getColorUnpicked() {
        throw new UnsupportedOperationException("Operation not implemented yet");
    }

    @Override
    public javafx.scene.paint.Color getColorPicked() {
        throw new UnsupportedOperationException("Operation not implemented yet");
    }

    @Override
    public void setColorPicked(javafx.scene.paint.Color colorPicked) {
        throw new UnsupportedOperationException("Operation not implemented yet");
    }

    @Override
    public void setColorUnpicked(javafx.scene.paint.Color colorUnpicked) {
        throw new UnsupportedOperationException("Operation not implemented yet");
    }

    @Override
    public void setShape(javafx.scene.shape.Shape shape) {
        throw new UnsupportedOperationException("Operation not implemented yet");
    }

    @Override
    public void animate() {
        throw new UnsupportedOperationException("Operation not implemented yet");
    }

    @Override
    public javafx.scene.paint.Color getDrawUnpicked() {
        throw new UnsupportedOperationException("Operation not implemented yet");
    }

    @Override
    public void setDrawUnpicked(javafx.scene.paint.Color drawUnpicked) {
        throw new UnsupportedOperationException("Operation not implemented yet");
    }

    @Override
    public javafx.scene.paint.Color getDrawPicked() {
        throw new UnsupportedOperationException("Operation not implemented yet");
    }

    @Override
    public void setDrawPicked(javafx.scene.paint.Color drawPicked) {
        throw new UnsupportedOperationException("Operation not implemented yet");
    }

    @Override
    public void togglePicked() {
        throw new UnsupportedOperationException("Operation not implemented yet");
    }

    @Override
    public void isPicked(boolean b) {
        throw new UnsupportedOperationException("Operation not implemented yet");
    }

    @Override
    public boolean picked() {
        throw new UnsupportedOperationException("Operation not implemented yet");
    }

    @Override
    public IRenderer createDeepCopy(ICopyCreator copyCreator) {
        throw new UnsupportedOperationException("Operation not implemented yet");
    }

    @Override
    public boolean isMutable() {
        throw new UnsupportedOperationException("Operation not implemented yet");
    }

    @Override
    public boolean update(IRenderer instance, IUpdater updater) {
        throw new UnsupportedOperationException("Operation not implemented yet");
    }

/*    private Shape shape;
    private Color picked;
    private Color unpicked;
    private Stroke stroke;

    public StaticRenderer(Shape shape, Color colorPicked, Color colorUnpicked, Stroke stroke) {
        if (colorPicked == null) throw new NullPointerException("colorPicked must not be null!");
        if (colorUnpicked == null) throw new NullPointerException("colorUnpicked must not be null!");
        if (shape == null) throw new NullPointerException("shape must not be null!");
        if (stroke == null) throw new NullPointerException("stroke must not be null!");

        this.shape = shape;
        this.picked = colorPicked;
        this.unpicked = colorUnpicked;
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
        if (colorPicked == null) throw new NullPointerException("colorPicked must not be null!");
        this.picked = colorPicked;
    }

    @Override
    public void setColorUnpicked(Color colorUnpicked) {
        if (colorUnpicked == null) throw new NullPointerException("colorUnpicked must not be null!");
        this.unpicked = colorUnpicked;
    }

    @Override
    public void setShape(Shape shape) {
        if (shape == null) throw new NullPointerException("shape must not be null!");
        this.shape = shape;
    }

    @Override
    public void setStroke(Stroke stroke) {
        if (stroke == null) throw new NullPointerException("stroke must not be null!");
        this.stroke = stroke;
    }

    @Override
    public void animate() {
        throw new UnsupportedOperationException("Operation not implemented yet");
    }

    @Override
    public int animationTime() {
        throw new UnsupportedOperationException("Operation not implemented yet");
    }

    @Override
    public Icon getIconPicked() {
        throw new UnsupportedOperationException("Operation not implemented yet");
    }

    @Override
    public void setIconPicked(Icon icon) {
        throw new UnsupportedOperationException("Operation not implemented yet");
    }

    @Override
    public Color getDrawUnpicked() {
        throw new UnsupportedOperationException("Operation not implemented yet");
    }

    @Override
    public void setDrawUnpicked(Color drawUnpicked) {
        throw new UnsupportedOperationException("Operation not implemented yet");
    }

    @Override
    public Color getDrawPicked() {
        throw new UnsupportedOperationException("Operation not implemented yet");
    }

    @Override
    public void setDrawPicked(Color drawPicked) {
        throw new UnsupportedOperationException("Operation not implemented yet");
    }

    @Override
    public Icon getIconUnpicked() {
        throw new UnsupportedOperationException("Operation not implemented yet");
    }

    @Override
    public void setIconUnpicked(Icon icon) {
        throw new UnsupportedOperationException("Operation not implemented yet");
    }

    @Override
    public void addCachedShape(Shape transformedShape) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public Shape getCachedShape() {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public Shape getShape(double relation) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public IRenderer createDeepCopy(ICopyCreator copyCreator) {
        if (copyCreator == null) throw new NullPointerException("copyCreator must not be null!");
        return copyCreator.createDeepCopy(this);
    }

    @Override
    public boolean isMutable() {
        return true;
    }

    @Override
    public boolean update(IRenderer instance, IUpdater updater) {
        if (instance == null) throw new NullPointerException("instance must not be null!");
        if (updater == null) throw new NullPointerException("updater must not be null!");

        return updater.update(this, instance);
    }

    @Override
    public boolean equals(Object o) {
        return (o instanceof StaticRenderer);
    }*/
}
