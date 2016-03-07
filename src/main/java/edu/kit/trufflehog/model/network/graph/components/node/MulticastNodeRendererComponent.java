package edu.kit.trufflehog.model.network.graph.components.node;

import com.sun.istack.internal.NotNull;
import edu.kit.trufflehog.model.network.graph.IComponent;
import edu.kit.trufflehog.model.network.graph.INode;

import java.awt.*;
import java.awt.geom.Rectangle2D;

/**
 * This Component of a node will handle how a node is displayed in the graph
 * e.g. it is also possible to include bitmaps etc for specific Nodes
 *
 * @author Jan Hermes
 * @version 0.1
 *
 * TODO: reconsider default values
 */
public class MulticastNodeRendererComponent implements IComponent {

    private Shape shape = new Rectangle2D.Double(-50,-50,100,100);
    private Color colorPicked = new Color(0xf0caa3);
    private Color colorUnpicked = new Color(0xab7d63);

    /**
     * <p>
     *     Creates a component using default values.
     * </p>
     */
    public MulticastNodeRendererComponent() {

    }

    /**
     * <p>
     *     Creates a component with provided shape and colors.
     * </p>
     * @param shape The desired shape of the node
     * @param colorPicked The color to use if the node is selected
     * @param colorUnpicked The color to use if the node is not selected
     */
    public MulticastNodeRendererComponent(@NotNull Shape shape, @NotNull Color colorPicked, @NotNull Color colorUnpicked) {
        if (shape == null) throw new NullPointerException("shape must not be null!");
        if (colorPicked == null) throw new NullPointerException("colorPicked must not be null!");
        if (colorUnpicked == null) throw new NullPointerException("colorUnpicked must not be null!");
        this.shape = shape;
        this.colorPicked = colorPicked;
        this.colorUnpicked = colorUnpicked;
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
    public void setShape(@NotNull Shape shape) {
        if (shape == null) throw new NullPointerException("shape must not be null!");
        this.shape = shape;
    }

    /**
     * <p>
     *     Sets the color to use if node is selected.
     * </p>
     * @param colorPicked
     */
    public void setColorPicked(@NotNull Color colorPicked) {
        if (colorPicked == null) throw new NullPointerException("colorPicked must not be null!");
        this.colorPicked = colorPicked;
    }

    /**
     * <p>
     *     Sets the color to use if node is not selected.
     * </p>
     * @param colorUnpicked
     */
    public void setColorUnpicked(@NotNull Color colorUnpicked) {
        if (colorUnpicked == null) throw new NullPointerException("colorUnpicked must not be null!");
        this.colorUnpicked = colorUnpicked;
    }


    @Override
    public String name() {
        return "Node Renderer";
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
    public boolean update(INode update) {
        if (update == null) return false;
        MulticastNodeRendererComponent mnrc = update.getComposition().getComponent(MulticastNodeRendererComponent.class);
        if (mnrc == null) return false;

        setColorPicked(mnrc.getColorPicked());
        setColorUnpicked(mnrc.getColorUnpicked());

        return true;
    }
}
