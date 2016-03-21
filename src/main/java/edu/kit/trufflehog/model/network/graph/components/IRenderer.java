package edu.kit.trufflehog.model.network.graph.components;

import edu.kit.trufflehog.util.DeepCopyable;
import edu.kit.trufflehog.util.Updatable;

import javax.swing.Icon;
import java.awt.Color;
import java.awt.Shape;
import java.awt.Stroke;

/**
 * Created by root on 26.02.16.
 */
public interface IRenderer extends DeepCopyable<IRenderer>, Updatable<IRenderer> {

    Shape getShape();

    Color getColorUnpicked();

    Color getColorPicked();

    Stroke getStroke();

    void setColorPicked(Color colorPicked);

    void setColorUnpicked(Color colorUnpicked);

    void setShape(Shape shape);

    void setStroke(Stroke stroke);

    void animate();

    int animationTime();

    Icon getIconPicked();

    void setIconPicked(Icon icon);

    Color getDrawUnpicked();

    void setDrawUnpicked(Color drawUnpicked);

    Color getDrawPicked();

    void setDrawPicked(Color drawPicked);

    Icon getIconUnpicked();

    void setIconUnpicked(Icon icon);

    void addCachedShape(Shape transformedShape);

    Shape getCachedShape();

    Shape getShape(double relation);
}
