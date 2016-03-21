package edu.kit.trufflehog.model.network.graph.components.edge;

import edu.kit.trufflehog.model.network.graph.IUpdater;
import edu.kit.trufflehog.model.network.graph.components.IRenderer;
import edu.kit.trufflehog.util.ICopyCreator;
import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.util.Duration;

import javax.swing.Icon;
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
public class MulticastEdgeRenderer implements IRenderer {

    private long lastUpdate = Instant.now().toEpochMilli();

    private Shape shape = new Ellipse2D.Float(-10, -10, 20, 20);

    Color colorUnpicked = new Color(0xffffff);
    Color colorPicked = new Color(0xffffff);

    private final float baseWidth = 90f;
    private final float baseSize = 80f;

    private float strokeWidth = 0;
    private float multiplier = 0;
    private float opacity = 0;

    private float opac = 0.5f;

    private Stroke stroke = new BasicStroke(strokeWidth);

    private final Transition animator;
    public MulticastEdgeRenderer() {

        animator = new Transition() {

            {
                setCycleDuration(Duration.millis(500));
            }

            protected void interpolate(double frac) {

                    strokeWidth = (float) (frac * baseWidth);
                    opac = frac >= 0.6 ? 0 : (float) (0.6f - frac);
                    multiplier = (float) (baseSize * frac);
            }

        };
        animator.setCycleCount(2);
        animator.setInterpolator(Interpolator.EASE_OUT);
        animator.setOnFinished(e -> {
                multiplier = 0;
                opac = 0;
                strokeWidth = 0;
        });


        // TODO implement
    }

    @Override
    public Shape getShape() {

        return AffineTransform.getScaleInstance(multiplier, multiplier).createTransformedShape(shape);
    }

    @Override
    public Color getColorUnpicked() {

        return new Color(colorUnpicked.getColorSpace(), colorUnpicked.getColorComponents(null), opac);

        //return new Color(colorUnpicked.getRed(), colorUnpicked.getGreen(), colorUnpicked.getBlue(), (int) opacity);
    }

    @Override
    public Color getColorPicked() {

        return new Color(colorPicked.getColorSpace(), colorPicked.getColorComponents(null), opac);
        //return new Color(colorPicked.getRed(), colorPicked.getGreen(), colorPicked.getBlue(), (int) opacity);
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
    public void animate() {

        animator.play();
    }

    @Override
    public int animationTime() {
        return 20 * 40;
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

    public float getOpacity() {
        return opacity;
    }

    public void setOpacity(float opacity) {
        this.opacity = opacity;
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
        return (o instanceof MulticastEdgeRenderer);
    }

    public void setStrokeWidth(float strokeWidth) {
        this.strokeWidth = strokeWidth;
    }

    public void setMultiplier(float multiplier) {
        this.multiplier = multiplier;
    }
}
