package edu.kit.trufflehog.model.network.graph.components.edge;

import edu.kit.trufflehog.model.network.graph.IUpdater;
import edu.kit.trufflehog.model.network.graph.components.IRenderer;
import edu.kit.trufflehog.util.ICopyCreator;
import javafx.animation.StrokeTransition;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Shape;
import javafx.util.Duration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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

    private Color colorUnpicked = Color.web("0x21252b");
    private Color colorPicked = Color.web("0x6CFF82");

    private Line line = new Line();

    private Color arrowFillPicked = Color.web("0x6CFF82");
    private Color arrowFillUnpicked = Color.web("0xfffffff0");

    private Color arrowDrawPicked = null;
    private Color arrowDrawUnpicked = null;

    private Shape arrowShapePicked = new Circle(8);
    private Shape arrowShapeUnpicked = new Circle(6);

    private double currentBrightness = 0.5f;

    // TODO Delete??
    private Shape shape = null;

    private final StrokeTransition animator;

    private int animationTime = 500;

    public BasicEdgeRenderer() {

        line.setStroke(colorUnpicked);

        animator = new StrokeTransition(Duration.millis(animationTime), line, colorUnpicked, Color.WHITE);
        animator.setCycleCount(1);
        animator.setAutoReverse(true);

/*        animator = new Transition() {

            {
                setCycleDuration(Duration.millis(animationTime));
            }

            protected void interpolate(double frac) {
                currentBrightness = (frac < 0.5 ? 0.5 + frac : 1.5 - frac);
            }
        };
        animator.setCycleCount(1);

        colorPicked.*/
    }

    @Override
    public Line getLine() {
        return line;
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
        return line;
    }

    @Override
    public Color getColorUnpicked() {

        return colorUnpicked;
    }

    @Override
    public Color getColorPicked() {
       return colorPicked;
    }

    @Override
    public void setColorUnpicked(Color colorUnpicked) {

        if (colorUnpicked == null) {
            throw new NullPointerException("colorUnpicked must not be null!");
        }

        this.colorUnpicked = colorUnpicked;
    }

    @Override
    public void setShape(Shape shape) {

        if (shape == null) {
            throw new NullPointerException("shape must not be null!");
        }

        this.shape = shape;
    }

    @Override
    public void animate() {
        animator.play();
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
    public boolean isMutable() {
        return true;
    }

    public void setColorPicked(Color colorPicked) {
        if (colorPicked == null) throw new NullPointerException("colorPicked must not be null!");
        this.colorPicked = colorPicked;
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

    public double getCurrentBrightness() {
        return currentBrightness;
    }

    public void setCurrentBrightness(double currentBrightness) {
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
