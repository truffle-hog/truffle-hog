package edu.kit.trufflehog.model.network.graph.components.edge;

import edu.kit.trufflehog.model.network.graph.IComponent;
import edu.kit.trufflehog.view.graph.decorators.LayeredShape;
import javafx.application.Platform;
import javafx.scene.shape.Ellipse;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.time.Instant;

/**
 * This Component of a node will handle how a node is displayed in the graph
 * e.g. it is also possible to include bitmaps etc for specific Nodes
 *
 * @author Jan Hermes
 * @version 0.1
 */
public class MulticastLayeredEdgeRendererComponent implements IRendererComponent {


    private float multiplier = 1.0f;

    private long lastUpdate = Instant.now().toEpochMilli();

    private final LayeredShape shape = new LayeredShape(4, 400, new Ellipse2D.Float(-10, -10, 20, 20));

    Color baseUnpicked = new Color(0x7f7784);
    Color basePicked = new Color(0xf0caa3);

    private float stepSize = 0.1f;
    private int currentOpacity = 255;

    public MulticastLayeredEdgeRendererComponent() {


        // TODO implement
    }

    @Override
    public Shape getShape() {

        // TODO change to current view time for replay
        final long delta = Instant.now().toEpochMilli() - lastUpdate;
        multiplier = 1.05f;

        shape.increase(multiplier);
        return shape;
    }

    @Override
    public Color getColorUnpicked() {

        currentOpacity = currentOpacity <= 10 ? 0 : currentOpacity - 10;
        return new Color(baseUnpicked.getRed(), baseUnpicked.getGreen(), baseUnpicked.getBlue(), currentOpacity);
    }

    @Override
    public Color getColorPicked() {

        currentOpacity = currentOpacity <= 10 ? 0 : currentOpacity - 10;
        return new Color(basePicked.getRed(), basePicked.getGreen(), basePicked.getBlue(), currentOpacity);
    }

    @Override
    public Stroke getStroke() {
        return null;
    }

    @Override
    public void setColorPicked(Color colorPicked) {

    }

    @Override
    public void setColorUnpicked(Color colorUnpicked) {

    }

    @Override
    public void setShape(Shape shape) {

    }

    @Override
    public void setStroke(Stroke stroke) {

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

        shape.addShape(new Ellipse2D.Float(-10, -10, 20, 20));

        currentOpacity = 255;
        //multiplier = 0;
        lastUpdate = Instant.now().toEpochMilli();
        return true;
    }
}
