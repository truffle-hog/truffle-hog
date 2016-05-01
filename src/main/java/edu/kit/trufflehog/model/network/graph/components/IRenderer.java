package edu.kit.trufflehog.model.network.graph.components;

import edu.kit.trufflehog.model.network.graph.IComposition;
import edu.kit.trufflehog.util.DeepCopyable;
import edu.kit.trufflehog.util.Updatable;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;

/**
 * Created by root on 26.02.16.
 */
public interface IRenderer extends DeepCopyable<IRenderer>, Updatable<IRenderer> {

    Shape getShape();

    void setShape(Shape shape);

    void animate();

    void setParent(IComposition parent);

}
