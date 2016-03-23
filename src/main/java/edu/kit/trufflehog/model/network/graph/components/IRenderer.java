package edu.kit.trufflehog.model.network.graph.components;

import edu.kit.trufflehog.util.DeepCopyable;
import edu.kit.trufflehog.util.Updatable;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;

/**
 * Created by root on 26.02.16.
 */
public interface IRenderer extends DeepCopyable<IRenderer>, Updatable<IRenderer> {

    Shape getShape();

    Color getColorUnpicked();

    Color getColorPicked();

    void setColorPicked(Color colorPicked);

    void setColorUnpicked(Color colorUnpicked);

    void setShape(Shape shape);

    void animate();

    Color getDrawUnpicked();

    void setDrawUnpicked(Color drawUnpicked);

    Color getDrawPicked();

    void setDrawPicked(Color drawPicked);

    void togglePicked();

    void isPicked(boolean b);

    boolean picked();
}
