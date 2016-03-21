package edu.kit.trufflehog.model.network.graph.components.node;

import edu.kit.trufflehog.model.network.graph.IUpdater;
import edu.kit.trufflehog.model.network.graph.components.IRenderer;
import edu.kit.trufflehog.util.ICopyCreator;
import org.apache.commons.collections15.buffer.BlockingBuffer;
import org.apache.logging.log4j.Logger;

import javax.swing.Icon;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.util.Arrays;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.logging.LogManager;

/**
 * This Component of a node will handle how a node is displayed in the graph
 * e.g. it is also possible to include bitmaps etc for specific Nodes
 *
 * @author Jan Hermes
 * @version 0.1
 */
public class NodeRenderer implements IRenderer {

    private static final Logger logger = org.apache.logging.log4j.LogManager.getLogger(NodeRenderer.class);

    private Icon iconUnpicked = null;
    private Icon iconPicked = null;
    //private Shape shape = new Rectangle2D.Double(-50,-50,100,100);

    private final BlockingQueue<Shape> cachedShapes = new ArrayBlockingQueue<>(5);

    private Shape shape = new Ellipse2D.Double(-30, -30, 60, 60);
    private Color colorPicked = new Color(0x4089BFf0, true);
    private Color colorUnpicked = new Color(0x4089BFa0, true);

    private final Shape shapes[] = new Shape[10];



    private Color drawPicked = new Color(0xC6E9FFf3, true);
    private Color drawUnpicked = new Color(0x4089BFd0, true);

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
    public void addCachedShape(Shape transformedShape) {
        cachedShapes.offer(transformedShape);
    }

    @Override
    public Shape getCachedShape() {
        return cachedShapes.poll();
    }

    private Shape getShape(int index) {

        if (shapes[index] == null) {

            //shapes[index] = new Ellipse2D.Double(-(index + 1) * 10, -(index + 1) * 10, (index + 1) * 20, (index + 1) * 20);
            shapes[index] = AffineTransform.getScaleInstance(0.3d + ((double) (index + 1) / 10d), 0.3d + ((double) (index + 1) / 10d)).createTransformedShape(shape);
          /*  for (int i = 1; i < 11; i++) {
                shapes[i - 1] = AffineTransform.getScaleInstance(0.3d + ((double)i / 10d), 0.3d + ((double)i / 10d)).createTransformedShape(shape);
            }*/
        }
        return shapes[index];
    }

    @Override
    public Shape getShape(double relation) {


        //logger.debug("relation: " + relation);

        //logger.debug("index: " + (int) ((shapes.length - 1) * relation));

        return getShape((int) ((shapes.length - 1) * relation));
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