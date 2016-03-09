package edu.kit.trufflehog.model.network.graph.components.edge;

import edu.kit.trufflehog.model.network.graph.IComponent;
import edu.kit.trufflehog.model.network.graph.IUpdater;
import edu.kit.trufflehog.model.network.graph.components.IRendererComponent;
import edu.kit.trufflehog.util.ICopyCreator;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.time.Instant;

/**
 * This Component of a node will handle how a node is displayed in the graph
 * e.g. it is also possible to include bitmaps etc for specific Nodes
 *
 * @author Jan Hermes
 * @version 0.1
 *
 * //TODO FIX GETTER!!!
 */
public class MulticastEdgeRendererComponent implements IRendererComponent {

    private long lastUpdate = Instant.now().toEpochMilli();

    private Shape shape = new Ellipse2D.Float(-10, -10, 20, 20);

    Color colorUnpicked = new Color(0x7f7784);
    Color colorPicked = new Color(0xf0caa3);

    private float strokeWidth = 5f;
    private float multiplier = 1.05f;
    private float opacity = 170;

    private Stroke stroke = new BasicStroke(strokeWidth);

    public MulticastEdgeRendererComponent() {

        // TODO implement
    }

    @Override
    public Shape getShape() {

        return AffineTransform.getScaleInstance(multiplier, multiplier).createTransformedShape(shape);
    }

    @Override
    public Color getColorUnpicked() {

        return new Color(colorUnpicked.getRed(), colorUnpicked.getGreen(), colorUnpicked.getBlue(), (int) opacity);
    }

    @Override
    public Color getColorPicked() {

        return new Color(colorPicked.getRed(), colorPicked.getGreen(), colorPicked.getBlue(), (int) opacity);
    }

    @Override
    public Stroke getStroke() {
        return new BasicStroke(strokeWidth);
    }

    @Override
    public void setColorPicked(Color colorPicked) {
        if (colorPicked == null) throw new NullPointerException("colorPicked must not be null!");
        this.colorPicked = colorPicked;
    }

    @Override
    public void setColorUnpicked(Color colorUnpicked) {
        if (colorUnpicked == null) throw new NullPointerException("colorUnpicked must not be null!");
        this.colorUnpicked = colorUnpicked;
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
    public void updateState() {
        strokeWidth *= 1.4f;
        opacity = opacity <= 10 ? 0 : opacity * 0.8f;
        if (multiplier >= 70.0f) {
            multiplier = 0;
        }
        multiplier *= 1.3f;
    }


    @Override
    public String name() {
        return "Multicast Edge Renderer";
    }

    @Override
    public boolean isMutable() {
        return true;
    }

    public float getOpacity() {
        return opacity;
    }

    public void setOpacity(float opacity) {
        this.opacity = opacity;
    }

    public long getLastUpdate() {

        return lastUpdate;
    }

    public void setLastUpdate(long lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    @Override
    public IComponent createDeepCopy(ICopyCreator copyCreator) {
        if (copyCreator == null) throw new NullPointerException("copyCreator must not be null!");
        return copyCreator.createDeepCopy(this);
    }

    @Override
    public boolean update(IComponent instance, IUpdater updater) {
        if (instance == null) throw new NullPointerException("instance must not be null!");
        if (updater == null) throw new NullPointerException("updater must not be null!");
        return updater.update(this, instance);
    }

    @Override
    public boolean equals(Object o) {
        return (o instanceof MulticastEdgeRendererComponent);
    }

    public void setStrokeWidth(float strokeWidth) {
        this.strokeWidth = strokeWidth;
    }

    public void setMultiplier(float multiplier) {
        this.multiplier = multiplier;
    }
}
