package edu.kit.trufflehog.model.network.graph.components.edge;

import edu.kit.trufflehog.model.network.graph.IComponent;

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
 */
public class MulticastEdgeRendererComponent implements IRendererComponent {

    private long lastUpdate = Instant.now().toEpochMilli();

    private final Shape shape = new Ellipse2D.Float(-10, -10, 20, 20);

    Color baseUnpicked = new Color(0x7f7784);
    Color basePicked = new Color(0xf0caa3);

    private float currentOpacity = 170;
    private float multiplier = 1f;
    private float strokeWidth = 5f;

    public MulticastEdgeRendererComponent() {

        // TODO implement
    }

    @Override
    public Shape getShape() {

        if (multiplier >= 70.0f) {
            multiplier = 0;
        }
        multiplier *= 1.3f;

        return AffineTransform.getScaleInstance(multiplier, multiplier).createTransformedShape(shape);
    }

    @Override
    public Color getColorUnpicked() {

        currentOpacity = currentOpacity <= 10 ? 0 : currentOpacity * 0.8f;
        return new Color(baseUnpicked.getRed(), baseUnpicked.getGreen(), baseUnpicked.getBlue(), (int) currentOpacity);
    }

    @Override
    public Color getColorPicked() {

        currentOpacity = currentOpacity <= 10 ? 0 : currentOpacity * 0.8f;
        return new Color(basePicked.getRed(), basePicked.getGreen(), basePicked.getBlue(), (int) currentOpacity);
    }

    @Override
    public Stroke getStroke() {

        strokeWidth *= 1.4f;

        return new BasicStroke(strokeWidth);
    }


    @Override
    public String name() {
        return "Multicast Edge Renderer";
    }

    @Override
    public boolean isMutable() {
        return true;
    }

    @Override
    public IComponent createDeepCopy() {
        return null;
    }

    @Override
    public boolean update(IComponent update) {

        strokeWidth = 5f;
        multiplier = 1.05f;
        currentOpacity = 170;

        lastUpdate = Instant.now().toEpochMilli();
        return true;
    }
}
