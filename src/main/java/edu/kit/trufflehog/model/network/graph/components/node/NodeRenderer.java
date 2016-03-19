package edu.kit.trufflehog.model.network.graph.components.node;

import edu.kit.trufflehog.model.network.graph.IUpdater;
import edu.kit.trufflehog.model.network.graph.components.IRenderer;
import edu.kit.trufflehog.util.ICopyCreator;

import javax.swing.Icon;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.Ellipse2D;

/**
 * This Component of a node will handle how a node is displayed in the graph
 * e.g. it is also possible to include bitmaps etc for specific Nodes
 *
 * @author Jan Hermes
 * @version 0.1
 */
public class NodeRenderer implements IRenderer {
    private Icon iconUnpicked = null;
    private Icon iconPicked = null;
    //private Shape shape = new Rectangle2D.Double(-50,-50,100,100);
    private Shape shape = new Ellipse2D.Double(-50, -50, 100, 100);
    private Color colorPicked = new Color(0x4089BFf0, true);
    private Color colorUnpicked = new Color(0x4089BFa0, true);



    private Color drawPicked = new Color(0xC6E9FF, true);
    private Color drawUnpicked = new Color(0x4089BFa0, true);

    private Stroke stroke = new BasicStroke(7f);

    /**
     * <p>
     *     Creates a component using default values.
     * </p>
     */
    public NodeRenderer() {
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

    public NodeRenderer(Icon picked, Icon unpicked) {

        if (picked == null) throw new NullPointerException("picked icon must not be null");
        if (unpicked == null) throw new NullPointerException("unpicked icon must not be null");

        this.iconUnpicked = unpicked;
        this.iconPicked = picked;
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
    public Icon getIconUnpicked() {
        return iconUnpicked;
    }

    @Override
    public void setIconUnpicked(Icon iconUnpicked) {
        this.iconUnpicked = iconUnpicked;
    }

    @Override
    public Icon getIconPicked() {
        return iconPicked;
    }

    @Override
    public void setIconPicked(Icon iconPicked) {
        this.iconPicked = iconPicked;
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

    @Override
    public Stroke getStroke() {
        return stroke;
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
    public void setStroke(Stroke stroke) {
        if (stroke == null) { throw new NullPointerException("stroke must not be null!"); }
        this.stroke = stroke;
    }

    @Override
    public void animate() {

    }

    @Override
    public int animationTime() {
        return 0;
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