package edu.kit.trufflehog.view.graph.decorators;

import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by root on 26.02.16.
 */
public class LayeredShape implements Shape {

    private final Shape outermost;
    private final List<Shape> allShapes = new LinkedList<>();
    private final int maxShapeCount;
    private final int maxSize;

    public LayeredShape(int maxShapeCount, int maxSize, Shape ...shapes) {

        this.allShapes.addAll(Arrays.asList(shapes));
        this.outermost = this.allShapes.get(0);
        this.maxShapeCount = maxShapeCount;
        this.maxSize = maxSize;
    }

    public void addShape(Shape shape) {

        if (allShapes.size() == maxShapeCount) {
            allShapes.remove(allShapes.get(0));
        }
        allShapes.add(shape);
    }

    public void increase(float multiplier) {

        float scaler = 1.0f;

        for (ListIterator<Shape> cursor = allShapes.listIterator(); cursor.hasNext();) {

            final Shape next = cursor.next();
            cursor.remove();

            if (next.getBounds2D().getWidth() > maxSize) {
                continue;
            }
            cursor.add(AffineTransform.getScaleInstance(multiplier * scaler, multiplier * scaler).createTransformedShape(next));

            scaler += 0.5f;
        }


    }

    public void multiply(float value) {

/*        allShapes.stream().forEach(shape -> {
            shape.
        });*/
    }

    @Override
    public Rectangle getBounds() {
        return outermost.getBounds();
    }

    @Override
    public Rectangle2D getBounds2D() {
        return outermost.getBounds2D();
    }

    @Override
    public boolean contains(double x, double y) {
        return outermost.contains(x, y);
    }

    @Override
    public boolean contains(Point2D p) {
        return outermost.contains(p);
    }

    @Override
    public boolean intersects(double x, double y, double w, double h) {
        return outermost.intersects(x, y, w, h);
    }

    @Override
    public boolean intersects(Rectangle2D r) {
        return outermost.intersects(r);
    }

    @Override
    public boolean contains(double x, double y, double w, double h) {
        return outermost.contains(x, y, w, h);
    }

    @Override
    public boolean contains(Rectangle2D r) {
        return outermost.contains(r);
    }

    @Override
    public PathIterator getPathIterator(AffineTransform at) {
        return ShapeUtil.concatPathIterators(new LinkedList<>(allShapes), at);
    }

    @Override
    public PathIterator getPathIterator(AffineTransform at, double flatness) {
        return ShapeUtil.concatPathIterators(new LinkedList<>(allShapes), at, flatness);
    }
}
