package edu.kit.trufflehog.model.network.graph.components.edge;

import edu.kit.trufflehog.model.network.graph.IComposition;
import edu.kit.trufflehog.model.network.graph.IUpdater;
import edu.kit.trufflehog.model.network.graph.components.IRenderer;
import edu.kit.trufflehog.util.ICopyCreator;
import javafx.animation.Animation;
import javafx.animation.Transition;
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.QuadCurve;
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

    private Color colorUnpicked = Color.web("0xaaaaaa");
    private Color colorPicked = Color.web("0x6CFF82");

    private final double edgeWidthPickedMultiplier = 2;
    private final double edgeWidthUnpickedMultiplier = 1;

    private final DoubleProperty edgeWidthMultiplierProperty = new SimpleDoubleProperty(edgeWidthUnpickedMultiplier);

    private QuadCurve line = new QuadCurve();

    private Color arrowFillPicked = Color.web("0x6CFF82");
    private Color arrowFillUnpicked = Color.web("0xaaaaaa");

    private Color arrowDrawPicked = null;
    private Color arrowDrawUnpicked = null;

    private Shape arrowShapePicked = new Circle(8);
    private Shape arrowShapeUnpicked = new Circle(6);

    private Color currentArrowFill = arrowFillUnpicked;
    private Color currentEdgeStrokePaint = colorUnpicked;
    private Color currentAnimationEndColor = Color.WHITE;

    private double currentBrightness = 0.5f;

    private ObjectProperty<Color> arrowStrokePaintProperty = new SimpleObjectProperty<>(colorUnpicked);
    private ObjectProperty<Color> arrowHeadFillProperty = new SimpleObjectProperty<>(arrowFillUnpicked);

    // TODO Delete??
    private Shape shape = null;

    private final Animation animator;

    private int animationTime = 500;
    private Shape arrowShape = new Circle(16);
    private boolean isPicked = false;

    private IComposition parent;

    public BasicEdgeRenderer() {

       // line.setStroke(colorUnpicked);
        line.strokeProperty().bind(arrowStrokePaintProperty);
        arrowShape.fillProperty().bind(arrowHeadFillProperty);

        //animator = new StrokeTransition(Duration.millis(animationTime), arrowStrokePaintProperty, Color.WHITE);

        animator = new Transition() {
            {
                setCycleDuration(Duration.millis(animationTime));
            }
            protected void interpolate(double frac) {

                arrowStrokePaintProperty.set(currentEdgeStrokePaint.interpolate(currentAnimationEndColor, frac));
                arrowHeadFillProperty.set(currentArrowFill.interpolate(currentAnimationEndColor, frac));
            }
        };
        animator.setCycleCount(2);
        animator.setAutoReverse(true);
    }

    @Override
    public QuadCurve getLine() {
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
    public void setParent(IComposition parent) {
        this.parent = parent;
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

    @Override
    public void setCurrentFill(Color fill) {
        currentEdgeStrokePaint = fill;
        currentArrowFill = fill;
        arrowHeadFillProperty.set(fill);
        arrowStrokePaintProperty.set(fill);
    }

    @Override
    public Shape getArrowShape() {
        return arrowShape;
    }

    @Override
    public DoubleProperty edgeWidthMultiplierProperty() {
        return edgeWidthMultiplierProperty;
    }
}
