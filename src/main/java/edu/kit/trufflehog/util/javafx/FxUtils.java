package edu.kit.trufflehog.util.javafx;

import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.input.ScrollEvent;
import javafx.scene.input.ZoomEvent;
import javafx.scene.paint.Color;

/**
 * Created by root on 31.03.16.
 */
public class FxUtils {

    public static String toRGBCode( Color color )
    {
        return String.format( "#%02X%02X%02X",
                (int)( color.getRed() * 255 ),
                (int)( color.getGreen() * 255 ),
                (int)( color.getBlue() * 255 ) );
    }

    /** Allow to zoom/scale any node with pivot at scene (x,y) coordinates.
     *
     * @param node
     * @param factor
     * @param x
     * @param y
     */
    public static void zoom(Node node, double factor, double x, double y) {
        double oldScale = node.getScaleX();
        double scale = oldScale * factor;
        if (scale < 0.05) scale = 0.05;
        if (scale > 50)  scale = 50;
        node.setScaleX(scale);
        node.setScaleY(scale);

        double  f = (scale / oldScale)-1;
        Bounds bounds = node.localToScene(node.getBoundsInLocal());
        double dx = (x - (bounds.getWidth()/2 + bounds.getMinX()));
        double dy = (y - (bounds.getHeight()/2 + bounds.getMinY()));

        node.setTranslateX(node.getTranslateX()-f*dx);
        node.setTranslateY(node.getTranslateY()-f*dy);
    }

    public static void zoom(Node node, ScrollEvent event) {
        zoom(node, Math.pow(1.01, event.getDeltaY()), event.getSceneX(), event.getSceneY());
    }
    public static void zoom(Node node, ZoomEvent event) {
        zoom(node, event.getZoomFactor(), event.getSceneX(), event.getSceneY());
    }

}
