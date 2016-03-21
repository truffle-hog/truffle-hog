package edu.kit.trufflehog.model.network.graph.components.edge;

import edu.kit.trufflehog.model.network.graph.IUpdater;
import edu.kit.trufflehog.model.network.graph.components.IRenderer;
import edu.kit.trufflehog.util.ICopyCreator;
import javafx.animation.Transition;
import javafx.util.Duration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
 *          <p/>
 *          TODO FIX GETTER!!!
 */
public class BasicEdgeRenderer implements IEdgeRenderer {

    private static final Logger logger = LogManager.getLogger();

    private Color colorUnpicked = new Color(0x21252b);
    private float[] hsbValsUnpicked = new float[3];


    private Color colorPicked = new Color(0x6CFF82);
    private float[] hsbValsPicked = new float[3];

    private Shape shape = new Ellipse2D.Float(-10, -10, 20, 20);

    private Color arrowFillPicked = new Color(0x6CFF82);
    private Color arrowFillUnpicked = new Color(0xfffffff0, true);

    private Color arrowDrawPicked = null;
    private Color arrowDrawUnpicked = null;

    private Shape arrowShapePicked = new Ellipse2D.Float(-8, -8, 16, 16);
    private Shape arrowShapeUnpicked = new Ellipse2D.Float(-6, -6, 12, 12);


    //TODO change this!
    private Stroke stroke = new BasicStroke();

    private float currentBrightness = 0.5f;

    private final Transition animator;

    private int animationTime = 500;

    public BasicEdgeRenderer() {

        animator = new Transition() {

            {
                setCycleDuration(Duration.millis(animationTime));
            }

            protected void interpolate(double frac) {
                currentBrightness = (float) (frac < 0.5 ? 0.5 + frac : 1.5 - frac);
            }
        };

        animator.setCycleCount(1);

        Color.RGBtoHSB(colorUnpicked.getRed(), colorUnpicked.getGreen(), colorUnpicked.getBlue(), hsbValsUnpicked);
        Color.RGBtoHSB(colorPicked.getRed(), colorPicked.getGreen(), colorPicked.getBlue(), hsbValsPicked);

        currentBrightness = hsbValsUnpicked[2];
    }

    @Override
    public Shape getArrowShapePicked() {
        return arrowShapePicked;
    }

    @Override
    public void setArrowShapePicked(Shape arrowShapePicked) {
        this.arrowShapePicked = arrowShapePicked;
    }

    @Override
    public Shape getArrowShapeUnpicked() {
        return arrowShapeUnpicked;
    }

    @Override
    public void setArrowShapeUnpicked(Shape arrowShapeUnpicked) {
        this.arrowShapeUnpicked = arrowShapeUnpicked;
    }


    @Override
    public Color getArrowDrawPicked() {
        return arrowDrawPicked;
    }
    @Override
    public void setArrowDrawPicked(Color arrowDrawPicked) {
        this.arrowDrawPicked = arrowDrawPicked;
    }
    @Override
    public Color getArrowDrawUnpicked() {
        return arrowDrawUnpicked;

    }
    @Override
    public void setArrowDrawUnpicked(Color arrowDrawUnpicked) {
        this.arrowDrawUnpicked = arrowDrawUnpicked;
    }

    @Override
    public Shape getShape() {
        return shape;
    }

    @Override
    public Color getColorUnpicked() {

        return new Color(Color.HSBtoRGB(hsbValsUnpicked[0], hsbValsUnpicked[1], currentBrightness));
    }

    @Override
    public Color getColorPicked() {
        // TODO implement
        return new Color(Color.HSBtoRGB(hsbValsPicked[0], hsbValsPicked[1],
                (currentBrightness <= 0.7f) ? (currentBrightness + 0.3f) : 1.0f));
    }

    @Override
    public Stroke getStroke() {
        return stroke;
    }

    @Override
    public void setColorUnpicked(Color colorUnpicked) {

        if (colorUnpicked == null) {
            throw new NullPointerException("colorUnpicked must not be null!");
        }

        this.colorUnpicked = colorUnpicked;
        Color.RGBtoHSB(this.colorUnpicked.getRed(), this.colorUnpicked.getGreen(), this.colorUnpicked.getBlue(), hsbValsPicked);
    }

    @Override
    public void setShape(Shape shape) {

        if (shape == null) {
            throw new NullPointerException("shape must not be null!");
        }

        this.shape = shape;
    }

    @Override
    public void setStroke(Stroke stroke) {
        if (stroke == null) {
            throw new NullPointerException("stroke must not be null!");
        }
        this.stroke = stroke;
    }

    @Override

    public void animate() {
        animator.play();
    }

    @Override
    public int animationTime() {
        return animationTime;
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
    public boolean isMutable() {
        return true;
    }

    public void setColorPicked(Color colorPicked) {
        if (colorPicked == null) throw new NullPointerException("colorPicked must not be null!");
        this.colorPicked = colorPicked;

        Color.RGBtoHSB(this.colorPicked.getRed(), this.colorPicked.getGreen(), this.colorPicked.getBlue(), hsbValsPicked);
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
        return (o instanceof BasicEdgeRenderer);
    }

    public float getCurrentBrightness() {
        return currentBrightness;
    }

    public void setCurrentBrightness(float currentBrightness) {
        this.currentBrightness = currentBrightness;
    }

    @Override
    public Color getArrowFillPicked() {
        return arrowFillPicked;
    }

    @Override
    public Color getArrowFillUnpicked() {
        return arrowFillUnpicked;
    }

    @Override
    public void setArrowFillPicked(Color arrowFillPicked) {
        this.arrowFillPicked = arrowFillPicked;
    }

    @Override
    public void setArrowFillUnpicked(Color arrowFillUnpicked) {
        this.arrowFillUnpicked = arrowFillUnpicked;
    }
}
