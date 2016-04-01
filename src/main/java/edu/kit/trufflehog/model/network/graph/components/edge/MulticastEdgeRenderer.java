package edu.kit.trufflehog.model.network.graph.components.edge;

import edu.kit.trufflehog.model.network.graph.IComposition;
import edu.kit.trufflehog.model.network.graph.IUpdater;
import edu.kit.trufflehog.model.network.graph.components.IRenderer;
import edu.kit.trufflehog.util.ICopyCreator;
import javafx.animation.Animation;
import javafx.animation.Transition;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

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

    private final Circle circle = new Circle(1);

    private final Animation animator;

    private final long animationTime = 500;

    private IComposition parent = null;

    public MulticastEdgeRenderer() {

        circle.setFill(Color.WHITE);
        circle.setVisible(false);



        animator = new Transition() {

            {
                setCycleDuration(Duration.millis(animationTime));
            }

            @Override
            protected void interpolate(double frac) {

                circle.setRadius(frac * 1000);
                circle.setOpacity(1 - frac);

            }
        };
        animator.setCycleCount(2);
        animator.setOnFinished(e -> {
            circle.setVisible(false);
        });
    }

    @Override
    public javafx.scene.shape.Shape getShape() {
        return circle;
    }

    @Override
    public javafx.scene.paint.Color getColorUnpicked() {
        throw new UnsupportedOperationException("Operation not implemented yet");
    }

    @Override
    public javafx.scene.paint.Color getColorPicked() {
        throw new UnsupportedOperationException("Operation not implemented yet");
    }

    @Override
    public void setColorPicked(javafx.scene.paint.Color colorPicked) {
        throw new UnsupportedOperationException("Operation not implemented yet");
    }

    @Override
    public void setColorUnpicked(javafx.scene.paint.Color colorUnpicked) {
        throw new UnsupportedOperationException("Operation not implemented yet");
    }

    @Override
    public void setShape(javafx.scene.shape.Shape shape) {
        throw new UnsupportedOperationException("Operation not implemented yet");
    }

    @Override
    public void animate() {

        circle.setVisible(true);
        animator.play();

    }

    @Override
    public void setParent(IComposition parent) {
        this.parent = parent;
    }

    @Override
    public javafx.scene.paint.Color getDrawUnpicked() {
        throw new UnsupportedOperationException("Operation not implemented yet");
    }

    @Override
    public void setDrawUnpicked(javafx.scene.paint.Color drawUnpicked) {
        throw new UnsupportedOperationException("Operation not implemented yet");
    }

    @Override
    public javafx.scene.paint.Color getDrawPicked() {
        throw new UnsupportedOperationException("Operation not implemented yet");
    }

    @Override
    public void setDrawPicked(javafx.scene.paint.Color drawPicked) {
        throw new UnsupportedOperationException("Operation not implemented yet");
    }

    @Override
    public void togglePicked() {
        throw new UnsupportedOperationException("Operation not implemented yet");
    }

    @Override
    public void isPicked(boolean b) {
        throw new UnsupportedOperationException("Operation not implemented yet");
    }

    @Override
    public boolean picked() {
        throw new UnsupportedOperationException("Operation not implemented yet");
    }

    @Override
    public IRenderer createDeepCopy(ICopyCreator copyCreator) {
        throw new UnsupportedOperationException("Operation not implemented yet");
    }

    @Override
    public boolean isMutable() {
        throw new UnsupportedOperationException("Operation not implemented yet");
    }

    @Override
    public boolean update(IRenderer instance, IUpdater updater) {
        throw new UnsupportedOperationException("Operation not implemented yet");
    }
}
