package edu.kit.trufflehog.model.network.graph.components.node;

import edu.kit.trufflehog.model.network.graph.IUpdater;
import edu.kit.trufflehog.model.network.graph.components.IRenderer;
import edu.kit.trufflehog.util.ICopyCreator;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;
import org.apache.logging.log4j.Logger;

/**
 * This Component of a node will handle how a node is displayed in the graph
 * e.g. it is also possible to include bitmaps etc for specific Nodes
 *
 * @author Jan Hermes
 * @version 0.1
 */
public class NodeRenderer implements IRenderer {

    private static final Logger logger = org.apache.logging.log4j.LogManager.getLogger(NodeRenderer.class);

//    private Icon iconUnpicked = null;
//    private Icon iconPicked = null;


    private Color colorPicked = Color.web("0x4089BF90");
    private Color colorUnpicked = Color.web("0x4089BF43");// = new Color(0x4089BFa0, true);

    private Shape shape = new Circle(50); // = new Ellipse2D.Double(-30, -30, 60, 60);

    private Color drawPicked = Color.web("0xC6E9FF90");
    private Color drawUnpicked = Color.web("0x4089BF10");

    private ObjectProperty<Paint> fillPaintProperty = new SimpleObjectProperty<>(colorUnpicked);
    private ObjectProperty<Paint> strokePaintProperty = new SimpleObjectProperty<>(drawUnpicked);
    private boolean isPicked;

    /**
     * <p>
     *     Creates a component using default values.
     * </p>
     */
    public NodeRenderer() {

        shape.fillProperty().bind(fillPaintProperty);
        shape.strokeProperty().bind(strokePaintProperty);
        shape.setStrokeWidth(6);
        //new java.awt.Color(0x47F4CB);

    }

    /**
     * <p>
     *     Creates a component with provided shape and colors.
     * </p>
     * @param shape The desired shape of the node
     * @param colorPicked The color to use if the node is selected
     * @param colorUnpicked The color to use if the node is not selected
     */
    public NodeRenderer(Shape shape, Color colorPicked, Color colorUnpicked) {
        if (shape == null) throw new NullPointerException("shape must not be null!");
        if (colorPicked == null) throw new NullPointerException("colorPicked must not be null!");
        if (colorUnpicked == null) throw new NullPointerException("colorUnpicked must not be null!");
        this.shape = shape;
        this.colorPicked = colorPicked;
        this.colorUnpicked = colorUnpicked;
    }

    @Override
    public Color getDrawUnpicked() {
        return drawUnpicked;
    }
    @Override
    public void setDrawUnpicked(Color drawUnpicked) {
        this.drawUnpicked = drawUnpicked;
    }
    @Override
    public Color getDrawPicked() {
        return drawPicked;
    }
    @Override
    public void setDrawPicked(Color drawPicked) {
        this.drawPicked = drawPicked;
    }

    @Override
    public void togglePicked() {

        if (isPicked) {

            isPicked(false);
        } else {
            isPicked(true);
        }



    }

    @Override
    public void isPicked(boolean b) {
        isPicked = b;
        if (b) {

            Platform.runLater(() -> {

                fillPaintProperty.set(colorPicked);
                strokePaintProperty.set(drawPicked);
            });

        } else {
            Platform.runLater(() -> {

                fillPaintProperty.set(colorUnpicked);
                strokePaintProperty.set(drawUnpicked);

            });
        }
    }

    @Override
    public boolean picked() {
        return isPicked;
    }


    /**
     * <p>
     *     Gets the current shape of the node.
     * </p>
     * @return shape
     */
    public Shape getShape() {
        return shape;
    }

    /**
     * <p>
     *     Gets the color used if the node is selected.
     * </p>
     * @return color
     */
    public Color getColorPicked() {
        return colorPicked;
    }

    /**
     * <p>
     *     Gets the color used if the node is not selected.
     * </p>
     * @return color
     */
    public Color getColorUnpicked() {
        return colorUnpicked;
    }

    /**
     * <p>
     *     Sets the current shape of the node (must not be null!).
     * </p>
     * @param shape The shape to be used
     */
    public void setShape(Shape shape) {
        if (shape == null) { throw new NullPointerException("shape must not be null!"); }
        this.shape = shape;
    }

    @Override
    public void animate() {

    }

    /**
     * <p>
     *     Sets the color to use if node is selected.
     * </p>
     * @param colorPicked
     */
    public void setColorPicked(Color colorPicked) {
        if (colorPicked == null) { throw new NullPointerException("colorPicked must not be null!"); }
        this.colorPicked = colorPicked;
    }

    /**
     * <p>
     *     Sets the color to use if node is not selected.
     * </p>
     * @param colorUnpicked
     */
    public void setColorUnpicked(Color colorUnpicked) {
        if (colorUnpicked == null) { throw new NullPointerException("colorUnpicked must not be null!"); }
        this.colorUnpicked = colorUnpicked;
    }

    @Override
    public boolean isMutable() {
        return true;
    }

    @Override
    public IRenderer createDeepCopy(ICopyCreator copyCreator) {
        if (copyCreator == null) throw new NullPointerException("copyCreator must not be null!");
        return copyCreator.createDeepCopy(this);
    }

    @Override
    public boolean update(IRenderer instance, IUpdater updater) {
        if (instance == null) throw new NullPointerException("instance must not be null!");
        if (updater == null) throw new NullPointerException("updater must not be null!");
        return updater.update(this, instance);
    }

    @Override
    public boolean equals(Object o) {
        return (o instanceof NodeRenderer);
    }
}